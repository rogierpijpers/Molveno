package com.capgemini.web;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.WebResult;

@RestController
public class AdminController {
    @RequestMapping("/admin")
    public String index(){
        return "Admin page (I should be logged in as admin to see this)";
    }
}