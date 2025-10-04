package org.unimanage.service.imple;

import org.unimanage.domain.user.Person;
import org.unimanage.repository.PersonRepository;
import org.unimanage.service.PersonService;

public class PersonServiceImpl extends BaseServiceImpl<Person,Long> implements PersonService {

    private final PersonRepository personRepository;


    public PersonServiceImpl(PersonRepository personRepository) {
        super(personRepository);
        this.personRepository = personRepository;
    }






}
