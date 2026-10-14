package com.nkomoelate.meteo.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnumSkyWeatherTest {

    @Test
    void fromCode_codeConnu_retourneLaBonneCategorie() {
        assertThat(EnumSkyWeather.fromCode(0)).isEqualTo(EnumSkyWeather.CLEAR);
        assertThat(EnumSkyWeather.fromCode(3)).isEqualTo(EnumSkyWeather.CLOUDY);
        assertThat(EnumSkyWeather.fromCode(61)).isEqualTo(EnumSkyWeather.RAIN);
        assertThat(EnumSkyWeather.fromCode(86)).isEqualTo(EnumSkyWeather.SNOW);
        assertThat(EnumSkyWeather.fromCode(99)).isEqualTo(EnumSkyWeather.STORM);
    }

    @Test
    void fromCode_codeInconnu_retourneUnknown() {
        assertThat(EnumSkyWeather.fromCode(42)).isEqualTo(EnumSkyWeather.UNKNOWN);
    }

    @Test
    void fromCode_codeNull_retourneUnknown() {
        assertThat(EnumSkyWeather.fromCode(null)).isEqualTo(EnumSkyWeather.UNKNOWN);
    }
}