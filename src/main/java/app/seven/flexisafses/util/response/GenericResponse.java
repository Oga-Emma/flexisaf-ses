package app.seven.flexisafses.util.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class GenericResponse <T>{
    private boolean success;
    private String message;
    private T data;
}
