package com.bnext.dv.config;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.dozer.DozerBeanMapper;

@Factory
public class DozerConfigFiles {

    @Singleton
    public DozerBeanMapper configureDozerBeanMapper (){
        return new DozerBeanMapper();
    }

}
