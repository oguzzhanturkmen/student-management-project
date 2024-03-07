package com.project.service.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import org.springframework.data.domain.Pageable;
import java.util.Objects;

@Component
public class PageableHelper {

    public Pageable createPageableWithProperties(int page, int size, String sortBy, String order) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).ascending());
        if(Objects.equals(order, "desc")){
            pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
        }
        return pageable;
    }
    public Pageable createPageableWithProperties(int page, int size) {
        return PageRequest.of(page,size, Sort.by("id").descending());
    }


}
