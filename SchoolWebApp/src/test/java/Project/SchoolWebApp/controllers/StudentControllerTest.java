package Project.SchoolWebApp.controllers;

import Project.SchoolWebApp.dtos.student_dtos.StudentBasicInfoDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentUpdateDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.checkerframework.checker.regex.qual.Regex;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import({StudentControllerTest.TestConfig.class, NameValidation.class, EmailValidationTest.class})
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    private StudentDTO studentDTO;
    private StudentUpdateDTO studentUpdateDTO;

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
    @DisplayName("Should return ok when searching an existing code in DB")
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

       mockMvc.perform(get("/student/id/std676767")).andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").exists())
               .andExpect(jsonPath("$.status").value(404));
   }

   @Test
   @DisplayName("Should return bad request when the code searched does not follow required rules")
   void searchByCodeTestCase3() throws Exception{

        /*when(studentService.searchByCode("stdghtfg"))
            .thenThrow(new BadRequestException("invalid code format"));*/

        mockMvc.perform(get("/student/id/{code}", "stdghtfg"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(400));
   }

   @Test
   @DisplayName("Should return status ok when searching a student name")
   void searchByNameTestCase1() throws Exception{

        StudentBasicInfoDTO responseDTO = new StudentBasicInfoDTO("Carlos Benson",
                                                               "carlos@gmail.com",
                                                               LocalDate.parse("2001-03-24"),
                                                               LocalDate.parse("2010-02-01"));
        ArrayList<StudentBasicInfoDTO> responseList = new ArrayList<>();
        responseList.add(responseDTO);
        when(studentService.searchByName("Carlos")).thenReturn(responseList);

        mockMvc.perform(get("/student/name/{name}", "Carlos"))
                .andExpect(status().isOk());
   }

   @Test
   @DisplayName("Should return status BadRequest when there is no professor with the searched name")
   void searchByNameTestCase2() throws Exception{

        when(studentService.searchByName("Lubisca")).thenThrow(new DataNotFoundException("There is " +
                                                                            "no user with this name."));

        mockMvc.perform(get("/student/name/{name}", "Lubisca"))
                .andExpect(status().isNotFound());
   }

   @Test
   @DisplayName("Should return BadRequest when the searched name is not a valid name")
   void searchByNameTestCase3() throws Exception{

        when(studentService.searchByName("L3m0n")).thenThrow(new BadRequestException("The name must " +
                                                             "not have numbers or special characters"));

        mockMvc.perform(get("/student/name/{name}", "L3m0n"))
                .andExpect(status().isBadRequest());
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should return status DataConflict when trying to create a student" +
                 " with an already existing email")
    void createStudentTestCase3() throws Exception{

        studentDTO = new StudentDTO("bella",
                "bella@gmail.com",
                "bella4567",
                LocalDate.parse("2006-05-13"),
                LocalDate.parse("2011-02-01"));

        when(studentService.create(studentDTO))
                .thenThrow(new DataConflictException("the email is already being used"));

        String json = objectMapper.writeValueAsString(studentDTO);
        mockMvc.perform(post("/student/new")
               .contentType(MediaType.APPLICATION_JSON).content(json))
               .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return status BadRequest when trying to create a student with null values")
    void createStudentTestCase4() throws Exception{

        studentDTO = new StudentDTO(null,
                null,
                "bella4567",
                LocalDate.parse("2006-05-13"),
                LocalDate.parse("2011-02-01"));

        String json = objectMapper.writeValueAsString(studentDTO);
        mockMvc.perform(post("/student/new")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status ok when updating a student")
    void updateStudentTestCase1() throws Exception{

        studentUpdateDTO = new StudentUpdateDTO("Bananza", "bananza@gmail.com",
                "bananza5463", LocalDate.parse("2004-04-23"),
                LocalDate.parse("2011-02-01"));

        String json = objectMapper.writeValueAsString(studentUpdateDTO);

        mockMvc.perform(put("/student/update/std909090")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("Should throw data conflict status when trying to update with an already used email")
    void updateStudentTestCase2() throws Exception{

        studentUpdateDTO = new StudentUpdateDTO("Bananza", "loira@gmail.com",
                "bananza123", LocalDate.parse("2002-07-16"),
                LocalDate.parse("2008-02-01"));

        String json = objectMapper.writeValueAsString(studentUpdateDTO);

        when(studentService.update("std787878", studentUpdateDTO))
                .thenThrow(new DataConflictException(" invalid email"));

        mockMvc.perform(put("/student/update/std787878")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Should return status BadRequest when the email to be updated " +
                 "is already the current email")
    void updateStudentTestCase3() throws Exception{

        studentUpdateDTO = new StudentUpdateDTO("Bella",
                "bella@gmail.com",
                "bella4567",
                LocalDate.parse("2006-05-13"),
                LocalDate.parse("2011-02-01"));

        when(studentService.update("std585858", studentUpdateDTO))
                .thenThrow(new BadRequestException("the email is already being used by the student"));

        String json = objectMapper.writeValueAsString(studentUpdateDTO);
        mockMvc.perform(put("/student/update/{code}", "std585858")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(400));
    }
    @Test
    @DisplayName("Should return status NotFound when there is no student with this code " +
                 "to be updated")
    void updateStudentTestCase4() throws Exception{

        studentUpdateDTO = new StudentUpdateDTO("Bella",
                                    "bella@gmail.com",
                                    "bella4567",
                                    LocalDate.parse("2006-05-13"),
                                    LocalDate.parse("2011-02-01"));

        when(studentService.update("std565656", studentUpdateDTO))
                .thenThrow(new DataNotFoundException("there is no student with this id"));
        String json = objectMapper.writeValueAsString(studentUpdateDTO);

        mockMvc.perform(put("/student/update/{code}", "std565656")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return status BadRequest when the code of the student to be updated" +
                 " is not a valid code")
    void updateStudentTestCase5() throws Exception{

        studentUpdateDTO = new StudentUpdateDTO("Bella",
                "bella@gmail.com",
                "bella4567",
                LocalDate.parse("2006-05-13"),
                LocalDate.parse("2011-02-01"));

        String json = objectMapper.writeValueAsString(studentUpdateDTO);
        mockMvc.perform(put("/student/update/{code}", "8455242")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status ok when deleting a student from DB")
    void deleteStudentTestCase1() throws Exception{

        when(studentService.delete("std454545"))
                .thenReturn(new StudentBasicInfoDTO("Zara Larson",
                                                    "zaralarson@gmail.com",
                                                    LocalDate.parse("2003-05-16"),
                                                    LocalDate.parse("2010-02-01")));

        mockMvc.perform(delete("/student/delete/{code}", "std454545"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return status NotFound when searching a non existing code")
    void deleteStudentTestCase2()throws Exception{

        when(studentService.delete("std474747"))
                .thenThrow(new DataNotFoundException("there is no user with this code"));

        mockMvc.perform(delete("/student/delete/{code}", "std474747"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return status BadRequest when the searched code to delete a student " +
            " is not a valid code")
    void deleteStudentTestCase3() throws Exception{

        mockMvc.perform(delete("/student/delete/{code}", "stdlgklfkf"))
                .andExpect(status().isBadRequest());
    }
}