package com.mindex.challenge.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Compensation {
	private String employeeId;
	private Employee employee;
    private BigDecimal salary;
    private LocalDate effectiveDate;

    public Compensation() {
    }
    
    public String getEmployeeId() {
    	return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
    	this.employeeId = employeeId;
    }
    
    public Employee getEmployee() {
    	return employee;
    }
    
    public void setEmployee(Employee employee) {
    	this.employee = employee;
    }
    
    public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

}
