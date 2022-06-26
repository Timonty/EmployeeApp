package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String compensationUrl;
    private String compensationIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    /**
     * Create an employee and compensation object to store into DB.
     * Test that the compensation and employee fields match.
     */
    @Test
    public void testCompensationCreateRead() {
    	//Create an employee, store in DB
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        
        //Create compensation for the employee, store in DB
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId(createdEmployee.getEmployeeId());
        testCompensation.setEffectiveDate(LocalDate.now());
        testCompensation.setSalary(new BigDecimal(1000));
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();

        //Assert that employee field is not null for compensation
        assertNotNull(createdCompensation.getEmployee());

        // Read checks, assert all field values match as expected
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployeeId()).getBody();
        assertCompensationEquivalence(testCompensation, readCompensation);
        assertEmployeeEquivalence(createdEmployee, readCompensation.getEmployee());
    }

    /**
     * Tests that the compensation fields are equivalent
     * @param expected
     * @param actual
     */
    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
    }
    
    /**
     * Tests that the employee fields are equivalent
     * @param expected
     * @param actual
     */
    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
    	assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
    	assertEquals(expected.getFirstName(), actual.getFirstName());
    	assertEquals(expected.getLastName(), actual.getLastName());
    	assertEquals(expected.getDepartment(), actual.getDepartment());
    	assertEquals(expected.getPosition(), actual.getPosition());
    }
}
