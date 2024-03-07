package com.project.repository.business;

import com.project.entity.concretes.business.Meet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetRepository extends JpaRepository<Meet, Long> {
    List<Meet> getByAdvisoryTeacher_IdEquals(Long userId);

    List<Meet> findByStudentList_IdEquals(Long userId);
}
