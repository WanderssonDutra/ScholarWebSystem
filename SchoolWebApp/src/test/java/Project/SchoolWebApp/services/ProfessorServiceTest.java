package Project.SchoolWebApp.services;

import Project.SchoolWebApp.dtos.professor_dtos.ProfessorBasicInfoDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorUpdateDTO;
import Project.SchoolWebApp.exceptions.BadRequestException;
import Project.SchoolWebApp.exceptions.DataConflictException;
import Project.SchoolWebApp.exceptions.DataNotFoundException;
import Project.SchoolWebApp.mappers.UserMap;
import Project.SchoolWebApp.models.Professor;
import Project.SchoolWebApp.repositories.ProfessorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTest {

    @InjectMocks
    ProfessorService professorService;

    @Mock
    ProfessorRepository professorRepository;

    private ProfessorDTO professorDTO;

    private ProfessorUpdateDTO professorUpdateDTO;


    @Test
    @DisplayName("should get all professor from DB")
    void allProfessors(){

        Professor professor1 = new Professor("Marilac",
                                     "marilac@gmail.com",
                                     "marilac123",
                                     LocalDate.parse("1995-05-16"));

        Professor professor2 = new Professor("Diego",
                                     "diego@gmail.com",
                                     "diego123",
                                     LocalDate.parse("1989-09-23"));

        Mockito.when(professorRepository.findAll()).thenReturn(List.of(professor1, professor2));
        var response = professorService.allProfessors();

        assertThat(response).hasSize(2);
        assertThat(response.get(0).name()).isEqualTo("Marilac");

        Mockito.verify(professorRepository).findAll();
    }

    @Test
    @DisplayName("should get a professor from DB by searching the code")
    void searchByCodeTestCase1(){

        Professor professor = new Professor("Bonita",
                                     "bonita@gmail.com",
                                     "bonita123",
                                     LocalDate.parse("1998-02-25"));
        professor.setCode("pfr454545");

        Mockito.when(professorRepository.findById("pfr454545")).thenReturn(Optional.of(professor));

        var response = professorService.searchByCode("pfr454545");

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Bonita");

        Mockito.verify(professorRepository).findById("pfr454545");
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when the professor with typed code " +
                 "does not exists in DB")
    void searchByCodeTestCase2(){

        Mockito.when(professorRepository.findById("pfr323233")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, ()-> professorService.searchByCode("pfr323233"));

        Mockito.verify(professorRepository).findById("pfr323233");
    }

    @Test
    @DisplayName("Should get a professor from DB by searching the name")
    void searchByNameTestCase1(){

        Professor professor =new Professor("Jigoto",
                                    "jigoto@gmail.com",
                                    "jigoto123",
                                    LocalDate.parse("1993-07-13"));
        professor.setCode("pfr565656");

        Mockito.when(professorRepository.findByName("GOTO")).thenReturn(List.of(professor));

        var response = professorService.searchByName("GOTO");

        assertThat(response).hasSize(1);
        assertThat(response.get(0).name()).isEqualTo("Jigoto");

        Mockito.verify(professorRepository, Mockito.times(1)).findByName("GOTO");
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when the name cannot be found in DB")
    void searchByNameTestCase2(){

        Mockito.when(professorRepository.findByName("Wiggle")).thenReturn(List.of());

        assertThrows(DataNotFoundException.class, ()-> professorService.searchByName("Wiggle"));

        Mockito.verify(professorRepository).findByName("Wiggle");
    }

    @Test
    @DisplayName("Should create a professor into DB")
    void createTestCase1(){

        professorDTO = new ProfessorDTO("Bonana",
                                        "bonana@gmail.com",
                                        "bonana123",
                                        LocalDate.parse("2003-06-23"));

        Professor professor = UserMap.toEntity(professorDTO, "pfr878787");

        Mockito.when(professorRepository.save(Mockito.any(Professor.class))).thenReturn(professor);

        ProfessorBasicInfoDTO response = professorService.create(professorDTO);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Bonana");

        Mockito.verify(professorRepository).save(Mockito.any());
    }

    @Test
    @DisplayName("Should throw DataConflictException when the email is already in use")
    void createTestCase2(){

        professorDTO = new ProfessorDTO("Bananza",
                                        "belina@gmail.com",
                                        "bananza123",
                                        LocalDate.parse("2004-08-23"));

        Mockito.when(professorRepository.existsByEmail(professorDTO.email())).thenReturn(true);

        assertThrows(DataConflictException.class, ()-> professorService.create(professorDTO));

        Mockito.verify(professorRepository).existsByEmail("belina@gmail.com");
        Mockito.verify(professorRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Should update a professor from DB")
    void updateTestCase1(){

        Professor professor = new Professor("Kalina",
                                     "kalina@gmail.com",
                                     "kalina123",
                                     LocalDate.parse("2005-08-23"));
        professor.setCode("pfr454545");

        professorUpdateDTO = new ProfessorUpdateDTO(null,
                                                    "kalina23@gmail.com",
                                                    "kalina4532",
                                                    null);

        Mockito.when(professorRepository.findById("pfr454545")).thenReturn(Optional.of(professor));
        Mockito.when(professorRepository.existsByEmail(professorUpdateDTO.email())).thenReturn(false);

        Mockito.when(professorRepository.save(Mockito.any(Professor.class))).thenReturn(professor);

        ProfessorBasicInfoDTO response = professorService.update("pfr454545", professorUpdateDTO);

        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("kalina23@gmail.com");

        Mockito.verify(professorRepository).findById("pfr454545");
        Mockito.verify(professorRepository).save(Mockito.any());
    }

    @Test
    @DisplayName("should throw BadRequestException when the professor to be updated " +
                 "already uses the current email")
    void updateTestCase2(){

        Professor professor = new Professor("Baldure",
                                  "baldure@gmail.com",
                                  "baldure123",
                                  LocalDate.parse("1996-08-23"));
        professor.setCode("pfr575757");

        Mockito.when(professorRepository.findById("pfr575757")).thenReturn(Optional.of(professor));

        professorUpdateDTO = new ProfessorUpdateDTO("Baldure",
                                                    "baldure@gmail.com",
                                                    "baldure123",
                                                    LocalDate.parse("1996-08-23"));

        assertThrows(BadRequestException.class, ()-> professorService.update("pfr575757",
                                                                             professorUpdateDTO));

        Mockito.verify(professorRepository).findById("pfr575757");
        Mockito.verify(professorRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Should throw DataConflictException when the email to be updated is already in use")
    void updateTestCase3(){

        Professor professor = new Professor("Jiujipsu",
                                  "jiujipsu@gmail.com",
                                  "jiujipsu123",
                                  LocalDate.parse("1993-08-15"));
        professor.setCode("pfr525252");

        professorUpdateDTO = new ProfessorUpdateDTO(null,
                                                    "polino@gmail.com",
                                                    null,
                                                    null);

        Mockito.when(professorRepository.findById("pfr525252")).thenReturn(Optional.of(professor));
        Mockito.when(professorRepository.existsByEmail(professorUpdateDTO.email()))
                        .thenReturn(true);

        assertThrows(DataConflictException.class, ()-> professorService.update("pfr525252",
                                                                               professorUpdateDTO));

        Mockito.verify(professorRepository).existsByEmail("polino@gmail.com");
        Mockito.verify(professorRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when there is no user with the typed code " +
                 "to be updated")
    void updateTestCase4(){

        professorUpdateDTO = new ProfessorUpdateDTO(null,
                                                    "calcanele@gmail.com",
                                                    null,
                                                    null);

        Mockito.when(professorRepository.findById("pfr141415"))
                        .thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, ()-> professorService.update("pfr141415",
                                                                               professorUpdateDTO));

        Mockito.verify(professorRepository).findById("pfr141415");
        Mockito.verify(professorRepository, Mockito.never()).save(Mockito.any());
    }
    @Test
    @DisplayName("Should delete a professor from DB")
    void deleteTestCase1(){

        Professor professor = new Professor("Blanca",
                                  "blanca@gmail.com",
                                  "blanca123",
                                  LocalDate.parse("1992-05-27"));
        professor.setCode("pfr151515");
        Mockito.when(professorRepository.findById("pfr151515")).thenReturn(Optional.of(professor));
        Mockito.doNothing().when(professorRepository).delete(professor);

        ProfessorBasicInfoDTO response = professorService.delete("pfr151515");

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Blanca");

        Mockito.verify(professorRepository).findById("pfr151515");
        Mockito.verify(professorRepository).delete(professor);
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when there is no professor with the code")
    void deleteTestCase2(){

        Mockito.when(professorRepository.findById("pfr151413"))
                        .thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, ()-> professorService.delete("pfr151413"));

        Mockito.verify(professorRepository).findById("pfr151413");
        Mockito.verify(professorRepository, Mockito.never()).delete(Mockito.any());
    }
}