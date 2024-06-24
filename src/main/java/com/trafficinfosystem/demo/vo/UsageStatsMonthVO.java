package com.trafficinfosystem.demo.vo;

import lombok.Data;

@Data
public class UsageStatsMonthVO {
    private String id;  // Fields used for grouping
    private Long totalUsage; // Total usage time
}
