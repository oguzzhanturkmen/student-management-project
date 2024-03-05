package com.project.payload.mappers;

import com.project.entity.concretes.business.EducationTerm;
import com.project.entity.concretes.business.Lesson;
import com.project.entity.concretes.business.LessonProgram;
import com.project.payload.request.business.LessonProgramRequest;
import com.project.payload.response.business.LessonProgramResponse;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LessonProgramMapper {
    public LessonProgram mapLessonProgramRequestToLessonProgram(LessonProgramRequest lessonProgramRequest, Set<Lesson> lessons, EducationTerm educationTerm) {
        return LessonProgram.builder()
                .startTime(lessonProgramRequest.getStartTime())
                .endTime(lessonProgramRequest.getStopTime())
                .day(lessonProgramRequest.getDay())
                .lessons(lessons)
                .educationTerm(educationTerm)
                .build();

    }

    public LessonProgramResponse mapLessonProgramToLessonProgramResponse(LessonProgram lessonProgram) {
        return LessonProgramResponse.builder()
                .lessonProgramId(lessonProgram.getId())
                .day(lessonProgram.getDay())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getEndTime())
                .lessonName(lessonProgram.getLessons())
                .educationTerm(lessonProgram.getEducationTerm())
                .build();
    }
}
