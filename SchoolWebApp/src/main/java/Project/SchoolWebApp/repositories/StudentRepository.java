package Project.SchoolWebApp.repositories;

import Project.SchoolWebApp.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {

    public Optional<Student> findById(String code);

    public boolean existsById(String code);

    public boolean existsByEmail(String email);

    public Student findByEmail(String email);

    @Query("select s from student s " +
            "where lower(s.name) like lower(concat('%', :name , '%'))")
    public List<Student> findByName(@Param("name") String name);
}
