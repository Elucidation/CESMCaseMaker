/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
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
    private EnvConfigData envCfgData = new EnvConfigData();
    private HashMap envConfigOptions = envCfgData.getEnvConfigOptions(); // Contains only table data & defaults, not options chosen

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
            //System.err.println("Start here");
            template.fillTemplate(casename, resolution, compset, machine);
            String wrapper = "<wrap>"
                    + "<create_newcase>" + template.get() + "</create_newcase>"
                    + "<env_conf>" + getEnvConfigOptionsXML() + "</env_conf>"
                    + "</wrap>";
            response.getWriter().write(wrapper);
            //System.err.println("end here");
        } else if (action.equals("fillEnvConf")) {
            // Paramters passed in
            String[] envParams = new String[envConfigOptions.size()];
            Iterator it = envConfigOptions.keySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                String id = (String) it.next();
                EnvConfigOption option = (EnvConfigOption) envConfigOptions.get(id);
                envParams[i] = request.getParameter(option.getName() + "-field");
                //System.err.println("We're here #" + i + ": (id: " + id + ") " + envParams[i]);
                i++;
            }

            // Add env_conf.xml options to template
            //  Send Response
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            template.fillEnvConf(envParams);
            //response.getWriter().write("<create_newcase>" + template.get() + "</create_newcase>");
            String wrapper = "<wrap>"
                    + "<create_newcase>" + template.get() + "</create_newcase>"
                    + "<env_conf>" + getEnvConfigOptionsXML() + "</env_conf>"
                    + "</wrap>";
            response.getWriter().write(wrapper);
        } else if (action.equals("loadOptionsEnvConf")) {
            // Set up visible options table in index.jsp
            //System.err.println("Got called by loadOptionsEnvConf.");
            //  Send Response
            String level = request.getParameter("level");
            if (level != null && level.equalsIgnoreCase("full")) { // Reset template fully
                template.resetEnvConfOptions();
                template.resetPopulatedTemplate();

            }
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<env_conf>" + getEnvConfigOptionsXML() + "</env_conf>");
        } else {
            //nothing to show
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        // Put this separate for now
        /*
        if (action.equals("fillEnvConf")) {
            Iterator it = envConfigOptions.keySet().iterator();
            while (it.hasNext()) {
                String id = (String) it.next();
                EnvConfigOption option = (EnvConfigOption) envConfigOptions.get(id);
            }
        }*/
    }

    private String getEnvConfigOptionsXML() {
        // Format:
        /*<envConfigOption>
         *  <id></id>
         *  <name></name>
         *  <value></value>
         *  <defaultValue></defaultValue>
         *  <readableName></readableName>
         *  <description></description>
         *</envConfigOption>
         */
        String output = "";
        Iterator it = envConfigOptions.keySet().iterator();
        while (it.hasNext()) {
            String id = (String) it.next();
            EnvConfigOption option = (EnvConfigOption) envConfigOptions.get(id);
            if (doCheck(option)) {
                // This option should be disabled due to values in other parts of the config
                //String param = option.getName() + "-field");
                output += "<envConfigOption>";
                output += "<id>" + option.getId() + "</id>";
                output += "<name>" + option.getName() + "</name>";
                if (!template.getEnvConfigValue(option.getName()).isEmpty()) {
                    output += "<value>" + template.getEnvConfigValue(option.getName()) + "</value>";
                } else {
                    output += "<value>" + option.getDefaultValue() + "</value>";
                }
                output += "<defaultValue>" + option.getDefaultValue() + "</defaultValue>";
                // Add check to CaseTemplate where if value has previously been added, use that instead of default
                output += "<readableName>" + option.getReadableName() + "</readableName>";
                output += "<description>" + option.getDescription() + "</description>";
                output += "</envConfigOption>";
            } else {
                // Remove from template
                template.removeEnvConfigValue(option.getName());
            }
        }
        return output;
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

    private boolean doCheck(EnvConfigOption option) {
        // Checks if there is special circumstance for field given
        // This hardcoding needs to be moved to either SQL table merges of some sort (for deduction only)
        // Or for a more powerful comparison, OWL ontology inference engine.

        // RUN_STARTDATE only allowed if RUN_TYPE == Startup|Hybrid
        //System.err.println(":: "+option.getName() + " vs " + template.getEnvConfigValue("RUN_TYPE"));
        if (option.getName().equalsIgnoreCase("RUN_STARTDATE")
                && template.getEnvConfigValue("RUN_TYPE").equalsIgnoreCase("Branched")) {
            return false;
        } 
        // RUN_REFDATE ignored for RUN_TYPE == Startup (default)
        if (option.getName().equalsIgnoreCase("RUN_REFDATE")
                && (template.getEnvConfigValue("RUN_TYPE").equalsIgnoreCase("startup")
                || template.getEnvConfigValue("RUN_TYPE").equalsIgnoreCase(""))) {
            return false;
        }
        return true;
    }
}
