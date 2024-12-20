package com.hexaware.amazecare;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hexaware.amazecare.dto.ResponseMessageDto;
import com.hexaware.amazecare.exceptions.ResourceNotFoundException;
import com.hexaware.amazecare.service.LabOperatorService;


@ControllerAdvice
public class GlobalExceptionHandler {
@Autowired
private ResponseMessageDto dto; 
Logger logger = LoggerFactory.getLogger(LabOperatorService.class);
@ExceptionHandler(ResourceNotFoundException.class)
ResponseEntity<?> handleResourceNotFoundException(Exception e){
	dto.setMsg(e.getMessage());
	logger.error("ResourceNotFoundException thrown " + dto.getMsg());
	return ResponseEntity.badRequest().body(dto);}

@ExceptionHandler(IOException.class)
public ResponseEntity<?> handleIOException(IOException e) {
	dto.setMsg(e.getMessage());
	return ResponseEntity.badRequest().body(dto);
}
}
