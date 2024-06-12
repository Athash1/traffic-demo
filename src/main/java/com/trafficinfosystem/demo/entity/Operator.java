package com.trafficinfosystem.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.trafficinfosystem.demo.utils.CustomLocalDateTimeDeserializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "operator")
public class Operator {
    @Id
    @JsonProperty("@id")
    private String id;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("dc:date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime date;

    @JsonProperty("owl:sameAs")
    private String sameAs;

    @JsonProperty("dc:title")
    private String title;

    @JsonProperty("odpt:operatorTitle")
    private Title operatorTitle;

    @Data
    public static class Title {
        @JsonProperty("ja")
        private String japanese;

        @JsonProperty("en")
        private String english;
    }
}
