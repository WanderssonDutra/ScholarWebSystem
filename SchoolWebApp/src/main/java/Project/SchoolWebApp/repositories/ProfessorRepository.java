package Project.SchoolWebApp.repositories;

import Project.SchoolWebApp.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProfessorRepository extends JpaRepository<Professor, String> {

    public boolean existsById(String id);

    public boolean existsByEmail(String email);

    @Query("select p from professor p where lower(p.name) like lower(concat('%', :name, '%'))")
    public List<Professor> findByName(@Param("name") String name);
}
