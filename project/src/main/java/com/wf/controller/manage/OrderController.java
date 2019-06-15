package com.wf.controller.manage;

import com.wf.entity.manage.Order;
import com.wf.service.manage.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@Controller("orderController")
@RequestMapping("/manage/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @RequestMapping("/list.do")
    public void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       List<Order> orders=orderService.findOrders(new Order());
       req.setAttribute("ORDER",orders);
       req.getRequestDispatcher("list.jsp").forward(req,resp);

    }
    @RequestMapping("/info.do")
    public void info(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
            int id=Integer.parseInt(req.getParameter("id"));
            Order order=orderService.findById(id);
            req.setAttribute("ORDER",order);
            req.getRequestDispatcher("info.jsp").forward(req,resp);

    }
}
