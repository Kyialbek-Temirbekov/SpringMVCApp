package org.example.dao;

import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDao {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM mvc_person", new BeanPropertyRowMapper<>(Person.class));
    }
    public Optional<Person> show (String email) {
        return jdbcTemplate.query("SELECT * FROM mvc_person WHERE email=?", new Object[]{email}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM mvc_person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }
    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO mvc_person (name,age,email,address) VALUES(?,?,?,?)", person.getName(), person.getAge(), person.getEmail(), person.getAddress());
    }
    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE mvc_person SET name=?, age=?, email=?, address=? WHERE id=?", person.getName(), person.getAge(), person.getEmail(), person.getAddress(), id);
    }
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM mvc_person WHERE id=?", id);
    }

    ///////////////////////////////////
    // Testing batch update performance

    private List<Person> create1000people() {
        List<Person> people = new ArrayList<>();
        for(int i=0;i<1000;i++)
            people.add(new Person(i,"name " + i, 30,"test" + i + "@mail.ru", "some address"));
        return people;
    }
    public void testMultipleUpdate() {
        List<Person> people = create1000people();
        long before = System.currentTimeMillis();
        for(Person person : people)
            jdbcTemplate.update("INSERT INTO mvc_person (id,name,age,email) VALUES(?,?,?,?)",person.getId(), person.getName(), person.getAge(), person.getEmail());
        long after = System.currentTimeMillis();
        System.out.println("Multiple Update Time: " + (after - before));
    }
    public void testBatchUpdate() {
        List<Person> people = create1000people();
        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate("INSERT INTO mvc_person (id,name,age,email) VALUES(?,?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1,people.get(i).getId());
                ps.setString(2,people.get(i).getName());
                ps.setInt(3,people.get(i).getAge());
                ps.setString(4,people.get(i).getEmail());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });

        long after = System.currentTimeMillis();
        System.out.println("Batch Update Time: " + (after - before));
    }
}
