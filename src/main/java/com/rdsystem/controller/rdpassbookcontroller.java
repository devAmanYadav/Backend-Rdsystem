package com.rdsystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rdsystem.dto.UserpassbookDto;
import com.rdsystem.entity.rdpassbook;
import com.rdsystem.repo.rdpassbookRepo;
import com.rdsystem.services.PassbookService;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
public class rdpassbookcontroller {

	@Autowired
	private rdpassbookRepo prepo;
	
	@GetMapping("/rdpassbook")
	private List<rdpassbook> getrdpassbook(){
		List<rdpassbook> list= prepo.findAll();
		return list;
	}
	@GetMapping("/details")
	private List<UserpassbookDto> getDetails(){
		return prepo.getrdpassbookAllDetails();
	}
	
	@GetMapping("/detail")
	private List<UserpassbookDto> getDetail(@PathVariable("rid") int rid){
		return prepo.getrdpassbookAllDetail(rid);
	}
	
	@Autowired
	 private PassbookService service;
	 @GetMapping("/detailst/{rid}")
	 public List<UserpassbookDto> getDetailst(@PathVariable("rid") int  rid) {
	 return service.getDetails(rid);
	 }
	
	@PostMapping("/rdpsave")
	private rdpassbook saverdpassbook(@RequestBody rdpassbook s){

	    if(s.getLate_day() > 0){
	        s.setFlag(1);
	        s.setFine_amt(s.getLate_day() * 5);
	    }
	    else{
	        s.setFlag(0);
	        s.setFine_amt(0);
	    }
		return  prepo.save(s);
	}
	@PutMapping("/rdpupdate")
	private rdpassbook updaterdpassbook(@RequestBody rdpassbook s){
		return  prepo.save(s);
	}
	@DeleteMapping("/rdpdelete/{pid}")
	private String deleterdpassbook(@PathVariable("pid") int rid){
		prepo.deleteById(rid);
		return "Deleted Successfully.........!";
	}
	
	@GetMapping("/ttl")
	public Map<String, Object> summary(){
		Long totals = prepo.getTotalCount();
		Map<String, Object> result = new HashMap<>();
		result.put("Total", totals);
		return result;
	}
	
	@GetMapping("/ttl/{rid}")
	public Map<String, Object> getTotalAmountByRid(@PathVariable("rid") int rid){
	    Long totals = prepo.getTotalAmountByRid(rid);
	    Map<String, Object> result = new HashMap<>();
	    result.put("Total", totals);
	    
	    return result;
	}
	@GetMapping("/count/{rid}")
	public Map<String, Object> getTransactionCount(@PathVariable("rid") int rid){

	    Long totals = prepo.getTransactionCount(rid);

	    Map<String, Object> result = new HashMap<>();
	    result.put("Total", totals);

	    return result;
	}
	
	@GetMapping("/count")
	public Map<String, Object> summy(){
		Long totals = prepo.getTCount();
		Map<String, Object> result = new HashMap<>();
		result.put("Total", totals);
		return result;
	}
	
	@GetMapping("/rdpassbookByid/{rid}")
	private List<rdpassbook> getAllrdpassbook(@PathVariable("rid") int rid){
		List<rdpassbook> list= prepo.findAllRid(rid);
		return list;
	}
	
	@GetMapping("/rdclose/{rid}")
	public Map<String, Object> rdClose(@PathVariable("rid") int rid){
	    Long totalAmount = prepo.getTotalAmountByRid(rid);
	    Long months = prepo.getTransactionCount(rid);

	    Map<String, Object> response = new HashMap<>();

	    // Determine user status
	    String userStatus = months < 12 ? "ACTIVE" : "CLOSED";
	    response.put("userStatus", userStatus); // this is for React to show status

	    // Calculate final amount
	    double finalAmount;
	    if(months < 12){
	        finalAmount = totalAmount / 2.0;
	        response.put("status", "closed_before_maturity");
	    } else {
	        double interest = (totalAmount * 14) / 100.0;
	        finalAmount = totalAmount + interest;
	        response.put("status", "matured");
	    }

	    response.put("amount", finalAmount);
	    response.put("months", months);
	    response.put("totalAmount", totalAmount);

	    return response;
	}
	
}
