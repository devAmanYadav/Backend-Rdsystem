package com.rdsystem.repo;



import org.springframework.data.jpa.repository.JpaRepository;

import com.rdsystem.entity.rduser;

public interface rduserRepo extends JpaRepository<rduser, Integer>{
	 rduser findByMobile(String mobile);
}
