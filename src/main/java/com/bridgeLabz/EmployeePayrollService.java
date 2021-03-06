package com.bridgeLabz;
import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollService {

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }
    private List<EmployeePayrollData> employeePayrollList;
    private static EmployeePayrollDBService employeePayrollDBService;//Singleton

    public EmployeePayrollService(){
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }
    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
    }
    private void readEmployeePayrollData(Scanner consoleInputReader){
        System.out.println("Enter Employee ID:");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee Name:");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee Salary:");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new EmployeePayrollData(id,name,salary));
    }
    //UC2 -Retrieve data
    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService){
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }

    //UC3 Update salary
    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollList.get(0).equals(getEmployeePayrollData(name));
    }

    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayrollDBService.updateEmployeeData(name,salary);
        if(result == 0) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if(employeePayrollData != null) employeePayrollData.salary = salary;
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    //UC5
    public List<EmployeePayrollData> readEmployeePayrollForDataRange(IOService ioService, LocalDate startDate, LocalDate endDate) {
        if(ioService.equals(IOService.DB_IO))
            return employeePayrollDBService.getEmployeePayrollForDateRange(startDate, endDate);
        return null;
    }
    public void writeEmployeePayrollData(IOService ioService){
        if(ioService.equals(IOService.CONSOLE_IO)) {
            System.out.println("\nWriting Employee Payroll Roaster to Console\n" +employeePayrollList);
        }else if(ioService.equals(IOService.FILE_IO)){
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
        }
    }

    //UC6 Find Average salary by gender
    public Map<String, Double> readAverageSalaryByGender(IOService ioService) {
        if(ioService.equals(IOService.DB_IO))
            return employeePayrollDBService.getEmployeeAverageSalaryByGender();
        return null;
    }
    public void printData(IOService ioService) {
        if(ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().printData();
        else System.out.println(employeePayrollList);
    }

    //UC7 Add new Employee to DB table
    public void addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
        employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name, salary, startDate, gender));
    }

    //UC1 without Threading
    public void addEmployeesToPayroll(List<EmployeePayrollData> employeePayrollDataList) {
        employeePayrollDataList.forEach(employeePayrollData -> {
            System.out.println("Employee being Added = "+employeePayrollData.name);
            this.addEmployeeToPayroll(employeePayrollData.name, employeePayrollData.salary,
                                      employeePayrollData.startDate, employeePayrollData.gender);
            System.out.println("Employee Added -> "+employeePayrollData.name);
        });
        System.out.println(this.employeePayrollList);
    }

    //UC2 - with thread
    public void addEmployeesToPayrollWithThread(List<EmployeePayrollData> employeePayrollDataList) {
        Map<Integer,Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
        employeePayrollDataList.forEach(employeePayrollData -> {
            Runnable task = () -> {
                employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
                System.out.println("Employee being Added = "+Thread.currentThread().getName());
                this.addEmployeeToPayroll(employeePayrollData.name, employeePayrollData.salary,
                        employeePayrollData.startDate, employeePayrollData.gender);
                employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
                System.out.println("Employee Added -> "+Thread.currentThread().getName());
            };
            Thread thread =new Thread(task, employeePayrollData.name);
            thread.start();
        });
        while(employeeAdditionStatus.containsValue(false)) {
            try {
                Thread.sleep(10);
            }catch (InterruptedException e){ }
        }
        System.out.println(employeePayrollDataList);
    }

    public long countEntries(IOService ioService) {
        if(ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return employeePayrollList.size();
    }
}

