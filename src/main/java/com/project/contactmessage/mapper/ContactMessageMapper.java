package com.project.contactmessage.mapper;

import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.entity.ContactMessage;
import org.springframework.stereotype.Component;
import com.project.contactmessage.dto.ContactMessageRequest;

import java.time.LocalDateTime;

@Component
public class ContactMessageMapper {

    public ContactMessage mapToEntity(ContactMessageRequest contactMessageRequest) {
        return ContactMessage.builder()
                .name(contactMessageRequest.getName())
                .email(contactMessageRequest.getEmail())
                .message(contactMessageRequest.getMessage())
                .subject(contactMessageRequest.getSubject())
                .dateTime(LocalDateTime.now())
                .build();
    }

    public ContactMessageResponse mapToResponse(ContactMessage contactMessage) {
        return ContactMessageResponse.builder()
                .name(contactMessage.getName())
                .email(contactMessage.getEmail())
                .message(contactMessage.getMessage())
                .subject(contactMessage.getSubject())
                .dateTime(contactMessage.getDateTime())
                .build();
    }
}
