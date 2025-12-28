package Project.SchoolWebApp.services;

import Project.SchoolWebApp.exceptions.BadRequestException;
import Project.SchoolWebApp.exceptions.DataNotFoundException;
import Project.SchoolWebApp.models.Professor;
import Project.SchoolWebApp.repositories.ProfessorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class ProfessorServiceTest {

    @Autowired
    ProfessorService professorService;

    @Autowired
    ProfessorRepository professorRepository;

    @Mock
    Professor professor;


    @Test
    @DisplayName("should get all professor from DB")
    void allProfessors(){
        professor = new Professor("Marilac", "marilac@gmail.com", "marilac123",
                                  LocalDate.parse("1995-05-16"));
        professor.setCode("pfr121212");
        professorRepository.save(professor);

        professor = new Professor("Diego", "diego@gmail.com", "diego123",
                                  LocalDate.parse("1989-09-23"));
        professor.setCode("pfr323232");
        professorRepository.save(professor);

        var response = professorService.allProfessors();

        assertThat(response).hasSize(2);
    }

    @Test
    @DisplayName("should get a professor from DB by searching id")
    void searchByCodeTestCase1(){

        professor = new Professor("Bonita", "bonita@gmail.com", "bonita123",
                                  LocalDate.parse("1998-02-25"));
        professor.setCode("pfr454545");

        professorRepository.save(professor);

        professor = new Professor("Feia", "feia@gmail.com", "feia123",
                                  LocalDate.parse("1996-08-15"));
        professor.setCode("pfr787878");

        professorRepository.save(professor);

        var response = professorService.searchByCode("pfr454545");

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Bonita");
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when a professor id is not in DB")
    void searchByCodeTestCase2()throws DataNotFoundException{

        professor = new Professor("Gabriela", "gabriela@gmail.com", "gabriela123",
                                  LocalDate.parse("1994-01-25"));
        professor.setCode("pfr676767");

        professorRepository.save(professor);

        assertThrows(DataNotFoundException.class, ()-> professorService.searchByCode("pfr323233"),
                     "there is no user with this id.");
    }

    @Test
    @DisplayName("Should throw BadRequestException when the code does not follow the requisitions")
    void searchByCodeTestCase3() throws BadRequestException{

        professor = new Professor("Nafila", "nafila@gmail.com", "nafila123",
                                  LocalDate.parse("1999-03-16"));
        professor.setCode("pfr767676");

        assertThrows(BadRequestException.class, ()-> professorService.searchByCode("897ghjtf"),
                "the code must begin with the letters: \"pfr\" " +
                         "following a sequence of six numbers.");
    }

    @Test
    @DisplayName("Should get a professor from DB by searching the name")
    void searchByNameTestCase1(){

        professor =new Professor("Jigoto", "jigoto@gmail.com", "jigoto123",
                                 LocalDate.parse("1993-07-13"));
        professor.setCode("pfr565656");

        professorRepository.save(professor);

        professor = new Professor("Kalota", "kalota@gmail.com", "kalota123",
                                  LocalDate.parse("1994-03-12"));
        professor.setCode("pfr232323");

        professorRepository.save(professor);

        var response = professorService.searchByName("GOTO");

        assertThat(response).hasSize(1);
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when the name cannot be found in DB")
    void searchByNameTestCase2() throws DataNotFoundException{

        professor = new Professor("Aurela", "aurela@gmail.com", "aurela123",
                                  LocalDate.parse("1889-08-26"));
        professor.setCode("pfr090909");

        assertThrows(DataNotFoundException.class, ()-> professorService.searchByName("Wiggle"),
                     "The name does not exist.");
    }
}
