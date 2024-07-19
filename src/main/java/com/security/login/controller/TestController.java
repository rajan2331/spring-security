package com.security.login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	
	@GetMapping("/")
	public String save()
	{
		return "test1";
	}
	
	@GetMapping("/hello")
	public String hello()
	{
		return "test1 hello url";
	}
}
