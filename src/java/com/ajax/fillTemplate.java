/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
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
@WebServlet(name = "fillTemplate", urlPatterns = {"/fillTemplate"})
public class fillTemplate extends HttpServlet {
    private ServletContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();
    }
    
    // The are the placeholders (All lowercase)
    private String[] placeholders = ("casename resolution compset machine " // the basic create_newcase.sh parameters
            + "RUN_TYPE RUN_STARTDATE RUN_REFCASE RUN_REFDATE" // Come env_conf.xml Environment Variables
            + "").toLowerCase().split(" ");
    private String[] envPlaceholders = "".split(" ");
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
            /* TODO output your page here*/
            /*
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet fillTemplate</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet fillTemplate at " + request.getContextPath () + "</h1>");
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
        
        if (action == null) {
            context.getRequestDispatcher("/error.jsp").forward(request, response);
        } else if (action.equals("fill")) {
            
            // Get values of valid parameters
            String[] pValues = new String[placeholders.length];
            for (int i=0;i<placeholders.length;i++) {
                pValues[i] = request.getParameter(placeholders[i]); // null if doesn't exist
            }
            
            
            
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            try {

                /* TODO output your page here*/
                out.println("<parameters>");
                
                for (int i=0;i<placeholders.length;i++) {
                    if (pValues[i] != null)
                        out.println(String.format("<parameter name=\"%s\" value=\"%s\"/>",placeholders[i],pValues[i]));
                }
                out.println("</parameters>");
                
                
            } finally {            
                out.close();
            }
            
            
        } else {
            context.getRequestDispatcher("/error.jsp").forward(request, response);
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
