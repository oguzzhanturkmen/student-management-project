package com.project.service.business;

import com.project.entity.concretes.business.EducationTerm;
import com.project.entity.concretes.business.Lesson;
import com.project.entity.concretes.business.LessonProgram;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.ResourceNotFoudeException;
import com.project.messages.ErrorMessages;
import com.project.messages.SuccessMessages;
import com.project.payload.mappers.LessonProgramMapper;
import com.project.payload.request.business.LessonProgramRequest;
import com.project.payload.response.business.LessonProgramResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.LessonProgramRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonProgramService {

    private final LessonProgramRepository lessonProgramRepository;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;
    private final DateTimeValidator dateTimeValidator;
    private final LessonProgramMapper lessonProgramMapper;
    private final PageableHelper pageableHelper;
    private final MethodHelper methodHelper;
    public ResponseMessage<LessonProgramResponse> saveLessonProgram(LessonProgramRequest lessonProgramRequest) {
        Set<Lesson> lessons = lessonService.getAllLessonByLessonId(lessonProgramRequest.getLessonIdList());
        EducationTerm educationTerm = educationTermService.findEducationTermById(lessonProgramRequest.getEducationTermId());

        if (lessons.isEmpty()) {
            throw new ResourceNotFoudeException(ErrorMessages.NOT_FOUND_LESSOIN_IN_LIST);
        }
        dateTimeValidator.checkTimeWithException(lessonProgramRequest.getStartTime(), lessonProgramRequest.getStopTime());

        LessonProgram lessonProgram = lessonProgramMapper.mapLessonProgramRequestToLessonProgram(lessonProgramRequest, lessons, educationTerm);

        LessonProgram savedLessonProgram = lessonProgramRepository.save(lessonProgram);

        return ResponseMessage.<LessonProgramResponse>builder()
                .message(SuccessMessages.LESSON_PROGRAM_SAVED)
                .object(lessonProgramMapper.mapLessonProgramToLessonProgramResponse(savedLessonProgram))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public List<LessonProgramResponse> getAllLessonProgramByList() {
            return lessonProgramRepository.findAll()
                    .stream()
                    .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                    .collect(Collectors.toList());
    }

    public LessonProgramResponse getLessonProgramById(Long id) {
        return lessonProgramMapper.mapLessonProgramToLessonProgramResponse(isLessonProgramExistById(id));
    }

    private LessonProgram isLessonProgramExistById(Long id) {
        return lessonProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoudeException(String.format(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_BY_ID, id)));
    }

    public List<LessonProgramResponse> getAllUnassigned() {
        return lessonProgramRepository.findByUsers_IdNull()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }

    public List<LessonProgramResponse> getAllAssigned() {
        return lessonProgramRepository.findByUsers_IdNotNull()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage deleteLessonProgram(Long id) {
        isLessonProgramExistById(id);
        lessonProgramRepository.deleteById(id);
        return ResponseMessage.builder()
                .message(SuccessMessages.LESSON_PROGRAM_DELETED)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public Page<LessonProgramResponse> getAllLessonProgramByPage(int page, int size, String sort, String direction) {
        Pageable pageable = pageableHelper.createPageableWithProperties(page, size, sort, direction);
        return lessonProgramRepository.findAll(pageable)
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse);
    }

    public Set<LessonProgramResponse> getLessonProgramByUser(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return lessonProgramRepository.getLessonProgramByUsersUsername(username)
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toSet());
    }

    public Set<LessonProgramResponse> getByTeacherId(Long id) {
        User teacher = methodHelper.isUserExist(id);
        methodHelper.checkRole(teacher, RoleType.TEACHER);

        return lessonProgramRepository.findByUsers_IdEquals(id)
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toSet());

    }

    public Set<LessonProgramResponse> getByStudentId(Long id) {
        return null;
    }
}
