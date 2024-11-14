package com.hexaware.amazecare.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.amazecare.model.Doctor;
import com.hexaware.amazecare.service.DoctorService;
import com.hexaware.amazecare.service.ExecutiveService;

@RestController
public class ExecutiveController {
	@Autowired
	private ExecutiveService executiveService;
	@Autowired
	private DoctorService doctorService;
	Logger logger = LoggerFactory.getLogger(ExecutiveController.class);
	
	@GetMapping("/api/doctor/all")
	public Page<Doctor> getAllDoctor(
			@RequestParam(required = false, defaultValue = "0") int page, 
			@RequestParam(required = false, defaultValue = "5000") int size,Principal principal) {
		logger.info("API Accessed by "+principal.getName());
		Pageable pageable =  PageRequest.of(page, size);
		logger.info("Fetching all inpatient using pageable...");
		if(size==5000)
			logger.warn("Fetching inpatient without limit ");
		return doctorService.getAllDoctor(pageable);
	}

}
