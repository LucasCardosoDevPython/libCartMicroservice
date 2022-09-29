package library.libCartMicroservice.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClientDTO {
    private String email;
    private String name;
    private LocalDate birthDate;
    private String phone;
    private String sex;
}
