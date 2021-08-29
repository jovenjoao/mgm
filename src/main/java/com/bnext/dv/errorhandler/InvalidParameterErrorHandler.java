package com.bnext.dv.errorhandler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import javax.management.InstanceNotFoundException;
import java.security.InvalidParameterException;

@Produces
@Singleton
@Slf4j
@Requires(classes = {InvalidParameterException.class})
public class InvalidParameterErrorHandler implements ExceptionHandler<InvalidParameterException, HttpResponse<ErrorService>> {

    @Override
    public HttpResponse<ErrorService> handle(HttpRequest request, InvalidParameterException exception) {

        log.error("Generic error : {}", exception.getMessage());

        ErrorService error = new ErrorService("invalid-request",request.getPath(),exception.getMessage());
        return HttpResponse.badRequest(error);
    }

}
