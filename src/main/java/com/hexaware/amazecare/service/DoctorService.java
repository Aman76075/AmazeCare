package com.hexaware.amazecare.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.amazecare.dto.AppointmentDto;
import com.hexaware.amazecare.enums.Appointment_Status;
import com.hexaware.amazecare.exceptions.ResourceNotFoundException;
import com.hexaware.amazecare.model.Appointment;
import com.hexaware.amazecare.model.Doctor;
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
			throw new ResourceNotFoundException("Invalid Patient Id Given");
		}
		Doctor doctor=optional.get();
		return doctor;
	}

	public List<Appointment> getAllAppointments(int did) {
		Appointment_Status as=Appointment_Status.valueOf("BOOKED");
		return appointmentRepository.getAllAppointmentsByDoctor(as,did);
	}

	public List<AppointmentDto> fetchAllAppointments(int did) {
		Appointment_Status as=Appointment_Status.valueOf("BOOKED");
		List<Object[]> listObjArray =appointmentRepository.fetchAllAppointments(as,did);
		List<AppointmentDto> list=new ArrayList<>();
		for( Object[] obj : listObjArray) {
			String name=(String)obj[0];
			int age=(int)obj[1];
			String gender=(String)obj[2];
			String patient_type=obj[3].toString();
			LocalDate date=(LocalDate)obj[4];
			String timeSlot=obj[5].toString();
			String status=obj[6].toString();
		    AppointmentDto dto=new AppointmentDto();
		    dto.setName(name);
		    dto.setAge(age);
		    dto.setGender(gender);
		    dto.setPatient_type(patient_type);
		    dto.setDate(date);
		    dto.setTimeSlot(timeSlot);
		    dto.setStatus(status);
		    list.add(dto);
		}
		return list;		
	}

}
