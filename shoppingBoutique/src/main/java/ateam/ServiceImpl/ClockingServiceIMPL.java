package ateam.ServiceImpl;

import ateam.DAO.ClockingDAO;
import ateam.DAO.EmployeeDAO;
import ateam.DAOIMPL.ClockingDAOIMPL;
import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.Models.Clocking;
import ateam.Models.Employee;
import ateam.Service.ClockingService;

public class ClockingServiceIMPL implements ClockingService {

    private final ClockingDAO clockingDAO;
    private final EmployeeDAO employeeDAO;

    public ClockingServiceIMPL() {
        this.clockingDAO = new ClockingDAOIMPL();
        this.employeeDAO = new EmployeeDAOIMPL();
    }

    @Override
    public void clockIn(int employeeId) {
        clockingDAO.clockIn(employeeId);
    }

    @Override
    public void clockOut(int employeeId) {
        clockingDAO.clockOut(employeeId);
    }

    @Override
    public Clocking getClockingByEmployeeId(int employeeId) {
        return clockingDAO.getClockingByEmployeeId(employeeId);
    }

    @Override
    public boolean canClockIn(int employeeId) {
        Clocking clocking = clockingDAO.getClockingByEmployeeId(employeeId);
        if (clocking == null || clocking.getClock_out() == null) {
            return true; // Can clock in if no record exists or if not clocked out yet
        } else {
            long clockOutTime = clocking.getClock_out().getTime();
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - clockOutTime;

            // Check if 2 minutes have passed since the last clock-out
            long requiredTimeDifference = 2 * 60 * 1000;

            return timeDifference >= requiredTimeDifference;
        }
    }

    @Override
    public boolean canClockOut(int employeeId) {
        Clocking clocking = clockingDAO.getClockingByEmployeeId(employeeId);
        Employee employee = employeeDAO.getEmployeeById(employeeId);// Assume you have this method to fetch employee details

        if (clocking != null && clocking.getClock_out() == null) {
            // Calculate the time difference between now and the clock-in time
            long clockInTime = clocking.getClock_in().getTime();
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - clockInTime;

            // Convert min_hours to milliseconds
            long requiredTimeDifference = employee.getMin_hours() * 60 * 60 * 1000;

            if (timeDifference >= requiredTimeDifference) {
                return true; // Can clock out if enough time has passed
            } else {
                return false; // Not enough time has passed
            }
        }
        return false; // Cannot clock out if not clocked in or already clocked out
    }

}
