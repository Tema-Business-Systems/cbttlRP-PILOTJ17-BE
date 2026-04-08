package com.transport.tracking.k.controller;

import com.transport.tracking.k.annotation.Anonymous;
import com.transport.tracking.k.service.UserService;
import com.transport.tracking.response.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins={"http://localhost:3000","https://preview--coke-logistics-flow.lovable.app","http://localhost:3431","http://localhost:3432","http://10.1.5.10:3432","http://10.1.5.10:3431","https://10.1.5.10:3432","https://10.1.5.10:3431","https://routeplanner.cbttl.com:3431","https://routeplanner.cbttl.com:3432", "http://10.1.5.10:3535"})
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping ("/login")
//    @Anonymous
//    public @ResponseBody ResponseEntity<Object> login(@RequestBody UserVO userVO, HttpServletResponse response) {
//        log.info("UserVO  ======== ", userVO);
//        userVO = userService.login(userVO, response);
//        if(Objects.isNull(userVO)) {            Map<String, String> map = new HashMap<>();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map.put("message", "error"));
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(userVO);
//    }



    @PostMapping ("/opslogin")
    @Anonymous
    public @ResponseBody ResponseEntity<Object> opsLogin(@RequestBody UserVO userVO, HttpServletResponse response) {
        log.info("OPS Login attempt for: {}", userVO.getXlogin());
        try {
            UserVO result = userService.opsLogin(userVO, response);
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            Map<String, String> error= new HashMap<>();
            error.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }




    @PostMapping ("/login")
    @Anonymous
    public @ResponseBody ResponseEntity<Object> login(@RequestBody UserVO userVO, HttpServletResponse response) {
        log.info("UserVO  ======== ", userVO);
        try {
            UserVO result = userService.login(userVO, response);
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            Map<String, String> error= new HashMap<>();
            error.put("message",ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }



    @GetMapping ("/logout")
    @Anonymous
    public @ResponseBody ResponseEntity<Object> logout(HttpServletResponse response) {
        userService.logOut(response);
        return ResponseEntity.status(HttpStatus.OK).body("sucess");
    }
}
