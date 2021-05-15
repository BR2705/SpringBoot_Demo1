package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("Postgres")
public class PostgresDao implements  PersonDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgresDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        final  String sql = "INSERT INTO person (id,name) values (?,?)";
        return jdbcTemplate.update(sql,new Object[]{id,person.getName()});
    }

    @Override
    public List<Person> selectAllPerson() {
        final String sql = "SELECT id,name from person";
        return jdbcTemplate.query(sql, (resultSet,i)->{
            UUID id = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(id,name);
        });
    }

    @Override
    public int deletePerson(UUID id) {
        final String sql = "DELETE from person where id=?";
        return jdbcTemplate.update(sql,new Object[]{id});
    }

    @Override
    public int updatePerson(UUID id, Person person) {
        return selectPersonById(id)
                .map(people -> {
                    final String sql = "UPDATE person set name=? where id=?";
                    return  jdbcTemplate.update(sql,new Object[]{person.getName(),people.getId()});
                }).orElse(0);
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        final String sql = "SELECT id,name from person WHERE id =?";
        Person person = jdbcTemplate.queryForObject(sql,new Object[]{id},(resultSet,i)->{
           UUID personId = UUID.fromString(resultSet.getString("id"));
           String name = resultSet.getString("name");
           return  new Person(personId,name);
        });
        return Optional.ofNullable(person);
    }
}
