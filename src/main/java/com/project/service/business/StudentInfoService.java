package com.project.service.business;

import com.project.entity.concretes.business.EducationTerm;
import com.project.entity.concretes.business.Lesson;
import com.project.entity.concretes.business.StudentInfo;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.Note;
import com.project.entity.enums.RoleType;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoudeException;
import com.project.messages.ErrorMessages;
import com.project.messages.SuccessMessages;
import com.project.payload.mappers.StudentInfoMapper;
import com.project.payload.request.business.StudentInfoRequest;
import com.project.payload.request.business.UpdateStudentInfoRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.StudentInfoRepository;
import com.project.payload.response.business.StudentInfoResponse;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentInfoService {

    private final StudentInfoRepository studentInfoRepository;
    private final MethodHelper methodHelper;
    private final UserService userService;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;
    private final StudentInfoMapper studentInfoMapper;
    private final PageableHelper pageableHelper;

    @Value("${midterm.exam.impact.percentage}")
    private Double midtermExamPercentage ;

    @Value("${final.exam.impact.percentage}")
    private Double finalExamPercentage ;

    public ResponseMessage<StudentInfoResponse> saveStudentInfo(HttpServletRequest request, StudentInfoRequest studentInfoRequest) {
        String teacherUsername = (String) request.getAttribute("username");
        User student = methodHelper.isUserExist(studentInfoRequest.getStudentId());
        methodHelper.checkRole(student, RoleType.STUDENT);

        User teacher = userService.getTeacherByUsername(teacherUsername);

        Lesson lesson = lessonService.isExistLessonById(studentInfoRequest.getLessonId());

        EducationTerm educationTerm = educationTermService.findEducationTermById(studentInfoRequest.getEducationTermId());

        checkSameLesson(student.getId(), lesson.getLessonName());

        Double average = calculateExamAverage(studentInfoRequest.getMidtermExam(), studentInfoRequest.getFinalExam());

        Note note = checkLetterGrade(average);

        StudentInfo studentInfo = studentInfoMapper.mapStudentInfoRequestToStudentInfo(studentInfoRequest, note, average);
        studentInfo.setStudent(student);
        studentInfo.setTeacher(teacher);
        studentInfo.setLesson(lesson);
        studentInfo.setEducationTerm(educationTerm);

        studentInfoRepository.save(studentInfo);

        return ResponseMessage.<StudentInfoResponse>builder()
                .object(studentInfoMapper.mapStudentInfoToStudentInfoResponse(studentInfo))
                .message(SuccessMessages.STUDENT_INFO_SAVED)
                .build();
    }
    private void checkSameLesson(Long studentId, String lessonName){
        boolean isLessonDuplicateExist = studentInfoRepository.getAllByStudentId_Id(studentId).stream()
                .anyMatch(studentInfo -> studentInfo.getLesson().getLessonName().equalsIgnoreCase(lessonName));
        if(isLessonDuplicateExist){
            throw new ConflictException(String.format(ErrorMessages.LESSON_ALREADY_EXISTS, lessonName));
        }
    }
    private Double calculateExamAverage(Double midtermExam, Double finalExam){

        return (midtermExam * midtermExamPercentage ) + ( finalExam * finalExamPercentage );
    }

    private Note checkLetterGrade(Double average){
        if(average<50.0) {
            return Note.FF;
        } else if (average<60) {
            return Note.DD;
        } else if (average<65) {
            return Note.CC;
        } else if (average<70) {
            return  Note.CB;
        } else if (average<75) {
            return  Note.BB;
        } else if (average<80) {
            return Note.BA;
        } else {
            return Note.AA;
        }
    }

    public ResponseMessage deleteStudentInfo(Long studentInfoId) {
        StudentInfo studentInfo = isStudentInfoExistById(studentInfoId);
        //studentInfoRepository.delete(studentInfo);
        studentInfoRepository.deleteById(studentInfoId);

        return ResponseMessage.builder()
                .message(SuccessMessages.STUDENT_INFO_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }
    public StudentInfo isStudentInfoExistById(Long id){
        boolean isExist = studentInfoRepository.existsByIdEquals(id);

        if(!isExist){
            throw new ResourceNotFoudeException(String.format(ErrorMessages.STUDENT_INFO_NOT_FOUND));
        } else {
            return  studentInfoRepository.findById(id).get();
        }
    }

    public ResponseMessage<StudentInfoResponse> update(UpdateStudentInfoRequest studentInfoRequest, Long studentInfoId) {

        Lesson lesson = lessonService.isExistLessonById(studentInfoRequest.getLessonId());
        StudentInfo studentInfo = isStudentInfoExistById(studentInfoId);
        EducationTerm educationTerm =educationTermService.findEducationTermById(studentInfoRequest.getEducationTermId());
        Double noteAverage = calculateExamAverage(studentInfoRequest.getMidtermExam(), studentInfoRequest.getFinalExam());
        Note note = checkLetterGrade(noteAverage);

        StudentInfo studentInfoUpdate =
                studentInfoMapper.mapStudentInfoUpdateToStudentInfo(studentInfoRequest,studentInfoId,lesson,
                        educationTerm,note,noteAverage);
        studentInfoUpdate.setTeacher(studentInfo.getTeacher());
        studentInfoUpdate.setStudent(studentInfo.getStudent());

        StudentInfo updatedStudentInfo =  studentInfoRepository.save(studentInfoUpdate);

        return ResponseMessage.<StudentInfoResponse>builder()
                .message(SuccessMessages.STUDENT_INFO_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(studentInfoMapper.mapStudentInfoToStudentInfoResponse(updatedStudentInfo))
                .build();
    }
    public List<StudentInfoResponse> getStudentInfoByStudentId(Long studentId) {
        User student = methodHelper.isUserExist(studentId);

        methodHelper.checkRole(student,RoleType.STUDENT);


        if(!studentInfoRepository.existsByStudent_IdEquals(studentId)){
            throw new ResourceNotFoudeException(
                    String.format(ErrorMessages.STUDENT_INFO_NOT_FOUND_BY_STUDENT_ID,studentId));
        }

        return studentInfoRepository.findByStudent_IdEquals(studentId)
                .stream()
                .map(studentInfoMapper::mapStudentInfoToStudentInfoResponse)
                .collect(Collectors.toList());
    }

    public Page<StudentInfoResponse> getAllStudentInfoByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.createPageableWithProperties(page, size, sort, type);
        return studentInfoRepository.findAll(pageable)
                .map(studentInfoMapper::mapStudentInfoToStudentInfoResponse);
    }

    public StudentInfoResponse findStudentInfoById(Long studentInfoId) {
        return studentInfoMapper.mapStudentInfoToStudentInfoResponse(isStudentInfoExistById(studentInfoId));
    }

    public Page<StudentInfoResponse> getAllForTeacher(HttpServletRequest request, int page, int size) {
        Pageable pageable = pageableHelper.createPageableWithProperties(page,size);
        String userName = (String) request.getAttribute("username");

        return studentInfoRepository.findByTeacherId_UsernameEquals(userName,pageable)
                .map(studentInfoMapper::mapStudentInfoToStudentInfoResponse);
    }

    public Page<StudentInfoResponse> getAllForStudent(HttpServletRequest request, int page, int size) {
        Pageable pageable = pageableHelper.createPageableWithProperties(page,size);
        String userName = (String) request.getAttribute("username");

        return studentInfoRepository.findByStudentId_UsernameEquals(userName,pageable)
                .map(studentInfoMapper::mapStudentInfoToStudentInfoResponse);
    }
}
