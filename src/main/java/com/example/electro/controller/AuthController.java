package com.example.electro.controller;

import com.example.electro.customDetails.CustomUserDetails;
import com.example.electro.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;

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
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpServletRequest request, HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Check if authentication was successful
            if (authentication != null) {
                // Print the authentication details
                System.out.println("Authentication Details:");
                System.out.println("Principal: " + authentication.getPrincipal().toString());
                System.out.println("Authorities: " + authentication.getAuthorities());

//                Integer UserID = (Integer) authentication.getPrincipal();
//                System.out.println("userID  "+ UserID );

                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                System.out.println("userID  "+ userDetails.getId() );
                System.out.println("userIDpassword  "+ userDetails.getPassword() );
                System.out.println("userIDusername  "+ userDetails.getUsername() );


                // Determine if the user is an admin or a regular user
                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

                System.out.println("isAdmin: " + isAdmin);

                String token="";
                // Generate the JWT token
                if (isAdmin) {
                    // Generate token with admin role
                     token = jwtService.createToken(new HashMap<>(), email, "ROLE_ADMIN");
                } else {
                    // Generate token with user role
                     token = jwtService.createToken(new HashMap<>(), email, "ROLE_USER");
                }

                // Create a cookie to store the token
                Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
                jwtCookie.setHttpOnly(true); // Prevent JavaScript from accessing the token
                jwtCookie.setSecure(true); // Ensure the cookie is only sent over HTTPS
                jwtCookie.setPath("/"); // Set the cookie path
                jwtCookie.setMaxAge(60 * 60 * 10); // Set expiry time for the token (10 hours)

                // Add the cookie to the response
                response.addCookie(jwtCookie);

                // Redirect based on the user role
                if (isAdmin) {
                    return "redirect:/dashboard/customers"; // Admin dashboard
                } else {
                    return "redirect:/"; // User homepage
                }
            } else {
                redirectAttributes.addFlashAttribute("loginErrorResponse", "Authentication failed. Please try again.");
                return "redirect:" + referer; // Redirect back to the login page
            }
        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute("loginErrorResponse", "Invalid email or password");
            return "redirect:" + referer; // Redirect back to the login page
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
