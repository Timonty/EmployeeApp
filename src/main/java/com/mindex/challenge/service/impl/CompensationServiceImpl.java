package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
	public Compensation create(Compensation compensation) {
		LOG.debug("Creating compensation for [{}]", compensation.getEmployeeId());

		//Look for the employee in the DB to see if it exists.
		String employeeId = compensation.getEmployeeId();
		Employee employee = employeeRepository.findByEmployeeId(employeeId);
		if (employee == null) {
			throw new RuntimeException("Invalid employeeId: " + employeeId);
		}
		
		//Set the employee property in the compensation object
		compensation.setEmployee(employee);
        compensationRepository.insert(compensation);

        return compensation;
	}

	@Override
	public Compensation read(String id) {
		
		Compensation compensation = compensationRepository.findByEmployeeId(id);

		if(compensation == null) {
			throw new RuntimeException("No compensation exists for " + id);
		}
		
		return compensation;
		
	}
}
