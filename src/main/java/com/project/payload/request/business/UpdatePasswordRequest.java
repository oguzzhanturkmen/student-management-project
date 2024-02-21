package com.project.payload.request.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {
    @NotBlank(message = "New password cannot be blank")
    private String oldPassword;

    @NotBlank(message = "New password cannot be blank")
    private String newPassword;



}
