package Project.SchoolWebApp.repositories;

import Project.SchoolWebApp.models.Professor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class ProfessorRepositoryTest {

    @Autowired
    ProfessorRepository professorRepository;

    @Mock
    Professor professor;

    @Test
    @DisplayName("should get all the professors that matches the searched name")
    void findByNameTestCase1(){

        professor = new Professor();

        professor.setName("Alanildo Silva");
        professor.setEmail("alanildo@gmail.com");
        professor.setPassword("alanildo123");
        professor.setBirthDate(LocalDate.of(1989, 07, 23));
        professor.setCode("pfr999999");

        professorRepository.save(professor);

        professor.setName("Silvana Augusta");
        professor.setEmail("silvana@gmail.com");
        professor.setPassword("silvana123");
        professor.setBirthDate(LocalDate.of(1990, 11, 15));
        professor.setCode("pfr999991");

        professorRepository.save(professor);

        List<Professor> response = professorRepository.findByName("an");

        assertThat(response).hasSize(2);

    }

    @Test
    @DisplayName("Should not get a student from DB by searching a non existing name")
    void FindByNameTestCase2(){

        professor = new Professor();

        professor.setName("Ricaro");
        professor.setEmail("ricaro@gmail.com");
        professor.setPassword("ricaro123");
        professor.setBirthDate(LocalDate.of(2001,03,25));
        professor.setCode("pfr787878");

        professorRepository.save(professor);

        var result = professorRepository.findByName("Lola");
        assertThat(result).isEmpty();
    }
}
