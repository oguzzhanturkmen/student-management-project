package com.project;

import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.Gender;
import com.project.entity.enums.RoleType;
import com.project.payload.request.user.UserRequest;
import com.project.repository.user.UserRoleRepository;
import com.project.service.user.UserRoleService;
import com.project.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication

public class StudentManagementApplication implements CommandLineRunner {

    private final  UserRoleService userRoleService;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;

    public StudentManagementApplication(UserRoleService userRoleService , UserRoleRepository userRoleRepository, UserService userService) {
        this.userRoleService = userRoleService;
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRoleService.getAllUserRoles().isEmpty()){
            UserRole admin = new UserRole();
            admin.setRoleType(RoleType.ADMIN);
            admin.setRoleName("Admin");
            userRoleRepository.save(admin);

            UserRole manager = new UserRole();
            manager.setRoleType(RoleType.MANAGER);
            manager.setRoleName("Dean");
            userRoleRepository.save(manager);

            UserRole assistantManager = new UserRole();
            assistantManager.setRoleType(RoleType.ASSISTANT_MANAGER);
            assistantManager.setRoleName("ViceDean");
            userRoleRepository.save(assistantManager);

            UserRole student = new UserRole();
            student.setRoleType(RoleType.STUDENT);
            student.setRoleName("Student");
            userRoleRepository.save(student);

            UserRole teacher = new UserRole();
            teacher.setRoleType(RoleType.TEACHER);
            teacher.setRoleName("Teacher");
            userRoleRepository.save(teacher);

        }

        if (userService.countAdmins() == 0){
            UserRequest userRequest = new UserRequest();
            userRequest.setUsername("Admin");
            userRequest.setEmail("admin@admin.com");
            userRequest.setSsn("111-11-1111");
            userRequest.setPassword("12345678");
            userRequest.setName("Adminx");
            userRequest.setSurname("AdminY");
            userRequest.setPhoneNumber("111-111-1111");
            userRequest.setGender(Gender.FEMALE);
            userRequest.setBirthDate(LocalDate.of(1990, 1, 1));
            userRequest.setBirthPlace("Texas");

            userService.saveUser("Admin", userRequest);
        }

    }
}
