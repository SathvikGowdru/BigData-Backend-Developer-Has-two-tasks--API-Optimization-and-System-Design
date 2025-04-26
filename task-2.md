
## 1. Architecture Diagram
Internet
  |
┌────────────┐
│   NGINX    │  - TLS Termination
│ Load Balancer │  - Round-robin to API pods
└─────┬──────┘
      │
┌─────┴────┬────┬────┐
│ API Pod #1 │ API Pod #2 │ API Pod #3 │ ... (up to Pod #5)
└─────┬────┴────┴────┘
      │
   [In-memory cache: YardMap]
      │
  [Refresh thread: file/db every 10s]
      │
  [Prometheus + Grafana / Logs to Loki]


## 2. Component List
- **NGINX (Reverse Proxy)**
  - Handles TLS termination and forwards requests via round-robin.
  - Lightweight, easy to configure, supports health checks.

- **API Pods (Spring Boot JARs)**
  - Stateless services running `/pickSpot` endpoint.
  - Reads yardMap from local in-memory cache.

- **In-memory Cache**
  - `ConcurrentHashMap` storing current yardMap.
  - Background thread refreshes data from file or database every 10 seconds.

- **Observability Stack**
  - Prometheus for scraping metrics from Spring Actuator.
  - Grafana for dashboards.
  - Alerts via Alertmanager to Slack/Email.

## 3. Concurrency Model
- **Normal Traffic:**
  - 3 pods, each handles ~120 rps → total = 360 rps headroom.

- **Peak Traffic:**
  - Scale to 5 pods → ~600 rps capacity.

- **Load Balancing:**
  - NGINX evenly distributes traffic (round-robin).

- **Back-pressure Handling:**
  - Pod returns 503 if overloaded; NGINX retries on next pod.
  - Optionally configure queue/retry policy in Spring Boot.

## 4. Failure Story
- **API Pod Crash:**
  - NGINX health check stops routing to it.
  - Remaining pods continue; slight performance dip but service remains available.

- **Cache Refresh Failure:**
  - Fallback to previous yardMap version.
  - Warning log issued; restart script can be triggered automatically or by admin.

- **NGINX Crash:**
  - Use redundant NGINX nodes behind a cloud load balancer (e.g., AWS ALB).

## 5. Scaling Strategy
- **Horizontal Auto-scaling:**
  - Rule: If CPU > 70% for 1 minute → add 1 pod.
  - If CPU < 30% for 3 minutes → remove 1 pod.
  - Metrics pulled from Prometheus or K8s HPA.

## 6. Metrics & Alerts
- **Key Metrics:**
  - `http_server_requests_seconds` → P95 latency
  - `http_server_errors_total` → error rate
  - `process_cpu_usage` / `jvm_memory_used_bytes`

- **Sample Alert:**
  - *"ALERT: /pickSpot P95 latency > 400ms for 5 min"*
  - Triggers Slack/Email notification.
