package com.project.payload.request.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.entity.enums.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LessonProgramRequest {

    @NotNull(message = "Day cannot be null")
    private Day day;

    @NotNull(message = "Start time cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm" , timezone = "US")
    private LocalTime startTime;

    @NotNull(message = "Stop time cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm" , timezone = "US")
    private LocalTime stopTime;

    @NotNull(message = "Please select lesson")
    @Size(min = 1, message = "Please select at least one lesson")
    private Set<Long> lessonIdList;

    @NotNull(message = "Education term cannot be null")
    private Long educationTermId;



}
