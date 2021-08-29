package com.bnext.dv.provaider.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateResult {

    private String country;
    private String countryCode;
    private String countryCodeISO;
    private String currencyCode;
    private String internationalCallingCode;
    private String internationalNumber;
    private String isMobile;
    private String localNumber;
    private String location;
    private String prefixNetwork;
    private String type;
    private Boolean valid;

}
