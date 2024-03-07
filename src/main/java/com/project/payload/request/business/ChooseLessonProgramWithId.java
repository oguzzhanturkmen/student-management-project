package com.project.payload.request.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChooseLessonProgramWithId {
    @NotNull(message = "lessonProgramId cannot be null")
    @Size(min = 1, message = "lessonProgramId cannot be empty")
    private Set<Long> lessonProgramId;

}
