package com.wf.controller.system;

import com.wf.entity.system.Department;
import com.wf.entity.system.Post;
import com.wf.entity.system.Staff;
import com.wf.service.system.DepartmentService;
import com.wf.service.system.PostService;
import com.wf.service.system.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller("staffController")
@RequestMapping("/system/staff")
public class StaffController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private StaffService  staffService;
    @Autowired
    private PostService postService;
    @RequestMapping("/list.do")
    public void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Staff> list=staffService.searchAll();
        req.setAttribute("staffList",list);
        req.getRequestDispatcher("staff_list.jsp").forward(req,resp);
    }
    @RequestMapping("/toAdd.do")
    public void toAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Department> department=departmentService.searchAll();
        req.setAttribute("DEP",department);
        req.getRequestDispatcher("staff_add.jsp").forward(req,resp);
    }
    @RequestMapping("/add.do")
    public void add(HttpServletRequest req, HttpServletResponse resp) throws IOException, ParseException {
        Staff staff=new Staff();
        staff.setAccount(req.getParameter("account"));
        staff.setPassword(req.getParameter("password"));
        staff.setName(req.getParameter("name"));
        staff.setSex(req.getParameter("sex"));
        staff.setBornDate(new SimpleDateFormat("yy-mm-dd").parse(req.getParameter("bornDate")));
        staff.setIdNumber(req.getParameter("idNumber"));
        String ids[] =req.getParameterValues("dep");
        List<Post> posts=new ArrayList<Post>();
        if(ids==null)
        {

        }else
        {
            for(String id:ids)
            {
                Post post=new Post();
                post.setDid(Integer.parseInt(id));
                posts.add(post);
            }
        }
        staff.setPost(posts);
        staffService.add(staff);

        resp.sendRedirect("list.do");
    }

    @RequestMapping("/delete.do")
    public void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id=Integer.parseInt(req.getParameter("id"));
        staffService.delete(id);
        resp.sendRedirect("list.do");
    }
    @RequestMapping("/toUpdate.do")
    public void toUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id=Integer.parseInt(req.getParameter("id"));
        Staff staff=staffService.searchOne(id);
        List<Department> department=departmentService.searchAll();
        req.setAttribute("STAFF",staff);
        req.setAttribute("DEP",department);
        req.getRequestDispatcher("staff_update.jsp").forward(req,resp);
    }
    @RequestMapping("/update.do")
    public void update(HttpServletRequest req, HttpServletResponse resp) throws IOException, ParseException {
        Staff staff=staffService.searchOne(Integer.parseInt(req.getParameter("id")));
        staff.setSex(req.getParameter("sex"));
        staff.setName(req.getParameter("name"));
        staff.setStatus(req.getParameter("status"));
        if(req.getParameter("workTime")!=null && !req.getParameter("workTime").equals(""))
        {
            staff.setWorkTime(new SimpleDateFormat("yyyy-mm-dd").parse(req.getParameter("workTime")));
        }
        if(req.getParameter("bornDate")!=null && !req.getParameter("bornDate").equals(""))
        {
            staff.setBornDate(new SimpleDateFormat("yyyy-mm-dd").parse(req.getParameter("bornDate")));
        }
        if(req.getParameter("leaveTime")!=null && !req.getParameter("leaveTime").equals(""))
        {
            staff.setLeaveTime(new SimpleDateFormat("yyyy-mm-dd").parse(req.getParameter("leaveTime")));
        }
        staff.setAccount(req.getParameter("account"));
        staff.setInfo(req.getParameter("info"));
        staff.setPassword(req.getParameter("password"));
        staff.setIdNumber(req.getParameter("idNumber"));
        String ids[] =req.getParameterValues("dep");
        List<Post> posts=new ArrayList<Post>();
        if(ids==null)
        {

        }else
        {
            for(String id:ids)
            {
                Post post=new Post();
                post.setSid(Integer.parseInt(req.getParameter("id")));
                post.setDid(Integer.parseInt(id));
                posts.add(post);
            }
        }
        staff.setPost(posts);
        staffService.update(staff);
        resp.sendRedirect("list.do");
    }

    @RequestMapping("/lock.do")
    public void lock(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        int id=Integer.parseInt(req.getParameter("id"));
        Staff staff=staffService.searchOne(id);
        staff.setStatus("锁定");
        staffService.updateStatus(staff);
        resp.sendRedirect("list.do");

    }
    @RequestMapping("/unlock.do")
    public void unlock(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        int id=Integer.parseInt(req.getParameter("id"));
        Staff staff=staffService.searchOne(id);
        staff.setStatus("正常");
        staffService.updateStatus(staff);
        resp.sendRedirect("list.do");

    }
}
