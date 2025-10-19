package org.unimanage.controller.util;


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
