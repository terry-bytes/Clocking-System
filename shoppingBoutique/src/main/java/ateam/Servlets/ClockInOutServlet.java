package ateam.Servlets;

import ateam.Models.Employee;
import ateam.Service.ClockingService;
import ateam.ServiceImpl.ClockingServiceIMPL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ClockInOutServlet")
public class ClockInOutServlet extends HttpServlet {

    private final ClockingService clockingService;

    public ClockInOutServlet() {
        this.clockingService = new ClockingServiceIMPL();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        Employee loggedInUser = (Employee) request.getSession().getAttribute("Employee");

        if (loggedInUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int employeeId = loggedInUser.getEmployee_ID();
        String message = "";

        if ("Clock In".equals(action)) {
            if (clockingService.canClockIn(employeeId)) {
               
                if (clockingService.canClockOut(employeeId)) {
                    message = "You must clock out before you can clock in again.";
                } else {
                    clockingService.clockIn(employeeId);
                    message = "Clocked in successfully!";
                }
            } else {
                message = "You have already clocked in for the day. See you tomorrow!";
            }
        } else if ("Clock Out".equals(action)) {
            if (clockingService.canClockOut(employeeId)) {
                clockingService.clockOut(employeeId);
                message = "Clocked out successfully!";
            } else {
                message = "You cannot clock out yet. You must wait for your minimum working hours.";
            }
        } else {
            message = "Invalid action.";
        }

        request.setAttribute("message", message);
        request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
    }
}
