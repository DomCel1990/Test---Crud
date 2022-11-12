package com.example.crudtest;

import com.example.crudtest.entities.Student;
import com.example.crudtest.repositories.StudentRepository;
import com.example.crudtest.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")
public class StudentServiceTest {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    @Test
    void checkUserActivation() throws Exception{
        //creo utente
        Student student = new Student();
        student.setWorking(false);
        student.setName("Domenico");
        student.setSurname("Cdelan");
        //lo savo attraverso la repositorty inserendolo nel DB
        Student studentDB= studentRepository.save(student);
        //verifico che non sia null
        assertThat(studentDB).isNotNull();
        assertThat(studentDB.getId()).isNotNull();

        //attraverso lo userService modifico i suoi valori
        Student studentFromService= studentService.upDateWorking(student.getId(), true);
        //vedo che lo userFromService esiste e che non sia null
        assertThat(studentFromService).isNotNull();
        assertThat(studentFromService.getId()).isNotNull();
        //controllo che stato di attivazione è true
        assertThat(studentFromService.isWorking()).isTrue();

        Student studentFind= studentRepository.findById(studentDB.getId()).get();
        assertThat(studentFind.getId()).isNotNull();
        assertThat(studentFind.getId()).isNotNull();
        assertThat(studentFind.getId()).isEqualTo(studentDB.getId());
        assertThat(studentFind.isWorking()).isTrue();

    }
}
