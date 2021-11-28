package app.seven.flexisafses.models.pojo;

import app.seven.flexisafses.util.AuditMetaData;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode
@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Document
public class Student extends AuditMetaData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String otherName;

    @NotNull
    private String matricNumber;

    private LocalDate dateOfBirth;

    @NotNull
    private Gender gender;

    @NotNull
    @DBRef()
    private Department department;

    @NotNull
    @DBRef()
    private AppUser createdBy;

    private LocalDate createdAt = LocalDate.now();

}
