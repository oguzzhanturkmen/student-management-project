package com.project.service.business;

import com.project.entity.concretes.business.Meet;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoudeException;
import com.project.messages.ErrorMessages;
import com.project.messages.SuccessMessages;
import com.project.payload.mappers.MeetMapper;
import com.project.payload.request.business.MeetRequest;
import com.project.payload.response.business.MeetResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.MeetRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.user.UserService;
import com.project.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
    private final MethodHelper methodHelper;
    private final DateTimeValidator dateTimeValidator;
    private final UserService userService;
    private final MeetMapper meetMapper;
    private final PageableHelper pageableHelper;

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
        } else meets = meetRepository.findByStudents_IdEquals(userId);


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

    public List<MeetResponse> getAll() {
        return meetRepository.findAll()
                .stream()
                .map(meetMapper::mapMeetToMeetResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<MeetResponse> getMeetById(Long meetId) {

        return ResponseMessage.<MeetResponse>builder()
                .message(SuccessMessages.MEET_FOUND)
                .httpStatus(HttpStatus.OK)
                .object(meetMapper.mapMeetToMeetResponse(isMeetExistById(meetId)))
                .build();
    }
    private Meet isMeetExistById(Long meetId){
        return meetRepository
                .findById(meetId).orElseThrow(
                        ()->new ResourceNotFoudeException(String.format(ErrorMessages.MEET_NOT_FOUND_MESSAGE,meetId)));
    }

    public ResponseMessage delete(Long meetId, HttpServletRequest httpServletRequest) {
        Meet meet = isMeetExistById(meetId);
        isTeacherControl(meet, httpServletRequest);
        meetRepository.deleteById(meetId);
        return ResponseMessage.builder()
                .message(SuccessMessages.MEET_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }
    private void isTeacherControl(Meet meet, HttpServletRequest httpServletRequest){
        String userName = (String) httpServletRequest.getAttribute("username");
        User teacher = methodHelper.isUserExist(userName);
        if(
                (teacher.getUserRole().getRoleType().equals(RoleType.TEACHER)) &&
                        !(meet.getAdvisoryTeacher().getId().equals(teacher.getId()))
        )
        {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }

    public Page<MeetResponse> getAllMeetByPage(int page, int size) {
        Pageable pageable = pageableHelper.createPageableWithProperties(page, size);
        return meetRepository.findAll(pageable).map(meetMapper::mapMeetToMeetResponse);
    }

    public ResponseMessage<MeetResponse> updateMeetById(MeetRequest meetRequest, Long meetId,
                                                        HttpServletRequest httpServletRequest) {

        Meet meet = isMeetExistById(meetId);
        isTeacherControl(meet, httpServletRequest);
        dateTimeValidator.checkTimeWithException(meetRequest.getStartTime(), meetRequest.getStopTime());

        if( !(
                meet.getDate().equals(meetRequest.getDate()) &&
                        meet.getStartTime().equals(meetRequest.getStartTime()) &&
                        meet.getEndTime().equals(meetRequest.getStopTime())
        )
        ){

            for(Long studentId :meetRequest.getStudentIds()) {
                checkMeetConflict(studentId, meetRequest.getDate(), meetRequest.getStartTime(),
                        meetRequest.getStopTime());
            }

            checkMeetConflict(meet.getAdvisoryTeacher().getId(),  meetRequest.getDate(), meetRequest.getStartTime(),
                    meetRequest.getStopTime() );
        }

        List<User> students = userService.getStudentById(meetRequest.getStudentIds());

        Meet updatedMeet = meetMapper.mapMeetUpdateRequestToMeet(meetRequest, meetId);
        updatedMeet.setStudents(students);
        updatedMeet.setAdvisoryTeacher(meet.getAdvisoryTeacher());

        Meet savedMeet =  meetRepository.save(updatedMeet);

        return ResponseMessage.<MeetResponse>builder()
                .message(SuccessMessages.MEET_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(meetMapper.mapMeetToMeetResponse(savedMeet))
                .build();

    }

    public List<MeetResponse> getAllMeetByTeacher(HttpServletRequest httpServletRequest) {
        String userName = (String) httpServletRequest.getAttribute("username");
        User advisoryTeacher = userService.getTeacherByUsername(userName);
        methodHelper.checkIsAdvisorTeacher(advisoryTeacher);

        return meetRepository.getByAdvisoryTeacher_IdEquals(advisoryTeacher.getId())
                .stream()
                .map(meetMapper::mapMeetToMeetResponse)
                .collect(Collectors.toList());
    }

    public List<MeetResponse> getAllMeetByStudent(HttpServletRequest httpServletRequest) {
        String userName = (String) httpServletRequest.getAttribute("username");
        User student =  methodHelper.isUserExist(userName);

        methodHelper.checkRole(student, RoleType.STUDENT);

        return meetRepository.findByStudents_IdEquals(student.getId())
                .stream()
                .map(meetMapper::mapMeetToMeetResponse)
                .collect(Collectors.toList());
    }
}
