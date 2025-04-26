
package com.example.pickaspot.dto;

import com.example.pickaspot.model.Container;
import com.example.pickaspot.model.YardSlot;

import java.util.List;

public class PickSpotRequest {
    public Container container;
    public List<YardSlot> yardMap;
}
