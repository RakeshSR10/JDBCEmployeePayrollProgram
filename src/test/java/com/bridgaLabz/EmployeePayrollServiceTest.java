package com.bridgaLabz;
import com.bridgeLabz.EmployeePayrollData;
import com.bridgeLabz.EmployeePayrollService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

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
    //UC3 Update Salary from employee_payroll table
    @Test
    public void giveNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assertions.assertTrue(result);
    }
}
