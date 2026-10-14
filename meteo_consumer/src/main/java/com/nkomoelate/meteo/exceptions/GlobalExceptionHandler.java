package com.nkomoelate.meteo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CityNotFoundException.class)
    public ProblemDetail cityNotFound(CityNotFoundException cityNotFoundException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, cityNotFoundException.getMessage());
    }

    @ExceptionHandler(WeatherProviderException.class)
    public ProblemDetail openMeteoApiNotAvailable(WeatherProviderException weatherProviderException){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY,weatherProviderException.getMessage());
    }

}
