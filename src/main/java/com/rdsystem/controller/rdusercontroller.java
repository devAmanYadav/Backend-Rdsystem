package com.rdsystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.rdsystem.entity.rduser;
import com.rdsystem.repo.rduserRepo;
import com.rdsystem.security.JwtUtil;

@RestController
public class RdUserController {

    @Autowired
    private rduserRepo rdrepo;

    @Autowired
    private JwtUtil jwtUtil;

    // ----------------- PUBLIC APIs -----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody rduser user) {

        if(user.getMobile() == null || user.getPassword() == null){
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Mobile or Password missing");
        }

        rduser existing = rdrepo.findByMobile(user.getMobile());

        if(existing != null && existing.getPassword() != null &&
           existing.getPassword().equals(user.getPassword())) {

            String token = jwtUtil.generateToken(existing.getMobile());
        
            Map<String,Object> res = new HashMap<>();
            res.put("token", token);
            res.put("rid", existing.getRid());
            res.put("name", existing.getName());

            return ResponseEntity.ok(res);
        }

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Invalid Mobile or Password");
    }

    @PostMapping("/rdusave")
    public ResponseEntity<?> saveUser(@RequestBody rduser user){

        rduser existing = rdrepo.findByMobile(user.getMobile());

        if(existing != null){
            return ResponseEntity
                    .badRequest()
                    .body("Mobile already exists");
        }

        return ResponseEntity.ok(rdrepo.save(user));
    }

    // ----------------- PROTECTED APIs -----------------
    private boolean isTokenValid(String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")) return false;
        String token = authHeader.substring(7);
        String mobile = jwtUtil.validateTokenAndGetSubject(token);
        return mobile != null;
    }

    @GetMapping("/rduser")
    public ResponseEntity<?> getrduser(@RequestHeader("Authorization") String authHeader){
        if(!isTokenValid(authHeader))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");

        return ResponseEntity.ok(rdrepo.findAll());
    }

    @GetMapping("/rduser/{rid}")
    public ResponseEntity<?> getUser(@PathVariable("rid") int rid,
                                     @RequestHeader("Authorization") String authHeader){
        if(!isTokenValid(authHeader))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");

        return ResponseEntity.ok(rdrepo.findById(rid).orElse(null));
    }

    @GetMapping("/rduserbyid/{rid}")
    public ResponseEntity<?> rduserbyID(@PathVariable("rid") int rid,
                                        @RequestHeader("Authorization") String authHeader){
        if(!isTokenValid(authHeader))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");

        return ResponseEntity.ok(rdrepo.findAllById(List.of(rid)));
    }

    @PutMapping("/rduserupdate")
    public ResponseEntity<?> updaterduser(@RequestBody rduser s,
                                          @RequestHeader("Authorization") String authHeader){
        if(!isTokenValid(authHeader))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");

        return ResponseEntity.ok(rdrepo.save(s));
    }

    @DeleteMapping("/rdudelete/{rid}")
    public ResponseEntity<?> deleterdpassbook(@PathVariable("rid") int rid,
                                              @RequestHeader("Authorization") String authHeader){
        if(!isTokenValid(authHeader))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");

        rdrepo.deleteById(rid);
        Map<String,String> res = new HashMap<>();
        res.put("message", "Deleted Successfully");
        return ResponseEntity.ok(res);
    }

    @PutMapping("/rdclose/{rid}")
    public ResponseEntity<?> closeRD(@PathVariable("rid") int rid,
                                     @RequestHeader("Authorization") String authHeader){
        if(!isTokenValid(authHeader))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");

        rduser user = rdrepo.findById(rid).orElse(null);
        if(user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        user.setStatus("CLOSED");
        rdrepo.save(user);

        Map<String,String> res = new HashMap<>();
        res.put("message", "RD Closed Successfully");
        return ResponseEntity.ok(res);
    }
}