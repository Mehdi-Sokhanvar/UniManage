package org.unimanage.service.imple;

import org.springframework.context.MessageSource;
import org.unimanage.domain.user.Person;
import org.unimanage.repository.PersonRepository;
import org.unimanage.service.PersonService;

public class PersonServiceImpl extends BaseServiceImpl<Person,Long> implements PersonService {

    private final PersonRepository personRepository;
    private final MessageSource messageSource;


    public PersonServiceImpl(PersonRepository personRepository, MessageSource messageSource) {
        super(personRepository,messageSource);
        this.personRepository = personRepository;
        this.messageSource = messageSource;
    }






}
