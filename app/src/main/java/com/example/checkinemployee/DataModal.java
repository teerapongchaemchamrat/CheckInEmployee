package com.example.checkinemployee;

public class DataModal {
    private String employeeID;
    private String employeeName;
    private String Department;
    private String Location;

    public DataModal(String employeeID, String employeeName, String Department, String Location){
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.Department = Department;
        this.Location = Location;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String Department) {
        this.Department = Department;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }
}
