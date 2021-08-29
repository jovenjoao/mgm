package com.bnext.dv.errorhandler;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;


import javax.management.InstanceNotFoundException;

@Produces
@Singleton
@Slf4j
@Requires(classes = {InstanceNotFoundException.class})
public class NotFoundErrorHandler implements ExceptionHandler<InstanceNotFoundException, HttpResponse<ErrorService>> {

    @Override
    public HttpResponse<ErrorService> handle(HttpRequest request, InstanceNotFoundException exception) {

        log.error("Error instance not found : {}", exception.getMessage());

        ErrorService error = new ErrorService("object-not-found",request.getPath(),exception.getMessage());
        return HttpResponse.notFound(error);
    }
}
