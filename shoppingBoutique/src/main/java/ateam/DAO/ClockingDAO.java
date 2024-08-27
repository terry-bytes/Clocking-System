/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.DAO;

import ateam.Models.Clocking;
import ateam.Models.Employee;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 *
 * @author user pc
 */
public interface ClockingDAO {

    void clockIn(int employee_ID);

    void clockOut(int employee_ID);

    Clocking getClockingByEmployeeId(int employeeId);

    public List<Clocking> getFilteredClockingRecords(Timestamp startDate, Timestamp endDate, String company);

    List<Clocking> getFilteredClockingRecords(String firstName, String lastName, Timestamp startDate, Timestamp endDate, String company);
}
