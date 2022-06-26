package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

	private String employeeUrl;
	private String reportingStructureIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
    	employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureIdUrl = "http://localhost:" + port + "/reportingstructure/{id}";
    }

    /**
     * Create multiple Employees, and set up a direct report tree.
     * Test that the number of direct reports is properly generated in ReportingStructure object
     * 
     * Direct Reports Structure:
     * Employee 1 <- Employee 2 and Employee 3
     * Employee 3 <- Employee 4 and Employee 5
     * 
     */
    @Test
    public void testCreateReadUpdate() {

    	//Create 5 employees
    	Employee testEmployee1 = new Employee();
    	Employee testEmployee2 = new Employee();
    	Employee testEmployee3 = new Employee();
    	Employee testEmployee4 = new Employee();
    	Employee testEmployee5 = new Employee();
    	
    	//Put employee 4 and 5 in DB. This will generate an Employee Id for them that we need.
    	Employee createdEmployee4 = restTemplate.postForEntity(employeeUrl, testEmployee4, Employee.class).getBody();
    	Employee createdEmployee5 = restTemplate.postForEntity(employeeUrl, testEmployee5, Employee.class).getBody();
    	
    	//Create a list of direct reports for employee 3
    	List<Employee> directReportsForTestEmployee3 = new ArrayList<>();
    	directReportsForTestEmployee3.add(createdEmployee4);
    	directReportsForTestEmployee3.add(createdEmployee5);
    	testEmployee3.setDirectReports(directReportsForTestEmployee3);
    	
    	//Add employee 2 and 3 in DB. This will generate an Employee Id for them that we need.
    	Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();
    	Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
    	
    	//Now we create a list of direct reports for employee 1
    	List<Employee> directReportsForTestEmployee1 = new ArrayList<>();
    	directReportsForTestEmployee1.add(createdEmployee2);
    	directReportsForTestEmployee1.add(createdEmployee3);
    	testEmployee1.setDirectReports(directReportsForTestEmployee1);
    	
    	//Store employee 1 into DB.
    	Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();
    	
    	//Get reporting structure object, assert number of reports is equal to 4
    	ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, createdEmployee1.getEmployeeId()).getBody();
    	assertEquals(reportingStructure.getNumberOfReports(), 4);
    }
}
