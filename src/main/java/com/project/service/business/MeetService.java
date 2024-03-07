package com.project.service.business;

import com.project.entity.concretes.business.Meet;
import com.project.entity.concretes.user.User;
import com.project.exception.ConflictException;
import com.project.messages.ErrorMessages;
import com.project.messages.SuccessMessages;
import com.project.payload.mappers.MeetMapper;
import com.project.payload.request.business.MeetRequest;
import com.project.payload.response.business.MeetResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.MeetRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.user.UserService;
import com.project.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
    private final MethodHelper methodHelper;
    private final DateTimeValidator dateTimeValidator;
    private final UserService userService;
    private final MeetMapper meetMapper;

    public ResponseMessage<MeetResponse> saveMeet(HttpServletRequest request, MeetRequest meetRequest) {
        String username = (String) request.getAttribute("username");
        User advisorTeacher =  methodHelper.isUserExist(username);
        methodHelper.checkIsAdvisorTeacher(advisorTeacher);


        dateTimeValidator.checkTimeWithException(meetRequest.getStartTime(), meetRequest.getStopTime());


        checkMeetConflict(advisorTeacher.getId(), meetRequest.getDate(),
                meetRequest.getStartTime(), meetRequest.getStopTime());


        List<User> students = userService.getStudentById(meetRequest.getStudentIds());

        Meet meet = meetMapper.mapMeetRequestToMeet(meetRequest);
        meet.setStudents(students);
        meet.setAdvisoryTeacher(advisorTeacher);

        Meet savedMeet =  meetRepository.save(meet);

        return ResponseMessage.<MeetResponse>builder()
                .message(SuccessMessages.MEET_SAVED)
                .object(meetMapper.mapMeetToMeetResponse(savedMeet))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }
    private void checkMeetConflict(Long userId, LocalDate date, LocalTime startTime, LocalTime stoptime) {

        List<Meet> meets ;


        if(Boolean.TRUE.equals(userService.getUserByUserId(userId).getIsAdvisor())){
            meets = meetRepository.getByAdvisoryTeacher_IdEquals(userId);
        } else meets = meetRepository.findByStudentList_IdEquals(userId);


        for(Meet meet: meets){
            LocalTime existingStartTime =  meet.getStartTime();
            LocalTime existingStopTime =  meet.getEndTime();

            if(meet.getDate().equals(date) &&
                    (
                            (startTime.isAfter(existingStartTime) && startTime.isBefore(existingStopTime)) ||
                                    (stoptime.isAfter(existingStartTime) && stoptime.isBefore(existingStopTime)) ||
                                    (startTime.isBefore(existingStartTime) && stoptime.isAfter(existingStopTime)) ||
                                    (startTime.equals(existingStartTime) || stoptime.equals(existingStopTime))

                    )

            ){
                throw new ConflictException(ErrorMessages.MEET_HOURS_CONFLICT);
            }
        }

    }
}
