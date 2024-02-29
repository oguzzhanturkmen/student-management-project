package com.project.service.business;

import com.project.entity.concretes.business.Lesson;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoudeException;
import com.project.messages.ErrorMessages;
import com.project.messages.SuccessMessages;
import com.project.payload.mappers.LessonnMapper;
import com.project.payload.request.business.LessonRequest;
import com.project.payload.response.business.LessonResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.LessonRepository;
import com.project.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonnMapper lessonMapper;
    private final PageableHelper pageableHelper;

    public ResponseMessage<LessonResponse> saveLesson(LessonRequest lessonRequest) {
        isLessonExistByLessonName(lessonRequest.getLessonName());
        Lesson savedLesson = lessonRepository.save(lessonMapper.mapLessonRequestToLesson(lessonRequest));
        return ResponseMessage.<LessonResponse>builder()
                .message(SuccessMessages.LESSON_SAVED)
                .object(lessonMapper.mapLessonToLessonResponse(savedLesson))
                .httpStatus(HttpStatus.CREATED)
                .build();

    }

    private boolean isLessonExistByLessonName(String lessonName) {

       boolean lessonExists = lessonRepository.existsLessonByLessonNameEqualsIgnoreCase(lessonName);

       if (lessonExists) {
           throw new ConflictException(String.format(ErrorMessages.LESSON_ALREADY_EXISTS, lessonName));
       }
       return false;
    }

    public Lesson isExistLessonById(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoudeException(ErrorMessages.LESSON_NOT_FOUND));
    }

    public ResponseMessage deleteLessonById(Long id) {
        lessonRepository.delete(isExistLessonById(id));
        return ResponseMessage.builder()
                .message(SuccessMessages.LESSON_DELETED)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<LessonResponse> getLessonByName(String lessonName) {
        if (lessonRepository.getLessonByLessonName(lessonName).isPresent()) {
            return ResponseMessage.<LessonResponse>builder()
                    .message(SuccessMessages.LESSON_FOUND)
                    .object(lessonMapper.mapLessonToLessonResponse(lessonRepository.getLessonByLessonName(lessonName).get()))
                    .httpStatus(HttpStatus.OK)
                    .build();
        }
        return ResponseMessage.<LessonResponse>builder()
                .message(ErrorMessages.LESSON_NOT_FOUND)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    public Page<LessonResponse> findLessonByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.createPageableWithProperties(page, size, sort, type);
        return lessonRepository.findAll(pageable).map(lessonMapper::mapLessonToLessonResponse);
    }

    public Set<Lesson> getAllLessonByLessonId(Set<Long> idSet) {
        return idSet.stream()
                .map(this::isExistLessonById)
                .collect(Collectors.toSet());
    }

    public LessonResponse updateLessonById(Long lessonId, LessonRequest lessonRequest) {
        Lesson lesson = isExistLessonById(lessonId);

        if ( (!lesson.getLessonName().equals(lessonRequest.getLessonName())) && lessonRepository.existsByLessonName(lessonRequest.getLessonName())) {
            throw new ConflictException(String.format(ErrorMessages.LESSON_ALREADY_EXISTS, lessonRequest.getLessonName()));
        }
        Lesson updatedLesson = lessonMapper.mapLessonRequestToUpdatedLesson(lessonId, lessonRequest);
        updatedLesson.setLessonPrograms(lesson.getLessonPrograms());

        Lesson savedLesson = lessonRepository.save(updatedLesson);
        return lessonMapper.mapLessonToLessonResponse(savedLesson);
    }
}
