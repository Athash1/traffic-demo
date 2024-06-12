package com.trafficinfosystem.demo.entity;

import lombok.Data;

import java.util.List;

@Data
public class LineResponse {
    String destination;
    List<Event> events;
}
