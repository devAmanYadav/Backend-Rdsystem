package com.rdsystem.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.rdsystem.dto.UserpassbookDto;
import com.rdsystem.repo.rdpassbookRepo;

@Service
public class PassbookService {

	@Autowired
	private rdpassbookRepo repository;
	 public List<UserpassbookDto> getDetails(int rid) {
	 return repository.getrdpassbookAllDetail(rid);
	 }

}
