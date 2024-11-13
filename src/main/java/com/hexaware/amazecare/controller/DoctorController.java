package com.hexaware.amazecare.controller;


import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.amazecare.dto.AppointmentDto;
import com.hexaware.amazecare.dto.ResponseMessageDto;
import com.hexaware.amazecare.enums.TestScanStatus;
import com.hexaware.amazecare.exceptions.ResourceNotFoundException;
import com.hexaware.amazecare.model.Appointment;
import com.hexaware.amazecare.model.Doctor;
import com.hexaware.amazecare.model.MedicalRecord;
import com.hexaware.amazecare.model.TestAndScans;
import com.hexaware.amazecare.model.User;
import com.hexaware.amazecare.service.DoctorService;
import com.hexaware.amazecare.service.MedicalRecordService;
import com.hexaware.amazecare.service.TestAndScansService;
import com.hexaware.amazecare.service.UserService;

@RestController
public class DoctorController {
	@Autowired
	private DoctorService doctorService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private MedicalRecordService medicalRecordService;
	
	@Autowired
	private TestAndScansService testAndScansService;
	
	@PostMapping("/doctor/register")
	public Doctor registerDoctor(@RequestBody Doctor doctor) {
		User user=new User();
		user.setUsername(doctor.getUser().getUsername());
		user.setPassword(doctor.getUser().getPassword());
		user.setRole(doctor.getUser().getRole());
		user=userService.insert(user);
		doctor.setUser(user);
		doctor.setJoiningDate(LocalDate.now());
		
		return doctorService.insert(doctor);
	}
	@GetMapping("/doctor/viewAppointment/{did}")
	public List<Appointment> viewAllAppointment(@PathVariable int did){
		return doctorService.getAllAppointments(did);
	}
	@PostMapping("/doctor/generateMedicalRecord/{did}")
	public ResponseEntity<?> generateRecord(@PathVariable int did,@RequestBody MedicalRecord medicalRecord,ResponseMessageDto dto) {
	    Doctor doctor=null;
	    try {
	    	doctor=doctorService.validate(did);
	    }catch(ResourceNotFoundException e) {
	    	 dto.setMsg(e.getMessage());
			 return ResponseEntity.badRequest().body(dto);
	    }
	    medicalRecord.setDoctor(doctor);
	    medicalRecord.setGenerationDate(LocalDate.now());
	    medicalRecord=medicalRecordService.insert(medicalRecord);
	    return ResponseEntity.ok(medicalRecord);
	    
	}
	@PostMapping("doctor/testnscans/{did}")
	public ResponseEntity<?>getTestnScans(@PathVariable int did,@RequestBody TestAndScans testAndScans,ResponseMessageDto dto){
		Doctor doctor=null;
	    try {
	    	doctor=doctorService.validate(did);
	    }catch(ResourceNotFoundException e) {
	    	 dto.setMsg(e.getMessage());
			 return ResponseEntity.badRequest().body(dto);
	    }
	    testAndScans.setDoctor(doctor);
	    testAndScans.setStatus(TestScanStatus.PENDING);
	    testAndScans.setTestOrScanPrescibedon(LocalDate.now());
	    testAndScans=testAndScansService.insert(testAndScans);
	    return ResponseEntity.ok(testAndScans);
	}
	@GetMapping("/doctor/appointment/getAll/{did}")
	public List<AppointmentDto> fetchAllAppointments(@PathVariable int did){
		List<AppointmentDto> list=doctorService.fetchAllAppointments(did);
		return list;
	}
}
