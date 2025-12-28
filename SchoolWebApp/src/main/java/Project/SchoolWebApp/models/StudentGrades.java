package Project.SchoolWebApp.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity(name = "student_grades")
@Table(name = "Student_Grades")
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentGrades {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private UUID id;

    @Column(nullable = true)
    private float grade;

    @ManyToOne
    @JoinColumn(name = "student_code", referencedColumnName = "code",
                foreignKey = @ForeignKey(name = "fk_student_code"))
    Student student = new Student();

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "fk_subject_id"))
    Subject subject = new Subject();
}
