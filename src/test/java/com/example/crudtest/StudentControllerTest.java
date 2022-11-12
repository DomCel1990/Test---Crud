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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    //Creo funzione ausiliaria per prendere un singolo studente e non duplicare codice
    private Student getStudentFromId(Long id) throws Exception {
        MvcResult result = this.mockMvc.perform(get("/student/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        try {
            Student student1 = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
            return student1;
        }catch (Exception e){
            return null;
        }
    }

    //Creo una funzione ausiliaria che mi crea un utent
    private Student generateStudent() throws Exception {
        Student student = new Student("Domenico", "Celani ", false);
        return generateStudent(student);
    }

    private Student generateStudent(Student student) throws Exception {
        MvcResult result = generateStudentinDB(student);
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
        Student student = generateStudent();
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
        Student student1= getStudentFromId(student.getId());
        assertThat(student1.getId()).isEqualTo(student.getId());
    }

    @Test
    void updateUser() throws Exception {
        Student student = generateStudent();
        assertThat(student.getId()).isNotNull();

        String newName = "Marco";
        student.setName(newName);
        String json = objectMapper.writeValueAsString(student);
        //dico di fare il put di student a mockMVC
        MvcResult mvcResult = this.mockMvc.perform((put("/student/" + student.getId())
                        //gli dico che è un json
                        .contentType(MediaType.APPLICATION_JSON)
                        //gli do il contenuto
                        .content(json)))
                //gli dico di stampare tutta la rispons
                .andDo(print())
                //aspettati che sia tutto ok
                .andExpect(status().isOk())
                //fai return
                .andReturn();
        Student student1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Student.class);

        //qui stiamo controllando che il nostro put sia andato a buon fine
        assertThat(student1.getId()).isEqualTo(student.getId());
        assertThat(student1.getName()).isEqualTo(newName);

        //verifico anche con il get
        Student studentGet= getStudentFromId(student.getId());
        assertThat(studentGet.getId()).isEqualTo(student.getId());
        assertThat(studentGet.getName()).isEqualTo(newName);
    }
    @Test
    void deleteStudentTest() throws Exception{
        Student student = generateStudent();
        assertThat(student.getId()).isNotNull();
        //dico di fare il delete di student a mockMVC
        MvcResult mvcResult = this.mockMvc.perform(delete("/student/"+student.getId()))
                //gli dico di stampare tutta la rispons
                .andDo(print())
                //aspettati che sia tutto ok
                .andExpect(status().isOk())
                //fai return
                .andReturn();

        Student studentGet= getStudentFromId(student.getId());
        assertThat(studentGet).isNull();
    }

    @Test
    void updateStudentIsWoking() throws Exception {
        Student student = generateStudent();
        assertThat(student.getId()).isNotNull();
        MvcResult mvcResult = this.mockMvc.perform(put("/student/"+student.getId()+"/is-working?working=true"))

                //gli dico di stampare tutta la rispons
                .andDo(print())
                //aspettati che sia tutto ok
                .andExpect(status().isOk())
                //fai return
                .andReturn();

        Student student1 = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Student.class);
        assertThat(student1.getId()).isNotNull();
        assertThat(student1.getId()).isEqualTo(student.getId());
        assertThat(student1.isWorking()).isEqualTo(true);

    }
}

