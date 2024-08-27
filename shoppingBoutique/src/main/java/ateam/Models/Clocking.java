package ateam.Models;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 *
 * @author user pc
 */
public class Clocking {

    private int clocking_ID;
    private int employee_ID;
    private Timestamp clock_in;
    private Timestamp clock_out;
    private String first_name;
    private String last_name;
    private String company;

}
