package com.project.contactmessage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContactMessageRequest {

    @NotNull(message = "Name is required")
    @Size(min = 4, max = 16, message = "Name must be between 4 and 16 characters")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Name must contain only letters")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(min = 4, max = 24, message = "Email must be between 4 and 24 characters")
    private String email;

    @NotNull(message = "Subject is required")
    @Size(min = 4, max = 50, message = "Name must be between 4 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Name must contain only letters")
    private String subject;

    @NotNull(message = "Message is required")
    @Size(min = 4, max = 100, message = "Name must be between 4 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Name must contain only letters")
    private String message;


}
