package Project.SchoolWebApp.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Entity(name = "subject")
@Table(name = "Subject")
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "subjects")
    private Set<Student> students;

    @ManyToMany
    @JoinTable(
            name = "Subject_period",
            joinColumns = @JoinColumn(name = "subject_id",
                                      referencedColumnName = "id",
                                      foreignKey = @ForeignKey(name = "fk_subject_id")),

            inverseJoinColumns = @JoinColumn(name = "period_id",
                                             referencedColumnName = "id",
                                             foreignKey = @ForeignKey(name = "fk_period_id"))
    )
    private Set<Period> periods;

    @OneToMany(mappedBy = "subject")
    private Set<StudentGrades> studentGrades;

}
