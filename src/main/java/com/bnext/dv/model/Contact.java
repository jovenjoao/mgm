package com.bnext.dv.model;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Introspected
public class Contact {

    @NonNull
    @NotBlank
    @Pattern(regexp = "(6|7|8|9)\\d{9}", message = "Must not contain numbers")
    private String phoneNumber;

    @NonNull
    @NotBlank
    private String contactName;

    private String verifyCode;

    public String getId (){
        return phoneNumber;
    }


    private String country;
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
