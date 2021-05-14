package com.bridgeLabz;
import java.time.LocalDate;

public class EmployeePayrollData {
    public int id;
    public String name;
    public String gender;
    public double salary;
    public LocalDate startDate;

    public EmployeePayrollData(Integer id, String name, Double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public EmployeePayrollData(int id, String name, double salary, LocalDate startDate){
        this(id,name,salary);
        this.startDate = startDate;
    }

    public EmployeePayrollData(int id, String name, String gender, double salary, LocalDate startDate) {
        this(id,name,salary,startDate);
        this.gender = gender;
    }

    @Override
    public String toString(){
        return "Emp ID:"+id+", Emp name:"+name+", Salary:"+salary;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass())  return false;
        EmployeePayrollData that = (EmployeePayrollData) obj;
        return id == that.id &&
                Double.compare(that.salary, salary) == 0 &&
                name.equals(that.name);
    }
}
