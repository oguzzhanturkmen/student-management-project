package com.project.payload.response.business;

import com.project.entity.enums.Term;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EducationTermResponse {

    private Long id;

    private Term term;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate lastRegistrationDate;
}
