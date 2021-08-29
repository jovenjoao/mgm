package com.bnext.dv.provaider.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneValidatorResponse {

    private String country;
    @JsonProperty ("country-code")
    private String countryCode;
    @JsonProperty ("country-code3")
    private String countryCodeISO;
    @JsonProperty ("currency-code")
    private String currencyCode;
    @JsonProperty ("international-calling-code")
    private String internationalCallingCode;
    @JsonProperty ("international-number")
    private String internationalNumber;
    @JsonProperty ("is-mobile")
    private String isMobile;
    @JsonProperty ("local-number")
    private String localNumber;
    @JsonProperty ("location")
    private String location;
    @JsonProperty ("prefix-network")
    private String prefixNetwork;
    private String type;
    private Boolean valid;
}
