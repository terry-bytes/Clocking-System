package ateam.Servlets;

import ateam.DAO.EmployeeDAO;
import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.Models.Email;
import ateam.Models.Employee;
import ateam.Models.Role;
import ateam.Service.EmailService;
import ateam.Service.EmployeeService;
import ateam.ServiceImpl.EmailServiceImpl;
import ateam.ServiceImpl.EmployeeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "VerifyOTPServlet", urlPatterns = "/verifyOTP")
public class VerifyOTPServlet extends HttpServlet {
    
    private final EmployeeService employeeService;
    private final EmailService emailService;

    public VerifyOTPServlet() {
        try {
            EmployeeDAO employeeDAO = new EmployeeDAOIMPL();
            this.employeeService = new EmployeeServiceImpl(employeeDAO);
            this.emailService = new EmailServiceImpl();
        } catch (Exception e) {
            Logger.getLogger(EmployeeServlet.class.getName()).log(Level.SEVERE, "Error initializing EmployeeService", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inputOtp = request.getParameter("otp");
        String generatedOtp = (String) request.getSession().getAttribute("otp");
        Employee newEmployee = (Employee) request.getSession().getAttribute("newEmployee");
        Employee manager = (Employee) request.getSession(false).getAttribute("Employee");
        boolean success = employeeService.addEmployee(newEmployee);

        if (inputOtp.equals(generatedOtp)) {
            if (success) {
                Employee employee = employeeService.findByEmail(newEmployee.getEmail());

                String msg = "Dear " + newEmployee.getFirst_name() + " " + newEmployee.getLast_name()+ ",\nWelcome aboard! Your employee ID is " + employee.getEmployees_id()
                        + ". We're excited to have you on our team.\n\n\n\nBest regards,\n"
                        + manager.getFirst_name() + " " + manager.getLast_name() + "\n"
                        + manager.getRole() + "\n"
                        + "Carols Boutique\n";

                Email emailDetails = new Email("ramovhatp@gmail.com", "xaed clmt qpis ctvf");
                emailDetails.setReceiver(newEmployee.getEmail());
                emailDetails.setSubject("Welcome to Carols Boutique! Your Registration is Complete");
                emailDetails.setMessage(msg);

                emailService.sendMail(emailDetails);
                request.setAttribute("addEmployeeMessage", "Employee added successfully");
            }
            if (manager != null) {
                if (manager.getRole() == Role.Manager) {
                    request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("managerAddEmployee.jsp").forward(request, response);
                }
            } else {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("otpMessage", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("/verifyOTP.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/verifyOTP.jsp").forward(request, response);
    }
}
