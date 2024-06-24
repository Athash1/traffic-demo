package com.trafficinfosystem.demo.entity;

import lombok.Data;

import java.util.List;
@Data
public class Response {

    private String destination;

    private List<Event> events;

}
