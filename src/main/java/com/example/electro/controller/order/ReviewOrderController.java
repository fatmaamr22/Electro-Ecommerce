package com.example.electro.controller.order;

import com.laptop.entity.Order;
import com.laptop.service.OrderServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/dashboard/order")
public class ReviewOrderController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderServiceImpl orderService = new OrderServiceImpl();
        Order order = orderService.getOrder(Integer.parseInt(req.getParameter("id")));
        req.setAttribute("order", order);
        req.getRequestDispatcher("review-order.jsp").forward(req, resp);
    }
}
