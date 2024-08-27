package ateam.Servlets;

import ateam.DAO.EmployeeDAO;
import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.Exception.EmployeeNotFoundException;
import ateam.Exception.InvalidPasswordException;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "EmployeeServlet", urlPatterns = "/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {

    private final EmployeeService employeeService;
    private final EmailService emailService;

    public EmployeeServlet() {
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
        String action = request.getParameter("submit");

        switch (action) {
            case "add":
                addEmployee(request, response);
                break;
            case "update":
                updateEmployee(request, response);
                break;
            case "delete":
                deleteEmployee(request, response);
                break;
            case "login":
                handleLogin(request, response);
                break;
            case "verify":
                verifyOTP(request, response);
                break;
            case "forgotPassword":
                handleForgotPassword(request, response);
                break;
            case "changePassword":
                changePassword(request, response);
                break;
            case "verifyResetOTP":
                verifyResetOTP(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/employees");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("submit");
        HttpSession session = request.getSession(false);

        switch (action) {
            case "edit":
                showEditForm(request, response);
                break;
            case "addForm":
                showAddForm(request, response);
                break;
            case "deleteConfirm":
                showDeleteConfirm(request, response);
                break;
            case "myEmployees":
                listEmployees(request, response);
                break;
            case "logout":
                session.invalidate();
                request.getRequestDispatcher("login.jsp").forward(request, response);
                break;
            default:
                listEmployees(request, response);
                break;
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Employee> employees = employeeService.getAllEmployees();
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("/listEmployees.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/addEmployee.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        Employee employee = employeeService.getEmployeeById(employeeId);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("editEmployee.jsp").forward(request, response);
    }

    private void showDeleteConfirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        Employee employee = employeeService.getEmployeeById(employeeId);
        request.setAttribute("employeeList", employee);
        request.getRequestDispatcher("MyEmployees.jsp").forward(request, response);
    }

    private void addEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Role role = Role.valueOf(request.getParameter("role"));
        String companyName = request.getParameter("company");
        int minHours = Integer.parseInt(request.getParameter("min_hours"));

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Employee newEmployee = new Employee();
        newEmployee.setFirst_name(firstName);
        newEmployee.setLast_name(lastName);
        newEmployee.setEmail(email);
        newEmployee.setEmployee_password(hashedPassword);
        newEmployee.setRole(role);
        newEmployee.setCompany(companyName);
        newEmployee.setMin_hours(minHours);

        boolean isAdded = employeeService.addEmployee(newEmployee);
        if (isAdded) {
            String subject = "Welcome to the VMC MEDIA";
            String message = "Dear " + firstName + ",\n\n"
                    + "Your employee account has been successfully created.\n"
                    + "Employee ID: " + newEmployee.getEmployees_id() + "\n" 
                    + "Password: " + password + "\n\n"
                    + "Please log in using the provided credentials.\n\n"
                    + "Best regards,\n"
                    + "VMC MEDIA";

            Email emailDetails = new Email("your email", "your password");
            emailDetails.setReceiver(email);
            emailDetails.setSubject(subject);
            emailDetails.setMessage(message);

            emailService.sendMail(emailDetails);

            request.getSession().setAttribute("successMessage", "Employee added successfully and email sent!");

           
            String otp = generateOTP();
            Email otpEmailDetails = new Email("your email", "your password");
            otpEmailDetails.setReceiver(email);
            otpEmailDetails.setSubject("Email Verification OTP");
            otpEmailDetails.setMessage("Your OTP for email verification is: " + otp);

            emailService.sendMail(otpEmailDetails);
            request.getSession().setAttribute("otp", otp);
            request.getSession().setAttribute("newEmployee", newEmployee);

            response.sendRedirect(request.getContextPath() + "/verifyOTP.jsp");
        } else {
            Logger.getLogger(EmployeeServlet.class.getName()).log(Level.SEVERE, "Failed to add employee.");
            request.getSession().setAttribute("errorMessage", "Failed to add employee.");
        }
    }

    private String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Role role = Role.valueOf(request.getParameter("role"));
        String companyName = request.getParameter("company");
        int minHours = Integer.parseInt(request.getParameter("min_hours"));

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Employee employeeToUpdate = new Employee();
        employeeToUpdate.setEmployee_ID(employeeId);
        employeeToUpdate.setFirst_name(firstName);
        employeeToUpdate.setLast_name(lastName);
        employeeToUpdate.setEmail(email);
        employeeToUpdate.setEmployee_password(hashedPassword);
        employeeToUpdate.setRole(role);
        employeeToUpdate.setCompany(companyName);
        employeeToUpdate.setMin_hours(minHours);

        boolean success = employeeService.updateEmployee(employeeToUpdate);
        String message = null;
        if (success) {
            message = "Employee details are updated successfully";
        } else {
            message = "Failed to update employee's details";
        }
        request.setAttribute("message", message);
        request.getRequestDispatcher("editEmployee.jsp").forward(request, response);
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        boolean success = employeeService.deleteEmployee(employeeId);
        Employee manager = (Employee) request.getSession(false).getAttribute("Employee");

        if (success) {
            request.setAttribute("message", "Employee deleted successfully");
        } else {
            request.setAttribute("message", "Failed to delete employee");
        }
        request.setAttribute("employeeList", employeeService.getAllEmployees());
        if (manager != null) {
            if (manager.getRole() == Role.Manager) {
                request.getRequestDispatcher("listEmployee.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("addEmployees.jsp").forward(request, response);
            }
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
         
            String employeeId = request.getParameter("employeeId");
            String password = request.getParameter("password");

          
            Employee employee = employeeService.login(employeeId, password);

            
            if (employee != null) {
               
                HttpSession session = request.getSession(true);
                session.setAttribute("Employee", employee);

                switch (employee.getRole()) {
                    case Admin:
                        response.sendRedirect("AdminServlet");
                        break;
                    case Manager:
                        response.sendRedirect("addEmployee.jsp");
                        break;
                    default:
                        response.sendRedirect("tellerDashboard.jsp");
                        break;
                }
            } else {
               
                request.setAttribute("message", "Failed to login");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (EmployeeNotFoundException | InvalidPasswordException ex) {
           
            request.setAttribute("message", "You have entered an incorrect password ");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception ex) {
          
            ex.printStackTrace(); 
            request.setAttribute("message", "invalid employee ID");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String otp = generateOTP();

        Email emailDetails = new Email("your email", "your password");
        emailDetails.setReceiver(email);
        emailDetails.setSubject("Password Reset OTP");
        emailDetails.setMessage("Your OTP for password reset is: " + otp);

        emailService.sendMail(emailDetails);
        request.getSession().setAttribute("resetOtp", otp);
        request.getSession().setAttribute("resetEmail", email);
        response.sendRedirect(request.getContextPath() + "/verifyResetOTP.jsp");
    }

    private void verifyOTP(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String inputOtp = request.getParameter("otp");
        String generatedOtp = (String) request.getSession().getAttribute("otp");

        if (generatedOtp != null && generatedOtp.equals(inputOtp)) {
            Employee newEmployee = (Employee) request.getSession().getAttribute("newEmployee");

            if (employeeService.addEmployee(newEmployee)) {
                request.setAttribute("message", "Employee added successfully");
                response.sendRedirect("AdminDashboard.jsp");
            } else {
                request.setAttribute("message", "Failed to add employee");
                request.getRequestDispatcher("/addEmployee.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("message", "OTP verification failed. Please try again.");
            request.getRequestDispatcher("/verifyOTP.jsp").forward(request, response);
        }
    }

    private void verifyResetOTP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inputOtp = request.getParameter("otp");
        String generatedOtp = (String) request.getSession().getAttribute("resetOtp");
        String email = (String) request.getSession().getAttribute("resetEmail");

        if (generatedOtp != null && generatedOtp.equals(inputOtp)) {
            request.getSession().setAttribute("email", email);
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
        } else {
            request.setAttribute("message", "OTP verification failed. Please try again.");
            request.getRequestDispatcher("verifyResetOTP.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = (String) request.getSession().getAttribute("email");

        if (newPassword.equals(confirmPassword)) {
          
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

          
            boolean success = employeeService.updatePasswordByEmail(email, hashedPassword);
            if (success) {
                request.getSession().invalidate();
                request.setAttribute("message", "Password has been reset successfully. Please login with your new password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "Failed to reset password. Please try again.");
                request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("message", "Passwords do not match. Please try again.");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
        }
    }

    private void handleGetMyEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("MyEmployees.jsp").forward(request, response);
    }
}
