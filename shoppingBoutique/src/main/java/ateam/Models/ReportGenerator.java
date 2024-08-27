package ateam.Models;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ReportGenerator {

    public static void generateCSV(List<Clocking> records, Writer writer) throws IOException {
       
        writer.append("Employee ID,First Name,Last Name,Company,Clock In,Clock Out,Duration (Hours)\n");

        for (Clocking clocking : records) {
           
            double durationHours = 0.0;
            if (clocking.getClock_out() != null) {
                long durationMillis = clocking.getClock_out().getTime() - clocking.getClock_in().getTime();
                durationHours = durationMillis / (1000.0 * 60 * 60);
            }

          
            writer.append(String.valueOf(clocking.getEmployee_ID()))
                  .append(',')
                  .append(clocking.getFirst_name())                  
                  .append(',')
                  .append(clocking.getLast_name())                   
                  .append(',')
                  .append(clocking.getCompany())                    
                  .append(',')
                  .append(clocking.getClock_in().toString())        
                  .append(',');

          
            if (clocking.getClock_out() != null) {
                writer.append(clocking.getClock_out().toString());
            } else {
                writer.append("N/A");
            }

            writer.append(',')
                  .append(String.format("%.2f", durationHours))   
                  .append('\n');
        }

        writer.flush();
    }
}
