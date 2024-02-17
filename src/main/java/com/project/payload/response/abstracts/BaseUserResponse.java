package com.project.payload.response.abstracts;

import com.project.entity.enums.Gender;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseUserResponse {

    private Long userId;
    private String username;
    private String name;
    private String surname;
    private String ssn;
    private String birthPlace;
    private String email;
    private String userRole;
    private String phoneNumber;
    private LocalDate birthDate;
    private Gender gender;

}
