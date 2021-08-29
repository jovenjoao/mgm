package com.bnext.dv.controller.request;

import com.bnext.dv.model.Contact;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Introspected
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private Contact contact;
}
