package ma.emsi.digitalbankbackend.dtos;

import lombok.Data;
import ma.emsi.digitalbankbackend.entities.BankAccount;

import java.util.List;


@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
}
