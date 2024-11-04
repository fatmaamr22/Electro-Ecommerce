package com.example.electro.controller.rest;

import com.example.electro.customDetails.CustomUserDetails;
import com.example.electro.model.Customer;
import com.example.electro.provider.CustomUserDetailsService;
import com.example.electro.service.CustomerService;
import com.example.electro.service.JwtService;
import com.example.electro.service.TemporaryCartService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    CustomerService customerService;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    TemporaryCartService temporaryCartService;

    @GetMapping("/auth/grantcode")
    public ModelAndView grantCode(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        String accessToken = getOauthAccessTokenGoogle(code, request);

        if (accessToken != null) {
            // Get user details from Google
            JsonObject userDetails = getProfileDetailsGoogle(accessToken);
            String email = userDetails.get("email").getAsString();
            String firstName = userDetails.get("given_name").getAsString();
            String lastName = userDetails.get("family_name").getAsString();
            String id = userDetails.get("id").getAsString().substring(0, 5);

            // Create or update user
            Optional<Customer> customerOptional = customerService.findByEmail(email);
            String token;

            if (customerOptional.isPresent()) {
                // Existing user
                Customer customer = customerOptional.get();
                token = jwtService.createToken(new HashMap<>(), email, "ROLE_USER");
                // Load user details for authentication

                UserDetails userDetail = customUserDetailsService.loadUserByUsername(email);
                System.out.println("LOGGED IN USERDEATAILS "+ userDetail.toString());
                CustomUserDetails customUserDetails= (CustomUserDetails) userDetail;
                System.out.println("LOGGED IN CustomUser "+ customUserDetails.getCustomer().toString());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                temporaryCartService.mergeCarts(session, ((CustomUserDetails) userDetail).getId());
                System.out.println("User logged in with email: " + email);
            } else {
                // New user
                Customer newCustomer = new Customer();
                newCustomer.setEmail(email);
                newCustomer.setPassword(passwordEncoder.encode(id)); // Use id as temporary password
                newCustomer.setFirstName(firstName);
                newCustomer.setLastName(lastName);
                newCustomer.setDateOfBirth(parseDateOfBirth("2000-01-01")); // Default date, adjust as needed
                newCustomer.setAddress("ITI");
                newCustomer.setInterests("CODING");
                newCustomer.setPhone("01001001010");

                // Save the new customer
                customerService.addCustomer(newCustomer);
                token = jwtService.createToken(new HashMap<>(), newCustomer.getEmail(), "ROLE_USER");

                // Load user details for authentication
                UserDetails loaduser = customUserDetailsService.loadUserByUsername(email);
                Authentication authentication = new UsernamePasswordAuthenticationToken(loaduser, null, loaduser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                temporaryCartService.mergeCarts(session, newCustomer.getId());
                System.out.println("New user registered with email: " + newCustomer.getEmail());
            }

            // Set the token as a cookie
            Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(request.isSecure());
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60 * 10);
            response.addCookie(jwtCookie);

            // Redirect to home after successful login
            modelAndView.setViewName("redirect:/");
        } else {
            modelAndView.setViewName("redirect:/auth/login");
        }

        return modelAndView;
    }

    private String getOauthAccessTokenGoogle(String code, HttpServletRequest request) throws IOException {
        Dotenv dotenv = Dotenv.load();
        String clientId = dotenv.get("CLIENT_ID"); // Replace with your Client ID
        String clientSecret = dotenv.get("CLIENT_SECRET"); // Replace with your Client Secret
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        String baseUri = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String redirectUri = baseUri + "/auth/grantcode";

        String requestBody = "code=" + code +
                "&redirect_uri=" + redirectUri +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=authorization_code";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        String url = "https://oauth2.googleapis.com/token";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);
        return jsonObject.get("access_token").getAsString(); // Get the access token string
    }

    private JsonObject getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        return new Gson().fromJson(response.getBody(), JsonObject.class);
    }

    private Date parseDateOfBirth(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle parsing exceptions accordingly
        }
    }
}
