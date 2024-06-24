package com.trafficinfosystem.demo.vo;

import lombok.Data;

@Data
public class UsageStatsVO {
    private String id;  // Fields used for grouping
    private Long totalUsage;
}
