package com.project.payload.request.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {
    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String oldPassword;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String newPassword;



}
