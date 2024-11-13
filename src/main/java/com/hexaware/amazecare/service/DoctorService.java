package com.hexaware.amazecare.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hexaware.amazecare.enums.Appointment_Status;
import com.hexaware.amazecare.enums.Department;
import com.hexaware.amazecare.exceptions.ResourceNotFoundException;
import com.hexaware.amazecare.model.Appointment;
import com.hexaware.amazecare.model.Doctor;
import com.hexaware.amazecare.model.OutPatient;
import com.hexaware.amazecare.repository.AppointmentRepository;
import com.hexaware.amazecare.repository.DoctorRepository;

@Service
public class DoctorService {
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;

	public Doctor insert(Doctor doctor) {
		return doctorRepository.save(doctor);
	}

	public Doctor validate(int docId) throws ResourceNotFoundException {
		Optional<Doctor>optional=doctorRepository.findById(docId);
		if(optional.isEmpty()) {
			throw new ResourceNotFoundException("Invalid Id Given");
		}
		Doctor doctor=optional.get();
		return doctor;
	}

	public List<Appointment> getAllAppointments(int did) {
		Appointment_Status as=Appointment_Status.valueOf("BOOKED");
		return appointmentRepository.getAllAppointmentsByDoctor(as,did);
	}
	
	public void uploadDoctorThruExcel(MultipartFile file) throws IOException {
	    // Step 1: Convert file into InputStream
	    InputStream inputStream = file.getInputStream();

	    // Step 2: Give this inputStream to BufferedReader
	    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

	    // Step 3: Read the content line by line using loop and save them in Doctor objects
	    br.readLine(); // This reads the header line, which we ignore
	    String data;

	    List<Doctor> doctorList = new ArrayList<>();
	    while ((data = br.readLine()) != null) {
	        Doctor doctor = new Doctor();
	        String[] str = data.split(",");

	        doctor.setName(str[0]);
	        doctor.setEmail(str[1]);
	        doctor.setContact(str[2]);
	        doctor.setExperience(Integer.parseInt(str[3]));
	        doctor.setJoiningDate(LocalDate.parse(str[4]));
	        doctor.setDepartment(Department.valueOf(str[5].toUpperCase()));

	        // Generate Doctor ID
	        int id = (int) (Math.random() * 10000000);
	        doctor.setId(id);

	        doctorList.add(doctor);
	    }

	    // Step 5: Save Doctor in batch
	    doctorRepository.saveAll(doctorList);
	}
	
	public void deleteDoctorById(int id) {
		doctorRepository.deleteById(id);
		
	}

	public Page<Doctor> getAllDoctor(Pageable pageable) {
		return doctorRepository.findAll(pageable);
	}


}
