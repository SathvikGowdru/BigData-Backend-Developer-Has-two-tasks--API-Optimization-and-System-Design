
package com.example.pickaspot.service;

import com.example.pickaspot.dto.PickSpotRequest;
import com.example.pickaspot.dto.PickSpotResponse;
import com.example.pickaspot.model.Container;
import com.example.pickaspot.model.YardSlot;
import org.springframework.stereotype.Service;

@Service
public class SpotScoringService {

    private static final int INVALID = 10_000;

    public PickSpotResponse findBestSpot(PickSpotRequest request) {
        Container c = request.container;
        int bestScore = Integer.MAX_VALUE;
        YardSlot bestSlot = null;

        for (YardSlot slot : request.yardMap) {
            int score = computeScore(c, slot);
            if (score < bestScore) {
                bestScore = score;
                bestSlot = slot;
            }
        }

        if (bestScore >= INVALID) {
            return PickSpotResponse.error("no suitable slot");
        } else {
            return PickSpotResponse.success(c.id, bestSlot.x, bestSlot.y);
        }
    }

    private int computeScore(Container c, YardSlot slot) {
        int distance = Math.abs(c.x - slot.x) + Math.abs(c.y - slot.y);
        int sizePenalty = isSizeFit(c.size, slot.sizeCap) ? 0 : INVALID;
        int coldPenalty = c.needsCold && !slot.hasColdUnit ? INVALID : 0;
        int occupiedPenalty = slot.occupied ? INVALID : 0;

        return distance + sizePenalty + coldPenalty + occupiedPenalty;
    }

    private boolean isSizeFit(String containerSize, String slotSizeCap) {
        return containerSize.equals("small") || (containerSize.equals("big") && slotSizeCap.equals("big"));
    }
}
