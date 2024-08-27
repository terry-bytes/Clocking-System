package ateam.Models;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PDFReportGenerator {

    private static final String IMAGE_URL = "https://th.bing.com/th/id/OIP.IykRiGtKFK4NFlhONnH1FwHaHa?rs=1&pid=ImgDetMain"; // Replace with your image URL

    public static void generatePDF(List<Clocking> records, ByteArrayOutputStream byteArrayOutputStream) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        
        try {
            Image headerImage = Image.getInstance(new URL(IMAGE_URL));
            headerImage.setAbsolutePosition(50, 750); 
            headerImage.scaleToFit(100, 100); 
            document.add(headerImage);
        } catch (Exception e) {
            e.printStackTrace(); 
        }

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Employee Clocking Report", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

   
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100); 
        table.setSpacingBefore(10f); 
        table.setSpacingAfter(10f);

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        table.addCell(new Paragraph("Employee ID", headerFont));
        table.addCell(new Paragraph("First Name", headerFont));
        table.addCell(new Paragraph("Last Name", headerFont));
        table.addCell(new Paragraph("Company", headerFont));
        table.addCell(new Paragraph("Clock In", headerFont));
        table.addCell(new Paragraph("Clock Out", headerFont));
        table.addCell(new Paragraph("Duration (Hours)", headerFont)); 

       
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 12);
        for (Clocking clocking : records) {
            table.addCell(new Paragraph(String.valueOf(clocking.getEmployee_ID()), contentFont));
            table.addCell(new Paragraph(clocking.getFirst_name(), contentFont)); 
            table.addCell(new Paragraph(clocking.getLast_name(), contentFont)); 
            table.addCell(new Paragraph(clocking.getCompany(), contentFont));   
            table.addCell(new Paragraph(clocking.getClock_in().toString(), contentFont));

            if (clocking.getClock_out() != null) {
                table.addCell(new Paragraph(clocking.getClock_out().toString(), contentFont));
                long durationMillis = clocking.getClock_out().getTime() - clocking.getClock_in().getTime();
                double durationHours = durationMillis / (1000.0 * 60 * 60); 
                table.addCell(new Paragraph(String.format("%.2f", durationHours) + " hrs", contentFont));
            } else {
                table.addCell(new Paragraph("N/A", contentFont)); 
                table.addCell(new Paragraph("N/A", contentFont)); 
            }
        }

        document.add(table);
        document.close();
    }
}
