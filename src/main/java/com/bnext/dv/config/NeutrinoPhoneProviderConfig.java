package com.bnext.dv.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties("neutrino")
public class NeutrinoPhoneProviderConfig {

    private String host;

    private String validatePhonePath;

    private String userId;

    private String apiKey;

}
