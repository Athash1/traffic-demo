package com.trafficinfosystem.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
class Journey {
    private String origin;        // 起点
    private String destination;   // 终点
    private LocalDateTime travelTime; // 出行时间
    private String travelMode;    // 出行模式（如公交、地铁、步行等）
}