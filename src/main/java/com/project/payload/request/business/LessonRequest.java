package com.project.payload.request.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LessonRequest {
        @NotNull(message = "Lesson name is mandatory")
        @Size(min = 2, max = 16, message = "Lesson name must be between 2 and 16 characters")
        @Pattern (regexp = "\\A(?!\\s*\\Z).+" ,message="Lesson name must consist of the characters .")
        private String lessonName;

        @NotNull(message = "Credit score is mandatory")
        private int creditScore;
        @NotNull(message = "Compulsory is mandatory")
        private boolean isCompulsory;

}
