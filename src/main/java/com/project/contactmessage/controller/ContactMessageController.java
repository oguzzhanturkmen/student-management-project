package com.project.contactmessage.controller;

import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.service.ContactMessageService;
import com.project.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contactMessages")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @PostMapping("/save")
    public ResponseMessage<ContactMessageResponse> saveContactMessage(@Valid @RequestBody ContactMessageRequest contactMessageRequest) {
        return contactMessageService.saveContactMessage(contactMessageRequest);

    }
    @GetMapping("/getAll")
    public Page<ContactMessageResponse> getAllContactMessages(@RequestParam(value = "page" ,defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                              @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
                                                              @RequestParam(value = "direction", defaultValue = "DESC") String direction) {
        return contactMessageService.getAllContactMessages(page, size, sort, direction);
    }

    @GetMapping("/searchByEmail")
    public Page<ContactMessageResponse> searchByEmail(@RequestParam(value = "email") String email,
                                                      @RequestParam(value = "page" ,defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                                      @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
                                                      @RequestParam(value = "direction", defaultValue = "DESC") String direction) {
        return contactMessageService.searchByEmail(email, page, size, sort, direction);
    }
    @GetMapping("/searchBySubject")
    public Page<ContactMessageResponse> searchBySubject(@RequestParam(value = "subject") String subject,
                                                       @RequestParam(value = "page" ,defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                                       @RequestParam(value = "sort", defaultValue = "dateTime") String sort,
                                                       @RequestParam(value = "direction", defaultValue = "DESC") String direction) {
        return contactMessageService.searchBySubject(subject, page, size, sort, direction);
    }

    @DeleteMapping("/deleteById/{contactMessageId}")
    public ResponseEntity<String > deleteByIdPath(@PathVariable Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<String > deleteByIdParam(@RequestParam(value = "contactMessageId") Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.deleteById(contactMessageId));
    }

    @GetMapping("/searchBetweenDates") //?startDate=2021-01-01&endDate=2021-12-31
    public ResponseEntity<List<ContactMessage>> searchBetweenDates(@RequestParam(value = "startDate") String startDate,
                                                                   @RequestParam(value = "endDate") String endDate) {
        return ResponseEntity.ok(contactMessageService.searchBetweenDates(startDate, endDate));

    }

    @GetMapping("/searchBetweenTimes") //?startHour=00&startMinute=00&endHour=23&endMinute=59
    public ResponseEntity<List<ContactMessage>> searchBetweenTimes(@RequestParam(value="startHour") String startHour,
                                                                   @RequestParam(value="startMinute") String startMinute,
                                                                   @RequestParam(value="endHour") String endHour,
                                                                   @RequestParam(value="endMinute") String endMinute) {
        return ResponseEntity.ok(contactMessageService.searchBetweenTimes(startHour, startMinute, endHour, endMinute));
    }
    @GetMapping("/getByIdParam")
    public ResponseEntity<ContactMessage> getById(@RequestParam(value = "contactMessageId") Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.getContactMessageById(contactMessageId));
    }
    @GetMapping("/getByIdPath/{contactMessageId}")
    public ResponseEntity<ContactMessage> getByIdPath(@PathVariable Long contactMessageId) {
        return ResponseEntity.ok(contactMessageService.getContactMessageById(contactMessageId));
    }





}

