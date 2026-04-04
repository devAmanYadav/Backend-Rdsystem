package com.rdsystem.dto;

import java.time.LocalDate;

import jakarta.persistence.Id;

public interface UserpassbookDto {
	@Id
	Integer getRid();
	String getName();
	 String getAcno();
	 Double getRdamt();
	 LocalDate getRddate();
}
