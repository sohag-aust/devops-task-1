package com.task.devops.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException; // Import for 4xx errors
import org.springframework.web.client.HttpServerErrorException; // Import for 5xx errors
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class}) // Catch 4xx and 5xx
    public ModelAndView handleHttpExceptions(Exception ex) {
        ModelAndView mav = new ModelAndView("error");

        if (ex instanceof HttpClientErrorException) {
            HttpClientErrorException httpException = (HttpClientErrorException) ex;
            if (httpException.getStatusCode() == HttpStatus.FORBIDDEN) {
                mav.addObject("errorMessage", "API limit reach. Please try again later.");
                mav.setStatus(HttpStatus.FORBIDDEN);
                return mav;
            } else if (httpException.getStatusCode() == HttpStatus.NOT_FOUND){
                mav.addObject("errorMessage", "Resource not found. Please try again later.");
                mav.setStatus(HttpStatus.NOT_FOUND);
                return mav;
            }
            else {
                mav.addObject("errorMessage", "A client error occurred: " + httpException.getMessage());
                mav.setStatus(httpException.getStatusCode());
                return mav;
            }
        } else if (ex instanceof HttpServerErrorException) {
            HttpServerErrorException httpException = (HttpServerErrorException) ex;
            mav.addObject("errorMessage", "A server error occurred: " + httpException.getMessage());
            mav.setStatus(httpException.getStatusCode());
            return mav;
        }else{
            mav.addObject("errorMessage", "An error occurred: " + ex.getMessage());
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }
    }

    @ExceptionHandler(CustomException.class)
    public ModelAndView handleCustomException(CustomException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", ex.getMessage());
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }

    public static class CustomException extends RuntimeException {
        public CustomException(String message) {
            super(message);
        }
    }
}