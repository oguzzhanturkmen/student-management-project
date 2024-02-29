package com.project.service.business;

import com.project.entity.concretes.business.EducationTerm;
import com.project.exception.BadRequestException;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoudeException;
import com.project.messages.ErrorMessages;
import com.project.messages.SuccessMessages;
import com.project.payload.mappers.EducationTermMapper;
import com.project.payload.request.business.EducationTermRequest;
import com.project.payload.response.business.EducationTermResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.EducationTermRepository;
import com.project.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationTermService {

    private final EducationTermRepository educationTermRepository;
    private final EducationTermMapper educationTermMapper;
    private final PageableHelper pageableHelper;

    public ResponseMessage<EducationTermResponse> saveEducationTerm(EducationTermRequest educationTermRequest) {

        validateEducationTermDates(educationTermRequest);

        EducationTerm savedEducationTerm =
                educationTermRepository.save(educationTermMapper.mapEducationTermRequestToEducationTerm(educationTermRequest));

        return ResponseMessage.<EducationTermResponse>builder()
                .message(SuccessMessages.EDUCATION_TERM_SAVED)
                .object(educationTermMapper.mapEducationTermToEducationTermResponse(savedEducationTerm))
                .httpStatus(HttpStatus.CREATED)
                .build();

    }

    private void validateEducationTermDatesForRequest(EducationTermRequest educationTermRequest){
        // !!! bu metodda amacimiz requestten gelen registrationDate,StartDate ve endDate arasindaki
        // tarih sirasina gore dogru mu setlenmis onu kontrol etmek

        // registration > start
        if(educationTermRequest.getLastRegistrationDate().isAfter(educationTermRequest.getStartDate())){
            throw new BadRequestException(ErrorMessages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
        }
        // end > start
        if(educationTermRequest.getEndDate().isBefore(educationTermRequest.getStartDate())) {
            throw new BadRequestException(ErrorMessages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
        }
    }

    private void validateEducationTermDates(EducationTermRequest educationTermRequest){

        validateEducationTermDatesForRequest(educationTermRequest);


        if(educationTermRepository.existsByTermAndYear(educationTermRequest.getTerm(),
                educationTermRequest.getStartDate().getYear())){
            throw new ConflictException(ErrorMessages.EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE);
        }

        if(educationTermRepository.findByYear(educationTermRequest.getStartDate().getYear())
                .stream()
                .anyMatch(educationTerm ->
                        (         educationTerm.getStartDate().equals(educationTermRequest.getStartDate())
                                ||  (educationTerm.getStartDate().isBefore(educationTermRequest.getStartDate())
                                && educationTerm.getEndDate().isAfter(educationTermRequest.getStartDate()))
                                ||  (educationTerm.getStartDate().isBefore(educationTermRequest.getEndDate())
                                && educationTerm.getEndDate().isAfter(educationTermRequest.getEndDate()))
                                ||  (educationTerm.getStartDate().isAfter(educationTermRequest.getStartDate())
                                && educationTerm.getEndDate().isBefore(educationTermRequest.getEndDate()))

                        ))
        ){
            throw new BadRequestException(ErrorMessages.EDUCATION_TERM_CONFLICT_MESSAGE);
        }
    }

    public EducationTermResponse getEducationTermById(Long id) {

        return educationTermMapper.mapEducationTermToEducationTermResponse(isEducationTermExist(id));

    }

    private EducationTerm isEducationTermExist(Long id) {
        return educationTermRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoudeException(String.format(ErrorMessages.NOT_FOUND_EDUCATION_TERM_MESSAGE, id)));
    }

    public List<EducationTermResponse> getAllEducationTerms() {
        return educationTermRepository.findAll()
                .stream()
                .map(educationTermMapper::mapEducationTermToEducationTermResponse)
                .collect(Collectors.toList());
    }

    public Page<EducationTermResponse> getAllEducationTermsByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.createPageableWithProperties(page, size, sort, type);
        return educationTermRepository.findAll(pageable)
                .map(educationTermMapper::mapEducationTermToEducationTermResponse);

    }

    public ResponseMessage deleteEducationTermByID(Long id) {
        educationTermRepository.delete(isEducationTermExist(id));
        return ResponseMessage.builder()
                .message(SuccessMessages.EDUCATION_TERM_DELETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<EducationTermResponse> updateEducationTermById(EducationTermRequest educationTermRequest, Long id) {
        isEducationTermExist(id);
        validateEducationTermDates(educationTermRequest);

        EducationTerm updatedEducationTerm = educationTermRepository.save(educationTermMapper.mapEducationTermRequestToUpdatedEducationTerm(id, educationTermRequest));

        return ResponseMessage.<EducationTermResponse>builder()
                .message(SuccessMessages.EDUCATION_TERM_UPDATED)
                .object(educationTermMapper.mapEducationTermToEducationTermResponse(updatedEducationTerm))
                .httpStatus(HttpStatus.OK)
                .build();
    }
}