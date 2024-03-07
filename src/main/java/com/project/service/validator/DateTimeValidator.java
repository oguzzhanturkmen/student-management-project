package com.project.service.validator;

import com.project.entity.concretes.business.LessonProgram;
import com.project.exception.BadRequestException;
import com.project.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DateTimeValidator {

    private boolean checkTime(LocalTime start, LocalTime stop) {
        return start.isAfter(stop) || start.equals(stop);
    }
    public void checkTimeWithException(LocalTime start, LocalTime stop) {
        if (checkTime(start, stop)) {
            throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
        }
    }

    private void checkDuplicateLessonPrograms(Set<LessonProgram> lessonPrograms){
        Set<String> uniqueLessonProgramDays = new HashSet<>();
        Set<LocalTime> existingLessonProgramStartTimes = new HashSet<>();
        Set<LocalTime> existingLessonProgramStopTimes = new HashSet<>();

        for(LessonProgram lessonProgram : lessonPrograms) {
            String lessonProgramDay = lessonProgram.getDay().name();


            if(uniqueLessonProgramDays.contains(lessonProgramDay)){

                for(LocalTime startTime: existingLessonProgramStartTimes){

                    if(lessonProgram.getStartTime().equals(startTime)){
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }


                    if(lessonProgram.getStartTime().isBefore(startTime) && lessonProgram.getEndTime().isAfter(startTime)){
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }

                }

                for(LocalTime stopTime : existingLessonProgramStopTimes){
                    if(lessonProgram.getStartTime().isBefore(stopTime) && lessonProgram.getEndTime().isAfter(stopTime)) {
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                }
            }
            uniqueLessonProgramDays.add(lessonProgramDay);
            existingLessonProgramStartTimes.add(lessonProgram.getStartTime());
            existingLessonProgramStopTimes.add(lessonProgram.getEndTime());
        }
    }
    private void checkDuplicateLessonPrograms(Set<LessonProgram> existLessonProgram,
                                              Set<LessonProgram> lessonProgramRequest) {
        for(LessonProgram requestLessonProgram : lessonProgramRequest ){

            String requestLessonProgramDay = requestLessonProgram.getDay().name();
            LocalTime requestStart = requestLessonProgram.getStartTime();
            LocalTime requestStop = requestLessonProgram.getEndTime();

            if(existLessonProgram.stream()
                    .anyMatch(lessonProgram ->
                            lessonProgram.getDay().name().equals(requestLessonProgramDay)
                                    && (lessonProgram.getStartTime().equals(requestStart)
                                    || (lessonProgram.getStartTime().isBefore(requestStart) && lessonProgram.getEndTime().isAfter(requestStart))
                                    || (lessonProgram.getStartTime().isBefore(requestStop) && lessonProgram.getEndTime().isAfter(requestStop))
                                    || (lessonProgram.getStartTime().isAfter(requestStart) && lessonProgram.getEndTime().isBefore(requestStop))))

            ) {
                throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
            }
        }
    }

    public void checkLessonPrograms(Set<LessonProgram> existLessonProgram,
                                    Set<LessonProgram> lessonProgramRequest) {
        if(existLessonProgram.isEmpty() && lessonProgramRequest.size()>1){
            checkDuplicateLessonPrograms(lessonProgramRequest);
        } else {
            checkDuplicateLessonPrograms(lessonProgramRequest);
            checkDuplicateLessonPrograms(existLessonProgram, lessonProgramRequest);
        }
    }

}
