package com.example.electro.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error attributes
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        Object message = request.getAttribute("jakarta.servlet.error.message");
        Object throwable = request.getAttribute("jakarta.servlet.error.exception");
        Object requestURI = request.getAttribute("jakarta.servlet.error.request_uri");
        Object requestMethod = request.getMethod(); // Get the HTTP method

        // Retrieve additional details, if available
        String reason = "Unknown error"; // Default reason
        if (throwable instanceof Exception) {
            Exception exception = (Exception) throwable;
            reason = exception.getMessage(); // Get the reason from the exception message
        }

        // Debugging information (optional)

        // Add attributes to the model
        model.addAttribute("status", status);
        model.addAttribute("message", message != null ? message : "An error occurred. Please try again later.");
        model.addAttribute("exception", throwable != null ? throwable.toString() : "No additional information available.");
        model.addAttribute("requestURI", requestURI != null ? requestURI.toString() : "Unknown");
        model.addAttribute("requestMethod", requestMethod != null ? requestMethod : "Unknown");
        model.addAttribute("reason", reason); // Add the reason for the error

        return "error";
    }
}