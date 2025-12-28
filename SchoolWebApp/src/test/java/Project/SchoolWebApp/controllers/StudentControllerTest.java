package Project.SchoolWebApp.controllers;

import Project.SchoolWebApp.dtos.student_dtos.StudentBasicInfoDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentDTO;
import Project.SchoolWebApp.exceptions.BadRequestException;
import Project.SchoolWebApp.exceptions.DataConflictException;
import Project.SchoolWebApp.exceptions.DataNotFoundException;
import Project.SchoolWebApp.services.StudentService;
import Project.SchoolWebApp.validations.EmailValidationTest;
import Project.SchoolWebApp.validations.NameValidation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import({StudentControllerTest.TestConfig.class, NameValidation.class, EmailValidationTest.class})
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    private StudentDTO studentDTO;

    @TestConfiguration
    static class TestConfig{
        @Bean
        StudentService studentService(){
            return Mockito.mock(StudentService.class);
        }
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    StudentService studentService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return status ok when searching all students")
    void allStudentsTestCase1() throws Exception{

        when(studentService.allStudents()).thenReturn(List.of());

       mockMvc.perform(get("/student/all")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return ok when passing an existing id in DB")
    void searchByCodeTestCase1() throws Exception{

        StudentBasicInfoDTO studentBasicInfoDTO = new StudentBasicInfoDTO("Leonora",
                                                                 "leonora@gmail.com",
                                                                  LocalDate.parse("2006-08-17"),
                                                                  LocalDate.parse("2001-02-01"));
        when(studentService.searchByCode("std454545")).thenReturn(studentBasicInfoDTO);

        mockMvc.perform(get("/student/id/std454545")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return status not found when the code do not exists in DB")
   void searchByCodeTestCase2() throws Exception{


       when(studentService.searchByCode("std676767")).thenThrow(new DataNotFoundException
                                                      ("There is no user with this code."));

       mockMvc.perform(get("/student/id/std676767")).andExpect(status().isNotFound());
   }

   @Test
   @DisplayName("Should return bad request when the id searched does not follow required rules")
   void searchByCodeTestCase3() throws Exception{

        when(studentService.searchByCode("stdghtfg")).thenThrow(new BadRequestException
                ("\"the code must begin with the letters: \\\"std\\\" \" +\n" +
                "\"following a sequence of six numbers.\""));

        mockMvc.perform(get("/student/id/stdghtfg")).andExpect(status().isBadRequest());
   }

    @Test
    @DisplayName("Should return created when the requested body is correct")
    void createStudentTestCase1() throws Exception{

        studentDTO = new StudentDTO("Loira", "loira@gmail.com",
                "loira123", LocalDate.parse("2001-05-17"),
                LocalDate.parse("2010-02-01"));

        String json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(post("/student/new").contentType(MediaType.APPLICATION_JSON)
                                                       .content(json)).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return Bad Request status when the body fields are not correct")
    void createStudentTestCase2() throws Exception{

        studentDTO = new StudentDTO("12345", "loira#Gmaga,com",
                "loira123", LocalDate.parse("2001-05-14"),
                LocalDate.parse("2010-02-01"));

        String json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(post("/student/new")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status ok when updating a student")
    void updateStudentTestCase1() throws Exception{

        studentDTO = new StudentDTO("Bananza", "bananza@gmail.com",
                "bananza5463", LocalDate.parse("2004-04-23"),
                LocalDate.parse("2011-02-01"));

        String json = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(put("/student/update/std909090")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("Should throw data conflict status when trying to update with an already used email")
    void updatedStudentTestCase2() throws Exception{

        studentDTO = new StudentDTO("Bananza", "loira@gmail.com",
                "bananza123", LocalDate.parse("2002-07-16"),
                LocalDate.parse("2008-02-01"));

        String json = objectMapper.writeValueAsString(studentDTO);

        when(studentService.update("std787878", studentDTO)).thenThrow(DataConflictException.class);

        mockMvc.perform(put("/student/update/std787878")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }
}