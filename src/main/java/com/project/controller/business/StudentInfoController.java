package com.project.controller.business;

import com.project.payload.request.business.StudentInfoRequest;
import com.project.payload.request.business.UpdateStudentInfoRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.StudentInfoResponse;
import com.project.service.business.StudentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {

    private final StudentInfoService studentInfoService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseMessage<StudentInfoResponse> saveStudentInfo(HttpServletRequest request, @RequestBody @Valid StudentInfoRequest studentInfoRequest){
        return studentInfoService.saveStudentInfo(request, studentInfoRequest);

    }
    @DeleteMapping("/delete/{studentInfoId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    public ResponseMessage deleteStudentInfo(@PathVariable Long studentInfoId){
        return studentInfoService.deleteStudentInfo(studentInfoId);
    }

    @PutMapping("/update/{studentInfoId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    public ResponseMessage<StudentInfoResponse> update(@RequestBody @Valid UpdateStudentInfoRequest studentInfoRequest,
                                                       @PathVariable Long studentInfoId) {
        return studentInfoService.update(studentInfoRequest, studentInfoId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAllStudentInfoByPage")
    public Page<StudentInfoResponse> getAllStudentInfoByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ) {
        return  studentInfoService.getAllStudentInfoByPage(page,size,sort,type);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getByStudentId/{studentId}")
    public ResponseEntity<List<StudentInfoResponse>> getStudentInfoByStudentId(@PathVariable Long studentId){
        List<StudentInfoResponse>studentInfoResponse = studentInfoService.getStudentInfoByStudentId(studentId);
        return ResponseEntity.ok(studentInfoResponse);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/get/{studentInfoId}")
    public ResponseEntity<StudentInfoResponse>getStudentInfoById(@PathVariable Long studentInfoId){
        return ResponseEntity.ok(studentInfoService.findStudentInfoById(studentInfoId));
    }
    @GetMapping("/getAllForTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<Page<StudentInfoResponse>> getAllForTeacher(HttpServletRequest request,
                                                                      @RequestParam(value = "page") int page,
                                                                      @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(studentInfoService.getAllForTeacher(request,page,size));
    }
    @GetMapping("/getAllForStudent")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<Page<StudentInfoResponse>> getAllForStudent(HttpServletRequest request,
                                                                      @RequestParam(value = "page") int page,
                                                                      @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(studentInfoService.getAllForStudent(request,page,size));
    }
}

