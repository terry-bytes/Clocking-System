package ateam.Servlets;

import ateam.Models.Clocking;
import ateam.DAO.ClockingDAO;
import ateam.DAOIMPL.ClockingDAOIMPL;
import ateam.Models.PDFReportGenerator;
import ateam.Models.ReportGenerator;
import com.itextpdf.text.DocumentException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "ReportServlet", urlPatterns = {"/ReportServlet"})
public class ReportServlet extends HttpServlet {

    private ClockingDAO clockingDao = new ClockingDAOIMPL(); 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String startDateStr = request.getParameter("start_date");
        String endDateStr = request.getParameter("end_date");
        String company = request.getParameter("company");

      
        String startDateTime = startDateStr + " 00:00:00";
        String endDateTime = endDateStr + " 23:59:59";
        Timestamp startDate = Timestamp.valueOf(startDateTime);
        Timestamp endDate = Timestamp.valueOf(endDateTime);

        List<Clocking> records;

       
        if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) {
           
            records = clockingDao.getFilteredClockingRecords(firstName, lastName, startDate, endDate, company);
        } else {
            
            records = clockingDao.getFilteredClockingRecords(startDate, endDate, company);
        }

        System.out.println("Number of records fetched: " + records.size());

        try {
            String format = request.getParameter("format");

            if ("csv".equalsIgnoreCase(format)) {
               
                response.setContentType("text/csv");
                response.setHeader("Content-Disposition", "attachment; filename=report.csv");

                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                     Writer writer = new OutputStreamWriter(byteArrayOutputStream)) {
                    ReportGenerator.generateCSV(records, writer);
                    writer.flush();

                   
                    response.getOutputStream().write(byteArrayOutputStream.toByteArray());
                    response.getOutputStream().flush();
                }
            } else if ("pdf".equalsIgnoreCase(format)) {
               
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=report.pdf");

                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                    PDFReportGenerator.generatePDF(records, byteArrayOutputStream);

                   
                    response.getOutputStream().write(byteArrayOutputStream.toByteArray());
                    response.getOutputStream().flush();
                } catch (DocumentException e) {
                    throw new ServletException("Error generating PDF", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); 
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating report");
        }
    }
}
