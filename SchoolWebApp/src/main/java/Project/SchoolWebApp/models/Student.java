package Project.SchoolWebApp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity(name = "student")
@Table(name = "Student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User{

    @Column(nullable = false)
    private LocalDate yearOfEnrollment;

    @OneToMany(mappedBy = "student")
    private Set<StudentGrades> studentGrades;

    @ManyToMany
    @JoinTable(
            name = "Student_subject",
            joinColumns = @JoinColumn(name = "student_code"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects;

    public Student(String name, String email, String password, LocalDate birthDate,
                   LocalDate yearOfEnrollment){
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.yearOfEnrollment = yearOfEnrollment;
    }
}
