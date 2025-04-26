
package com.example.pickaspot.dto;

public class PickSpotResponse {
    public String containerId;
    public int targetX;
    public int targetY;
    public String error;

    public static PickSpotResponse success(String containerId, int x, int y) {
        PickSpotResponse res = new PickSpotResponse();
        res.containerId = containerId;
        res.targetX = x;
        res.targetY = y;
        return res;
    }

    public static PickSpotResponse error(String message) {
        PickSpotResponse res = new PickSpotResponse();
        res.error = message;
        return res;
    }
}
