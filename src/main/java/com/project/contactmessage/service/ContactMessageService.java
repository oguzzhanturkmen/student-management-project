package com.project.contactmessage.service;


import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.mapper.ContactMessageMapper;
import com.project.contactmessage.messages.Messages;
import com.project.contactmessage.repository.ContactMessageRepository;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoudeException;
import com.project.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;

    public ResponseMessage<ContactMessageResponse> saveContactMessage(ContactMessageRequest contactMessageRequest) {
        ContactMessage contactMessage = contactMessageMapper.mapToEntity(contactMessageRequest);
        ContactMessage savedContactMessage = contactMessageRepository.save(contactMessage);

        return ResponseMessage.<ContactMessageResponse>builder()
                .object(contactMessageMapper.mapToResponse(savedContactMessage))
                .message("Contact message saved successfully")
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public Page<ContactMessageResponse> getAllContactMessages(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(direction, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findAll(pageable)
                .map(contactMessageMapper::mapToResponse);

    }

    public Page<ContactMessageResponse> searchByEmail(String email, int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(direction, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return contactMessageRepository.findByEmail(email, pageable)
                .map(contactMessageMapper::mapToResponse);
    }

    public Page<ContactMessageResponse> searchBySubject(String subject, int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(direction, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findBySubject(subject, pageable)
                .map(contactMessageMapper::mapToResponse);
    }

    public String deleteById(Long contactMessageId) {
        getContactMessageById(contactMessageId);
        contactMessageRepository.deleteById(contactMessageId);
        return Messages.CONTACT_MESSAGE_DELETED;
    }

    public ContactMessage getContactMessageById(Long contactMessageId) {
        return contactMessageRepository.findById(contactMessageId).orElseThrow(() -> new ResourceNotFoudeException(Messages.CONTACT_MESSAGE_NOT_FOUND));
    }

    public List<ContactMessage> searchBetweenDates(String startDate, String endDate) {

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            return contactMessageRepository.findMessagesBetweenDates(start, end);
        } catch (DateTimeParseException e) {
            throw new ConflictException(Messages.UNVALID_DATE_FORMAT);
        }
    }

    public List<ContactMessage> searchBetweenTimes(String startHour, String startMinute, String endHour, String endMinute) {
        try {
            int startHourInt = Integer.parseInt(startHour);
            int startMinuteInt = Integer.parseInt(startMinute);
            int endHourInt = Integer.parseInt(endHour);
            int endMinuteInt = Integer.parseInt(endMinute);

            return contactMessageRepository.findMessagesBetweenTimes(startHourInt, startMinuteInt, endHourInt, endMinuteInt);
        } catch (NumberFormatException e) {
            throw new ConflictException(Messages.UNVALID_TIME_FORMAT);
        }
    }
}
