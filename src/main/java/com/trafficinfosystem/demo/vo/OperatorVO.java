package com.trafficinfosystem.demo.vo;

import com.trafficinfosystem.demo.entity.Operator;
import lombok.Data;

@Data
public class OperatorVO {
    private String sameAs;
    private Operator.Title title;
}