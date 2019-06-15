package com.wf.controller;

import com.wf.entity.system.Department;
import com.wf.entity.system.Function;
import com.wf.entity.system.Post;
import com.wf.entity.system.Staff;
import com.wf.global.FunctionTree;
import com.wf.service.SelfService;
import com.wf.service.system.DepartmentService;
import com.wf.service.system.FunctionService;
import com.wf.service.system.PostService;
import com.wf.service.system.StaffService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("selfController")
@RequestMapping("/")
public class SelfController {
    @Autowired
    private FunctionService functionService;
    @Autowired
    private SelfService selfService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private PostService postService;
    @Autowired
    private DepartmentService departmentService;


    @RequestMapping("/toLogin.do")
    public String toLogin() throws IOException, ServletException {
        return "login";
    }

    @RequestMapping("/login.do")
    public String login(
            @RequestParam("account") String account,
            @RequestParam("password") String password,
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            //把用户名和密码封装为UsernamepasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(account, password);
            //remember me
            token.setRememberMe(true);
            try {
                //执行登陆
                currentUser.login(token);
            } catch (AuthenticationException ae) {
                System.out.println("登陆失败:" + ae.getMessage());
                return "redirect: login.jsp";
            }

        }
        Staff staff=(Staff)currentUser.getPrincipal();
        Map<Function, List<Function>> map = FunctionTree.functionTree(staff);
        List<Function> list = new ArrayList<Function>(map.keySet());
        Collections.sort(list, new Comparator<Function>() {
            public int compare(Function o1, Function o2) {
                return o1.getSerialNum() - o2.getSerialNum();
            }
        });
        HttpSession session = req.getSession();
        session.setAttribute("TREE", map);
        session.setAttribute("SORT", list);
        session.setAttribute("staff", staff);
       return "redirect: main.do";

    }


      /*String account=req.getParameter("account");
      String password=req.getParameter("password");
        Staff staff = selfService.login(account, password);
        if (staff == null) {
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        } else if (staff.getStatus().equals("正常")) {
            Map<Function, List<Function>> map = FunctionTree.functionTree(staff);
            List<Function> list = new ArrayList<Function>(map.keySet());
            Collections.sort(list, new Comparator<Function>() {
                public int compare(Function o1, Function o2) {
                    return o1.getSerialNum() - o2.getSerialNum();
                }
            });
            HttpSession session = req.getSession();
            session.setAttribute("TREE", map);
            session.setAttribute("SORT", list);
            session.setAttribute("staff", staff);
            resp.sendRedirect("main.do");
        } else {
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }*/


    @RequestMapping("/logout.do")
    public void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session=req.getSession();
        session.removeAttribute("staff");
        resp.sendRedirect("login.jsp");
    }

    @RequestMapping("/main.do")
    public void main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }

    @RequestMapping("/info.do")
    public void info(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession();
        Staff staff= (Staff) session.getAttribute("staff");
        req.setAttribute("USER",staff);
        req.getRequestDispatcher("info.jsp").forward(req,resp);
    }

@RequestMapping("/toChangePassword.do")
    public void toChangePassword(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession();
        Staff staff= (Staff) session.getAttribute("staff");
        req.setAttribute("USER",staff);
        req.getRequestDispatcher("change_password.jsp").forward(req,resp);
    }



    @RequestMapping("/changePassword.do")
    public void changePassword(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        HttpSession session=req.getSession();
        Staff staff= (Staff) session.getAttribute("staff");
        String newPass=req.getParameter("password1");
        String pass=req.getParameter("password");
        if(pass.equals(staff.getPassword()))
        {
            selfService.changePass(staff.getId(),newPass);
            resp.getWriter().print("<script type=\"text/javascript\">parent.location.href=\"logout.do\"</script>");
        }
       else
        {
           resp.sendRedirect("toChangePassword.do");
        }
    }

    @RequestMapping("/register.do")
    public void register(HttpServletRequest req, HttpServletResponse resp) throws IOException, ParseException {
        Staff staff=new Staff();
        String hashAlgorithnName="MD5";
        int hashIterations = 1024;
        Object pass= new SimpleHash(hashAlgorithnName,req.getParameter("password"),req.getParameter("account"),hashIterations);
        staff.setAccount(req.getParameter("account"));
        staff.setPassword(pass.toString());
        staff.setName(req.getParameter("name"));
        staff.setSex(req.getParameter("sex"));
        staff.setBornDate(new SimpleDateFormat("yy-mm-dd").parse(req.getParameter("bornDate")));
        staff.setIdNumber(req.getParameter("idNumber"));
        staffService.add(staff);
        String ids[] =req.getParameterValues("dep");
        if(ids==null)
        {

        }else
        {
            for(String id:ids)
            {
                Post post=new Post();
                post.setSid(staff.getId());
                post.setDid(Integer.parseInt(id));
                postService.add(post);
            }
        }
        resp.sendRedirect("toLogin.do");
    }




    @RequestMapping("toRegister")
    public void toRegister(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        List<Department> department=departmentService.searchAll();
        req.setAttribute("DEP",department);
        req.getRequestDispatcher("register.jsp").forward(req,resp);
    }

}
