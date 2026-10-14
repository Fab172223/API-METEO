package com.nkomoelate.meteo.exceptions;

public class CityNotFoundException extends RuntimeException{

    public CityNotFoundException(String city){
        super(city +": Ville non trouvée");
    }
}
