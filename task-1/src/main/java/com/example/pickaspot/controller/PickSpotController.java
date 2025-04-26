
package com.example.pickaspot.controller;

import com.example.pickaspot.dto.PickSpotRequest;
import com.example.pickaspot.dto.PickSpotResponse;
import com.example.pickaspot.service.SpotScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PickSpotController {

    @Autowired
    private SpotScoringService scoringService;

    @PostMapping("/pickSpot")
    public PickSpotResponse pickSpot(@RequestBody PickSpotRequest request) {
        return scoringService.findBestSpot(request);
    }
}
