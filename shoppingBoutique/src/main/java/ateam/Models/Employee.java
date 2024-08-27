package ateam.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private int employee_ID;
    private String first_name;
    private String last_name;
    private String employees_id;
    private String employee_password;
    private Role role;
    private String email;
    private String company;
    private int min_hours;
}
