package com.security.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.login.model.User;
import com.security.login.service.UserService;

// https://medium.com/code-with-farhan/spring-security-2023-4110f1e33b47
@RestController
public class AuthController {
	   private final UserService userService;

	    private final AuthenticationManager authenticationManager;

	    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
	        this.userService = userService;
	        this.authenticationManager = authenticationManager;
	    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        return "redirect:/login";
    }
    @GetMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response, Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "register";
    }
    
    @PostMapping("/register")
    public String createNewUser(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("user")User user){

        try {

            user.setRole("USER");

            User newUser = userService.createUser(user);
            if(newUser == null){
                return "redirect:/register?error";
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,securityContext);

            return "redirect:/";

        } catch (Exception e){
            return "redirect:/register?error";
        }

    }
}