package com.bnext.dv.controller.request;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Introspected
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank
    private String user;
}
