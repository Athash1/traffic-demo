package com.trafficinfosystem.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TrainInformation {
    @JsonProperty("odpt:operator")
    private String operator;

    @JsonProperty("odpt:trainInformationText")
    private TrainInformationText trainInformationText;

    @Data
    public static class TrainInformationText {
        private String ja;
        private String en;
    }
}
