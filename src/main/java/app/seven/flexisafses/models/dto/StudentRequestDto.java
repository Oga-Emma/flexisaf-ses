package app.seven.flexisafses.models.dto;

import app.seven.flexisafses.models.pojo.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestDto {
    public String firstName;
    public String lastName;
    public String otherName;

    public Gender gender;

    @JsonFormat(pattern="dd/MM/yyyy")
    public LocalDate dateOfBirth;

    public String department;
}
