package com.example.demo.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateReq {
    @NotNull(message = "Last_Password is required")
    @NotEmpty(message = "Last_Password is required")
    private String last_password;

    @NotNull(message = "Last_Password is required")
    @NotEmpty(message = "Last_Password is required")
    @Size(min = 4, max = 20, message = "Password must be between 4 - 20 characters")
    private String new_password;

    @NotNull(message = "Confirm_Password is required")
    @NotEmpty(message = "Confirm_Password is required")
    private String confirm_password;
}
