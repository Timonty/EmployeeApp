package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        
        ReportingStructure reportingStructure = getReportingStructure(id);
        return reportingStructure;
    }
	
	/**
	 * Method that returns a report structure object.
	 * 
	 * @param employeeId the employee id number
	 * @return the ReportingStructure object
	 */
	public ReportingStructure getReportingStructure(String employeeId) {
    	return new ReportingStructure(employeeRepository.findByEmployeeId(employeeId), calculateDirectEmployees(employeeId));
    }
    
	/**
	 * Recursive function that computes the number of direct employees. 
	 * 
	 * Warning: This assumes there are no circular patterns in direct reports.
	 * Infinite loop is created if there are any circular patterns in direct reports.
	 * We could cache the employee ID to prevent infinite looping.
	 * 
	 * @param employeeId the employee id number
	 * @return number of direct reports
	 */
	public int calculateDirectEmployees(String employeeId) {
		
		Employee employee = employeeRepository.findByEmployeeId(employeeId);
		int count = 0;
		
		//Check for non-null employee and direct reports
		if(employee != null && employee.getDirectReports() != null) {
			//Iterate through each direct report
			for(Employee dirReport : employee.getDirectReports()) {
				count++;
				//Find the direct report of the direct report.
				count += calculateDirectEmployees(dirReport.getEmployeeId());
			}
		}
		return count;
	}
}
