package Project.SchoolWebApp.controllers;

import Project.SchoolWebApp.dtos.professor_dtos.ProfessorBasicInfoDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorUpdateDTO;
import Project.SchoolWebApp.exceptions.BadRequestException;
import Project.SchoolWebApp.exceptions.DataConflictException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.notNull;
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

    private ProfessorUpdateDTO professorUpdateDTO;

    @Test
    @DisplayName("Should return status ok when searching all professors")
    void AllProfessorsTest() throws Exception{

        when(professorService.allProfessors()).thenReturn(List.of());

        mockMvc.perform(get("/professor/all")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return ok when searching a professor by code")
    void searchByCodeTestCase1() throws Exception{

        when(professorService.searchByCode("pfr121212"))
                .thenReturn(new ProfessorBasicInfoDTO("Loraina", "loraina@gmail.com",
                                                      LocalDate.parse("1994-02-18")));

        mockMvc.perform(get("/professor/id/pfr121212")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return Not Found status when searching a non existing code")
    void searchByCodeTestCase2() throws Exception{

     when(professorService.searchByCode("pfr232323")).thenThrow(DataNotFoundException.class);

     mockMvc.perform(get("/professor/id/pfr232323")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return Bad Request status when searching a code that" +
                 " does not follow requisitions")
    void searchByCodeTestCase3() throws Exception{

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
    @DisplayName("Should return Bad Request status when searching a name that" +
                 " does not follow requisitions")
    void searchByNameTestCase3() throws Exception{

        mockMvc.perform(get("/professor/name/B3rn4dette")).andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return status created when creating a new professor")
    void createProfessorTestCase1() throws Exception{

        professorDTO = new ProfessorDTO("Dan", "dan@gmail.com", "dan123",
                                        LocalDate.parse("1995-03-18"));

        String json = objectMapper.writeValueAsString(professorDTO);

        mockMvc.perform(post("/professor/new")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(json)).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return DataConflict status when the email from the user to be created " +
                 "is already in use")
    void createProfessorTestCase2() throws Exception{

        professorDTO = new ProfessorDTO("Leba", "leba@gmail.com", "leba123",
                LocalDate.parse("1994-03-18"));

        when(professorService.create(professorDTO)).thenThrow(DataConflictException.class);

        String json = objectMapper.writeValueAsString(professorDTO);

        mockMvc.perform(post("/professor/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return status BadRequest when fields doesn´t follow requisitions")
    void createProfessorTestCase3() throws Exception{


        professorDTO = new ProfessorDTO("L3b4", "leba@@gmail.com", "leba123",
                                        LocalDate.parse("1994-03-18"));
        String json = objectMapper.writeValueAsString(professorDTO);

        mockMvc.perform(post("/professor/new")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should return status BadRequest when trying to create a professor with null values")
    void createProfessorTestCase4() throws Exception{

        professorDTO = new ProfessorDTO(null,
                                        "nionakiku@gmail.com",
                                        "niona5642",
                                        null);

        String json = objectMapper.writeValueAsString(professorDTO);
        mockMvc.perform(post("/professor/new")
               .contentType(MediaType.APPLICATION_JSON).content(json))
               .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update a professor from DB")
    void updateProfessorTestCase1() throws Exception{

        professorUpdateDTO = new ProfessorUpdateDTO(null, "nionakiku@gmail.com",
                                        "niona5642", null);

        ProfessorBasicInfoDTO basicInfoDTO = new ProfessorBasicInfoDTO("Niona",
                                                                       "nionakiku@gmail.com",
                                                                       LocalDate.parse("1994-03-18"));
        when(professorService.update("pfr656565", professorUpdateDTO)).thenReturn(basicInfoDTO);

        String json = objectMapper.writeValueAsString(professorUpdateDTO);

        mockMvc.perform(put("/professor/update/{code}", "pfr656565")
               .contentType(MediaType.APPLICATION_JSON).content(json))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return status notFound when trying to update a non existing professor")
    void updateProfessorTestCase2() throws Exception{

        professorUpdateDTO = new ProfessorUpdateDTO("Leba", "leba@gmail.com", "leba123",
                LocalDate.parse("1994-03-18"));

        when(professorService.update("pfr424242", professorUpdateDTO))
                .thenThrow(new DataNotFoundException("there is no professor with this id."));

        String json = objectMapper.writeValueAsString(professorUpdateDTO);

        mockMvc.perform(put("/professor/update/{code}", "pfr424242")
               .contentType(MediaType.APPLICATION_JSON).content(json))
               .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return status BadRequest when the email to update is already the current email")
    void updateProfessorTestCase3() throws Exception{

        professorUpdateDTO = new ProfessorUpdateDTO("Leba", "leba@gmail.com", "leba123",
                LocalDate.parse("1994-03-18"));

        when(professorService.update("pfr636363", professorUpdateDTO))
                .thenThrow(new BadRequestException("This user already uses the current email."));

        String json = objectMapper.writeValueAsString(professorUpdateDTO);
        mockMvc.perform(put("/professor/update/{code}", "pfr636363")
               .contentType(MediaType.APPLICATION_JSON).content(json))
               .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status Conflict when the email to update is already being used")
    void updateProfessorTestCase4() throws Exception{

        professorUpdateDTO = new ProfessorUpdateDTO("Leba", "leba@gmail.com", "leba123",
                LocalDate.parse("1994-03-18"));

        when(professorService.update("pfr787878", professorUpdateDTO))
                .thenThrow(new DataConflictException("The email is already in use."));

        String json = objectMapper.writeValueAsString(professorUpdateDTO);

        mockMvc.perform(put("/professor/update/{code}", "pfr787878")
               .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return status BadRequest when the code of the professor to be updated" +
                 " is not a valid code")
    void updateProfessorTestCase5() throws Exception{

        professorUpdateDTO = new ProfessorUpdateDTO(null,
                                        "luca123@gmail.com",
                                        "luca76367",
                                        null);

        String json = objectMapper.writeValueAsString(professorUpdateDTO);
        mockMvc.perform(put("/professor/update/{code}", "pftjj457")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status ok when deleting a professor from DB")
    void deleteProfessorTestCase1() throws Exception{

        when(professorService.delete("pfr565656"))
                .thenReturn(new ProfessorBasicInfoDTO("Kenaida",
                                                      "kenaida@gmail.com",
                                                      LocalDate.parse("1999-04-20")));

        mockMvc.perform(delete("/professor/delete/{code}", "pfr565656"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return status NotFound when trying to delete a non existing professor")
    void deleteProfessorTestCase2() throws Exception{

        when(professorService.delete("pfr898989"))
                .thenThrow(new DataNotFoundException("there is no professor with this code."));

        mockMvc.perform(delete("/professor/delete/{code}", "pfr898989"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return status BadRequest when the code typed to delete a professor is" +
            " not a valid code")
    void deleteProfessorTestCase3() throws Exception{

        mockMvc.perform(delete("/professor/delete/{code}", "kjhg4565"))
                .andExpect(status().isBadRequest());
    }
}
