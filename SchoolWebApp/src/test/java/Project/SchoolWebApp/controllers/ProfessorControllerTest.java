package Project.SchoolWebApp.controllers;

import Project.SchoolWebApp.dtos.professor_dtos.ProfessorBasicInfoDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorDTO;
import Project.SchoolWebApp.exceptions.BadRequestException;
import Project.SchoolWebApp.exceptions.DataNotFoundException;
import Project.SchoolWebApp.services.ProfessorService;
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
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(ProfessorController.class)
@Import({NameValidation.class, EmailValidationTest.class, ProfessorControllerTest.TestConfig.class})
@AutoConfigureMockMvc(addFilters = false)
public class ProfessorControllerTest {

    @TestConfiguration
    static class TestConfig{

        @Bean
        ProfessorService professorService(){
            return Mockito.mock(ProfessorService.class);
        }
    }

    @Autowired
    ProfessorService professorService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private ProfessorDTO professorDTO;

    @Test
    @DisplayName("Should return status ok when searching all professors")
    void AllProfessorsTest() throws Exception{

        when(professorService.allProfessors()).thenReturn(List.of());

        mockMvc.perform(get("/professor/all")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return ok when searching a professor by id")
    void searchByIdTestCase1() throws Exception{

        when(professorService.searchByCode("pfr121212"))
                .thenReturn(new ProfessorBasicInfoDTO("Loraina", "loraina@gmail.com",
                                                      LocalDate.parse("1994-02-18")));

        mockMvc.perform(get("/professor/id/pfr121212")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return Not Found status when searching a non existing code")
    void searchByIdTestCase2() throws Exception{

     when(professorService.searchByCode("pfr232323")).thenThrow(DataNotFoundException.class);

     mockMvc.perform(get("/professor/id/pfr232323")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return Bad Request status when searching id that does not follow " +
                 "requested rules")
    void searchByIdTestCase3() throws Exception{

        when(professorService.searchByCode("fhgtd333")).thenThrow(BadRequestException.class);

        mockMvc.perform(get("/professor/id/fhgtd333")).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return ok status when searching an existing name")
    void searchByNameTestCase1() throws Exception{

        when(professorService.searchByName("Bernade"))
                .thenReturn(List.of());

        mockMvc.perform(get("/professor/name/Bernade")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return Not Found status when searching a non existing name")
    void searchByNameTestCase2() throws Exception{

        when(professorService.searchByName("Balella")).thenThrow(DataNotFoundException.class);

        mockMvc.perform(get("/professor/name/Balella")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return Bad Request status when searching a name that does not follow " +
                 "requested rules")
    void searchByNameTestCase3() throws Exception{

        when(professorService.searchByName("B3rn4dette")).thenThrow(BadRequestException.class);

        mockMvc.perform(get("/professor/name/B3rn4dette")).andExpect(status().isBadRequest());
    }

}
