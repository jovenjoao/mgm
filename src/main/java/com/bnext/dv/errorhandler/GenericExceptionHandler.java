package com.bnext.dv.errorhandler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import javax.management.InstanceNotFoundException;

@Produces
@Singleton
@Slf4j
@Requires(classes = {Exception.class})
public class GenericExceptionHandler implements ExceptionHandler<Exception, HttpResponse> {

    @Override
    public HttpResponse<ErrorService> handle(HttpRequest request, Exception exception) {

        log.error("Generic error : {}", exception.getMessage());

        ErrorService error = new ErrorService("unexpected-error",request.getPath(),exception.getMessage());
        return HttpResponse.serverError(error);
    }

}
