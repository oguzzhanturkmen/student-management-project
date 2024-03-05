package com.project.controller.business;

import com.project.payload.request.business.LessonProgramRequest;
import com.project.payload.response.business.LessonProgramResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lessonPrograms")
@RequiredArgsConstructor
public class LessonProgramController {

    private final LessonProgramService lessonProgramService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER' , 'ASSISTANT_MANAGER')")
    public ResponseMessage<LessonProgramResponse> saveLessonProgram(@RequestBody @Valid LessonProgramRequest lessonProgramRequest){
        return lessonProgramService.saveLessonProgram(lessonProgramRequest);
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER' , 'ASSISTANT_MANAGER' , 'TEACHER', 'STUDENT')")
    public List<LessonProgramResponse> getAllLessonProgramByList(){
        return lessonProgramService.getAllLessonProgramByList();
    }
    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER' , 'ASSISTANT_MANAGER')")
    public LessonProgramResponse getLessonProgramById(@PathVariable Long id){
        return lessonProgramService.getLessonProgramById(id);
    }
    @GetMapping("/getAllUnassigned")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER' , 'ASSISTANT_MANAGER' , 'TEACHER', 'STUDENT')")
    public List<LessonProgramResponse> getAllUnassigned(){
        return lessonProgramService.getAllUnassigned();
    }
    @GetMapping("/getAllAssigned")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER' , 'ASSISTANT_MANAGER' , 'TEACHER', 'STUDENT')")
    public List<LessonProgramResponse> getAllAssigned() {
           return lessonProgramService.getAllAssigned();
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER' , 'ASSISTANT_MANAGER')")
    public ResponseMessage deleteLessonProgram(@PathVariable Long id){
         return lessonProgramService.deleteLessonProgram(id);
    }

    @GetMapping("/getAllLessonProgramByPage")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER' , 'ASSISTANT_MANAGER' , 'TEACHER', 'STUDENT')")
    public Page<LessonProgramResponse> getAllLessonProgramByPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "25") int size,
                                                                 @RequestParam(value = "sort", defaultValue = "id") String sort,
                                                                 @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        return lessonProgramService.getAllLessonProgramByPage(page, size, sort, direction);
    }
    @GetMapping("/getLessonProgramByTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public Set<LessonProgramResponse> getLessonProgramByTeacherUsername(HttpServletRequest request){
        return lessonProgramService.getLessonProgramByUser(request);
    }
    @GetMapping("/getLessonProgramByStudent")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public Set<LessonProgramResponse> geAllLessonProgramByStudent(HttpServletRequest request){
        return lessonProgramService.getLessonProgramByUser(request);
    }

    @GetMapping("/getLessonProgramByTeacherId/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER' , 'ASSISTANT_MANAGER')")
    public Set<LessonProgramResponse> getByTeacherId(@PathVariable Long id){
        return lessonProgramService.getByTeacherId(id);
    }
    @GetMapping("/getLessonProgramByStudentId/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER' , 'ASSISTANT_MANAGER')")
    public Set<LessonProgramResponse> getLessonProgramByTeacherId(@PathVariable Long id){
        return lessonProgramService.getByStudentId(id);
    }






}
