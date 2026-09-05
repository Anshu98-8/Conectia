package com.CodingBrajmohan.connectionService.service;


import com.CodingBrajmohan.connectionService.entity.PersonEntity;
import com.CodingBrajmohan.connectionService.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public void createPerson(Long userId, String name) {
        PersonEntity person = PersonEntity.builder().name(name).userId(userId).build();
        personRepository.save(person);
    }

}
