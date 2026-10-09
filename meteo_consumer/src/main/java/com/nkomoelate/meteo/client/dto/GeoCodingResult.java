package com.nkomoelate.meteo.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

public record GeoCodingResult(
        String name,
        double latitude,
        double longitude,
        String country,
        String timeZone,
        @JsonProperty("admin1") String region,
        @JsonProperty("admin2") String department,
        @JsonProperty("admin3") String chefLieu
) {}
