package com.trafficinfosystem.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.trafficinfosystem.demo.utils.CustomLocalDateTimeDeserializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "railway")
@Data
public class Railway {
    @Id
    private String id;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("dc:date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime date;

    @JsonProperty("@context")
    private String context;

    @JsonProperty("dc:title")
    private String title;

    @JsonProperty("owl:sameAs")
    private String sameAs;

    @JsonProperty("odpt:operator")
    private String operator;

    @JsonProperty("odpt:railwayTitle")
    private Title railwayTitle;

    @Data
    public static class Title {
        @JsonProperty("ja")
        private String japanese;

        @JsonProperty("en")
        private String english;
    }
}
