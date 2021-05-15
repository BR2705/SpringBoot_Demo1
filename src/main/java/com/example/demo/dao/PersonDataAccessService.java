package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("PersonDataAccessService")
public class PersonDataAccessService implements  PersonDao{

    private static List<Person> persons = new ArrayList<>();

    @Override
    public int insertPerson(UUID id, Person person) {
        persons.add(new Person(id,person.getName()));
        return 0;
    }

    @Override
    public List<Person> selectAllPerson() {
        return persons;
    }

    @Override
    public int deletePerson(UUID id) {
        Optional<Person> personMayBe = selectPersonById(id);
        if(personMayBe.isEmpty()) {
            return 0;
        }
        persons.remove(personMayBe.get());
        return 1;
    }

    @Override
    public int updatePerson(UUID id, Person updatePerson) {
        return selectPersonById(id)
                .map(person->{
                   int index = persons.indexOf(person);
                   if(index >=0){
                       persons.set(index,new Person(id,updatePerson.getName()));
                       return 1;
                   }
                   return 0;
                }).orElse(0);
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        return persons.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }




}
