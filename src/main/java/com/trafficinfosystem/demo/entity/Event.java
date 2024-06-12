package com.trafficinfosystem.demo.entity;

import lombok.Data;

@Data
public class Event {
    private String type;
    private Message message;
    private long timestamp;
    private Source source;
    private String replyToken;
    private String mode;
    private String webhookEventId;
    private DeliveryContext deliveryContext;
}
