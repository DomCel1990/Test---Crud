package com.example.crudtest;

import com.example.crudtest.controllers.StudentController;
import com.example.crudtest.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class StudentControllerTest {


    @Autowired
    private StudentController studentController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    //Creo una funzione ausiliaria che mi crea un utent
    private Student generateStudent() throws Exception {
        Student student = new Student("Domenico", "Celani ", false);
        return generateStudent(student);
    }
    private Student generateStudent(Student student) throws Exception {
        MvcResult result= generateStudentinDB(student);
        Student student1 = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        return student1;
    }
    //Creo una funzione ausiliaria che salva nel DB uno studente
    private MvcResult generateStudentDB() throws Exception {
        Student student = new Student("Domenico", "Celani ", false);
        return generateStudentinDB(student);
    }

    //Creo una funzione ausiliaria che prende uno studento e lo salva nel DB
    private MvcResult generateStudentinDB(Student student) throws Exception {
        String studentString = objectMapper.writeValueAsString(student);
        MvcResult result = this.mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentString))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        return result;

    }

    //controllo che il controller non sia vuoto
    @Test
    void studentControllerLoads() {
        assertThat(studentController).isNotNull();
    }

    @Test
    void createStudent() throws Exception {
        Student student =generateStudent();
        assertThat(student.getId()).isNotNull();
    }

    @Test
    void studentGetTest() throws Exception {
        //inserisco uno studente nel DB
        generateStudentDB();
        //dico ti far partire il metodo get
        MvcResult result = this.mockMvc.perform(get("/student"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<Student> student = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(student.size()).isNotZero();
    }

    @Test
    void getSingleUserTest() throws Exception {
        //Creo uno studente
        Student student = generateStudent();
        //dico ti far partire il metodo get per ottenere un singolo utente
        MvcResult result = this.mockMvc.perform(get("/student/" +student.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Student student1 = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertThat(student1.getId()).isEqualTo(student.getId());
    }


}

