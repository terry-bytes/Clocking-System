package ateam.Service;

import ateam.Models.Clocking;

public interface ClockingService {

    void clockIn(int employee_ID);

    void clockOut(int employee_ID);

    Clocking getClockingByEmployeeId(int employee_ID);

    boolean canClockIn(int employee_ID);

    boolean canClockOut(int employee_ID);
}
