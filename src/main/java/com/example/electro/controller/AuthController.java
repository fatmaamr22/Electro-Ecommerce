package com.example.electro.controller;

import com.example.electro.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/login")
    public String welcome() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpServletResponse response, Model model) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Generate the JWT token
            String token = jwtService.generateToken(email);

            // Create a cookie to store the token
            Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
            jwtCookie.setHttpOnly(true); // Prevent JavaScript from accessing the token
            jwtCookie.setSecure(true); // Ensure the cookie is only sent over HTTPS
            jwtCookie.setPath("/"); // Set the cookie path
            jwtCookie.setMaxAge(60 * 60 * 10); // Set expiry time for the token (10 hours)

            // Add the cookie to the response
            response.addCookie(jwtCookie);

            return "redirect:/index.jsp";
        } catch (AuthenticationException e) {
            // Set the error message in the model
            model.addAttribute("loginErrorResponse", "Invalid email or password");

            // Redirect back to the login page
            return "redirect:/auth/login.jsp";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Create a cookie with the same name as the JWT token and set maxAge to 0
        Cookie jwtCookie = new Cookie("JWT_TOKEN", null); // Set the value to null
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/"); // Ensure the path is the same as when the cookie was created
        jwtCookie.setMaxAge(0); // Setting maxAge to 0 will delete the cookie

        // Add the cookie to the response to delete it
        response.addCookie(jwtCookie);

        // Redirect to the login page or home page after logging out
        return "redirect:/auth/login.jsp";
    }
}
