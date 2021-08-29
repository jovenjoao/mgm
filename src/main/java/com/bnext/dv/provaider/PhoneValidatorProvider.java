package com.bnext.dv.provaider;

import com.bnext.dv.provaider.model.ValidateResult;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PhoneValidatorProvider {

    ValidateResult validatePhoneNumber (String phoneNumber, String country) ;


}
