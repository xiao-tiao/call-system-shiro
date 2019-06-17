package com.wf.global;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class URLParserListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext application=sce.getServletContext();
        String path=application.getContextPath();
        application.setAttribute("APP_PATH",path);
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
