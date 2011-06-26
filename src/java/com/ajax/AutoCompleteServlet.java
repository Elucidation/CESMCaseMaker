/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sam
 */
@WebServlet(name = "AutoCompleteServlet", urlPatterns = {"/autocomplete"})
public class AutoCompleteServlet extends HttpServlet {

    private ServletContext context;
    private CaseTemplate template = new CaseTemplate();

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AutoCompleteServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AutoCompleteServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
             */
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        //StringBuffer sb = new StringBuffer();

        // On Error
        //context.getRequestDispatcher("/error.jsp").forward(request, response);

        if (action.equals("fill")) {
            // Paramters passed in
            String casename = request.getParameter("name");
            String resolution = request.getParameter("res");
            String compset = request.getParameter("compset");
            String machine = request.getParameter("mach");
            
            //  Send Response
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            template.resetPopulatedTemplate();
            template.fillTemplate(casename, resolution, compset, machine);
            response.getWriter().write("<create_newcase>" + template.get() + "</create_newcase>");
        } else if (action.equals("fillEnvConf")) {
            // Paramters passed in
            String runType = request.getParameter("runType");
            String startDate = request.getParameter("startDate");

            // Add env_conf.xml options to template
            //  Send Response
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            template.fillEnvConf(runType, startDate);
            response.getWriter().write("<create_newcase>" + template.get() + "</create_newcase>");
        } else if (action.equals("loadOptionsEnvConf")) {
            // Set up visible options table in index.jsp
            //System.err.println("Got called by loadOptionsEnvConf.");
            //  Send Response
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            String envConfTableXML = "bloop";
            response.getWriter().write("<env_conf>" + envConfTableXML + "</env_conf>");
        } else {
            //nothing to show
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
