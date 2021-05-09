package com.bridgaLabz;
import com.bridgeLabz.EmployeePayrollData;
import com.bridgeLabz.EmployeePayrollService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bridgeLabz.EmployeePayrollService.IOService.DB_IO;
import static com.bridgeLabz.EmployeePayrollService.IOService.FILE_IO;

public class EmployeePayrollServiceTest {
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries(){
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1,"Jeff Bezos",1000000.0),
                new EmployeePayrollData(2, "Bill Gates",2000000.0),
                new EmployeePayrollData(3,"Mark Zuckerberg",3000000.0)
        };
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmployeePayrollData(FILE_IO);
        employeePayrollService.printData(FILE_IO);
        long entries = employeePayrollService.countEntries(FILE_IO);
        Assertions.assertEquals(3, entries);
    }
    //UC2 Retrieve data of employee_payroll table
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assertions.assertEquals(3,employeePayrollData.size());
    }
    //UC3 Update Salary from employee_payroll table and UC4 Singleton class
    @Test
    public void giveNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assertions.assertTrue(result);
    }
    //UC5 -Retrieve particular data
    @Test
    public void givenDataRange_When_Retrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate = LocalDate.of(2018,01,01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollForDataRange(DB_IO, startDate, endDate);
        Assertions.assertEquals(3,employeePayrollData.size());
    }
    //UC6 DataBase Functions.
    @Test
    public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        Assertions.assertTrue(averageSalaryByGender.get("M").equals(2000000.00) &&
                               averageSalaryByGender.get("F").equals(3000000.00));
    }
}
