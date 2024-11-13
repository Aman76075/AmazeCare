package com.hexaware.amazecare.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.amazecare.JwtUtil;
import com.hexaware.amazecare.dto.JwtDto;
import com.hexaware.amazecare.dto.ResponseMessageDto;
import com.hexaware.amazecare.exceptions.InvalidUsernameException;
import com.hexaware.amazecare.model.Doctor;
import com.hexaware.amazecare.model.InPatient;
import com.hexaware.amazecare.model.LabOperator;
import com.hexaware.amazecare.model.OutPatient;
import com.hexaware.amazecare.model.Patient;
import com.hexaware.amazecare.model.User;
import com.hexaware.amazecare.service.DoctorService;
import com.hexaware.amazecare.service.InPatientService;
import com.hexaware.amazecare.service.LabOperatorService;
import com.hexaware.amazecare.service.OutPatientService;
import com.hexaware.amazecare.service.PatientService;
import com.hexaware.amazecare.service.UserSecurityService;
import com.hexaware.amazecare.service.UserService;

@RestController
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserSecurityService userSecurityService;
	@Autowired
	private UserService userService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private DoctorService doctorService;
	@Autowired
	private InPatientService inPatientService;
	@Autowired
	private OutPatientService outPatientService;
	@Autowired
	private LabOperatorService labOperatorService;
	
	@PostMapping("/api/token")
	public ResponseEntity<?> getToken(@RequestBody User user, JwtDto dto ) {
		try {
		Authentication auth 
				= new UsernamePasswordAuthenticationToken
							(user.getUsername(), user.getPassword());
		
		authenticationManager.authenticate(auth);
		
		/*Check if username is in DB */
		user = (User) userSecurityService.loadUserByUsername(user.getUsername());
		
		String jwt = jwtUtil.generateToken(user.getUsername());
		dto.setUsername(user.getUsername());
		dto.setToken(jwt);
		return ResponseEntity.ok(dto);
		}
		catch(AuthenticationException ae) {
			return ResponseEntity.badRequest().body(ae.getMessage());
		}
	}

	@PostMapping("/auth/sign-up/inPatient")
	public ResponseEntity<?> inPatientSignUp(@RequestBody InPatient inPatient, ResponseMessageDto dto) {
		try {
			User user=new User();
			user.setUsername(inPatient.getPatient().getUser().getUsername());
			user.setPassword(inPatient.getPatient().getUser().getPassword());
			user.setRole(inPatient.getPatient().getUser().getRole());
			user = userService.signup(user);
			
			Patient patient=new Patient();
			patient.setUser(user);
			patient.setPatientType(inPatient.getPatient().getPatientType());
			
			patient=patientService.insert(patient);
			
			inPatient.setPatient(patient);
			
			inPatient= inPatientService.insert(inPatient);	
			return ResponseEntity.ok(inPatient);

		} catch (InvalidUsernameException e) {
			dto.setMsg(e.getMessage());
			return ResponseEntity.badRequest().body(dto);
		}
	}
	@PostMapping("/auth/sign-up/outPatient")
	public ResponseEntity<?>outPatientSignup(@RequestBody OutPatient outPatient,ResponseMessageDto dto){
		try {
			User user=new User();
			user.setUsername(outPatient.getPatient().getUser().getUsername());
			user.setPassword(outPatient.getPatient().getUser().getPassword());
			user.setRole(outPatient.getPatient().getUser().getRole());
			user = userService.signup(user);
			
			Patient patient=new Patient();
			patient.setUser(user);
			patient.setPatientType(outPatient.getPatient().getPatientType());
			
			patient=patientService.insert(patient);
			
			outPatient.setPatient(patient);
			
			outPatient= outPatientService.insert(outPatient);	
			return ResponseEntity.ok(outPatient);

		} catch (InvalidUsernameException e) {
			dto.setMsg(e.getMessage());
			return ResponseEntity.badRequest().body(dto);
		}
	}
	@PostMapping("/auth/sign-up/doctor")
	public ResponseEntity<?> doctorSignUp(@RequestBody Doctor doctor, ResponseMessageDto dto) {
		try {
			User user = new User();
			user.setUsername(doctor.getUser().getUsername());
			user.setPassword(doctor.getUser().getPassword());
			user.setRole(doctor.getUser().getRole());
			user = userService.signup(user);
			doctor.setUser(user);
			doctor.setJoiningDate(LocalDate.now());
			doctor = doctorService.insert(doctor);
			return ResponseEntity.ok(doctor);

		} catch (InvalidUsernameException e) {
			dto.setMsg(e.getMessage());
			return ResponseEntity.badRequest().body(dto);
		}
	}
	
	@PostMapping("/auth/sign-up/lab-operator")
	public ResponseEntity<?> labOperatorSignUp(@RequestBody LabOperator labOperator,ResponseMessageDto dto){
		try {
		User user = new User();
		user.setUsername(labOperator.getUser().getUsername());
		user.setPassword(labOperator.getUser().getPassword());
		user.setRole(labOperator.getUser().getRole());
		user = userService.signup(user);
		labOperator.setUser(user);
		labOperator.setJoinedOn(LocalDate.now());
		labOperator=labOperatorService.getOperator(labOperator);
		return ResponseEntity.ok(labOperator);
		}catch (InvalidUsernameException e) {
			dto.setMsg(e.getMessage());
			return ResponseEntity.badRequest().body(dto);
		}
	}

	@GetMapping("/api/hello")
	public String sayHello(Principal principal) {
		String user = "";
		if(principal == null) {
			user = "TEMP_USER";
		}
		else {
			user = principal.getName();
		}
		return "api accessed by: " + user;
	}
	
	@GetMapping("/api/doctor/hello")
	public String sayHelloDoc(Principal principal) {
		String user = "";
		if(principal == null) {
			user = "TEMP_USER";
		}
		else {
			user = principal.getName();
		}
		return "api accessed by: " + user;
	}
}
