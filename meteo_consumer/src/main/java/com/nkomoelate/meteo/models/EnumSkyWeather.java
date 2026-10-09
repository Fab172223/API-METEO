package com.nkomoelate.meteo.models;

public enum EnumSkyWeather {
    CLEAR("Ciel dégagé"),
    CLOUDY("Nuageux"),
    FOG("Brouillard"),
    DRIZZLE("Bruine"),
    RAIN("Pluie"),
    SNOW("Neige"),
    STORM("Orage"),
    UNKNOWN("Inconnu");

    private final String label;

    EnumSkyWeather(String label){
        this.label=label;
    }

    public String getLabel(){
        return this.label;
    }

    public static EnumSkyWeather getLabelFromCode(Integer code){
       return switch(code){
           case 0 -> CLEAR;
           case 1,2,3 -> CLOUDY;
           case 45,48 -> FOG;
           case 51,53,55,56,57 -> DRIZZLE;
           case 61,63,65,66,67,80,81,82 -> RAIN;
           case 71,73,75,77,85,76 -> SNOW;
           case 95,96,99 -> STORM;
           default -> UNKNOWN;
       };
    }
}
