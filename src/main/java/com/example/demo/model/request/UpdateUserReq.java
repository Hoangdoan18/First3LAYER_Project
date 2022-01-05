package com.example.demo.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserReq {
    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @Pattern(regexp = "[0-9]+" , message = "Please provide a valid phone number")
    private String phone;

    @Valid
    @URL(regexp="(\\/\\/.*\\.(?:png|jpg))", message="Avatar must be an url image")
    private String avatar;
}
