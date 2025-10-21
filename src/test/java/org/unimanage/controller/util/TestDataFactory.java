package org.unimanage.controller.util;


import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.util.dto.PersonRegisterDto;


public class TestDataFactory {

    public  PersonRegisterDto studentSuccessfullyData() {
        return PersonRegisterDto.builder()
                .firstName("student_name")
                .lastName("student_family_name")
                .email("example@gmail.com")
                .nationalCode("1234567899")
                .phoneNumber("09000000000")
                .majorName("علوم کامپیوتر")
                .build();
    }

    public  PersonRegisterDto teacherSuccessfullyData() {
        return PersonRegisterDto.builder()
                .firstName("teacher_name")
                .lastName("teacher_family_name")
                .email("example@gmail.com")
                .nationalCode("1111111111")
                .phoneNumber("09000000000")
                .majorName("علوم کامپیوتر")
                .build();
    }
    public  PersonRegisterDto managerSuccessfullyData() {
        return PersonRegisterDto.builder()
                .firstName("manager_name")
                .lastName("manager_family_name")
                .email("example@gmail.com")
                .nationalCode("1234567899")
                .phoneNumber("09000000000")
                .majorName("علوم کامپیوتر")
                .build();
    }




    public  PersonRegisterDto studentNotValidData() {
        return PersonRegisterDto.builder()
                .firstName("student_name")
                .lastName("student_family_name")
                .email("student_email")
                .nationalCode("1230")
                .phoneNumber("090000")
                .majorName("علوم کامپیوتر")
                .build();
    }

    public  Person createValidPerson() {
        return Person.builder()
                .firstName("student_name")
                .lastName("student_family_name")
                .email("student_email")
                .nationalCode("123456790")
                .phoneNumber("09000000000")
                .build();
    }

}
