package com.rdsystem.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rdsystem.dto.UserpassbookDto;
import com.rdsystem.entity.rdpassbook;



public interface rdpassbookRepo extends JpaRepository<rdpassbook, Integer>{
	
	
	@Query("select SUM(r.rdamt) from rdpassbook r where r.rid=:rid")
	Long getTotalAmountByRid(@Param("rid") int rid);
	
	@Query("select COUNT(r.rdamt) from rdpassbook r where r.rid=:rid")
	Long getTransactionCount(@Param("rid") int rid);

	@Query (value = "SELECT SUM(rdamt) FROM rdpassbook", nativeQuery = true)
	Long getTotalCount();
	@Query (value = "SELECT COUNT(rdamt) FROM rdpassbook", nativeQuery = true)
	Long getTCount();
	@Query (value = "SELECT *FROM rdpassbook  where rid = :rid", nativeQuery = true)
	List<rdpassbook> findAllRid(@Param("rid") int rid);

	@Query (value = "select rduser.rid,name,acno,rdpassbook.rdamt,rdpassbook.rddate from rduser inner join rdpassbook on\r\n"
			+ "rduser.rid=rdpassbook.rid ", nativeQuery = true)
	List<UserpassbookDto> getrdpassbookAllDetails();
	@Query (value = "select rduser.rid,name,acno,rdpassbook.rdamt,rdpassbook.rddate from rduser inner join rdpassbook on\r\n"
			+ "rduser.rid=rdpassbook.rid;", nativeQuery = true)
	List<UserpassbookDto> getrdpassbookAllDetail(@Param("rid") int rid);
}
