package com.hexaware.amazecare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.amazecare.dto.ReportDetailsDto;
import com.hexaware.amazecare.dto.ResponseMessageDto;
import com.hexaware.amazecare.exceptions.ResourceNotFoundException;
import com.hexaware.amazecare.service.PatientService;
import com.hexaware.amazecare.service.ReportService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class ReportController {
	@Autowired
	private PatientService patientService;
	@Autowired
	private ReportService reportService;
	
	@GetMapping("reports/fetch/{pid}")
	public ResponseEntity<?> fetchReports(@PathVariable int pid,ResponseMessageDto dto) throws ResourceNotFoundException{
		patientService.validate(pid);
		List<ReportDetailsDto>list=reportService.fetchReport(pid);
		return ResponseEntity.ok(list);
		
	}
	

}
