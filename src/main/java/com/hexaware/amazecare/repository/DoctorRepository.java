package com.hexaware.amazecare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hexaware.amazecare.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer>{
    @Query("Select d from Doctor d where d.user.id=?1")
	Doctor getDoctorDetails(int id);

}
