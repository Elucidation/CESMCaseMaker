/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Requires all parameters passed to be lower case, and directly what are in the template
 * @author Sam
 */
@WebServlet(name = "fillTemplate", urlPatterns = {"/fillTemplate"})
public class fillTemplate extends HttpServlet {

    private ServletContext context;
    ConfigTemplate template = new ConfigTemplate(); // template helps determine valid parameters, and of course, the output.

    private String buildParameterXML(String[] names, String[] values) {
        // Expects names & values to never be null, only template valid parameters are passed
        StringBuilder out = new StringBuilder();
        out.append("<parameters>");

        for (int i = 0; i < names.length; i++) {
            out.append(String.format("<parameter name=\"%s\" value=\"%s\"/>", names[i], values[i]));
        }
        out.append("</parameters>");
        return out.toString();
    }

    private String buildPopulatedTemplateXML(String[] names, String[] values) {
        // Expects names & values to never be null
        StringBuilder out = new StringBuilder();
        out.append("<template>");
        out.append(buildPopulatedTemplateString(names, values));
        out.append("</template>");
        return out.toString();
    }

    private String buildPopulatedTemplateString(String[] names, String[] values) {
        // Expects names & values to never be null
        assert(names.length==values.length); // Stuff being passed doesn't map one-one
        template.passReplacements(names,values);
        return template.get();
    }

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
        } else if (action.equals("fill") || action.equals("fillSimple") || action.equals("fillHTML")) {

            // Get values of valid parameters
            ArrayList<String> names = new <String>ArrayList();
            ArrayList<String> values = new <String>ArrayList();
            for (int i = 0; i < ConfigTemplate.getValidPlaceholders().length; i++) {
                String value = request.getParameter(ConfigTemplate.getValidPlaceholders()[i].toLowerCase()); // null if doesn't exist
                if (value == null) {
                    value = request.getParameter(ConfigTemplate.getValidPlaceholders()[i].toUpperCase()); // try Upper Case also
                }
                if (value != null) {
                    names.add( ConfigTemplate.getValidPlaceholders()[i] );
                    values.add( value );
                }
            }
            String[] placeholders = new String[names.size()];
            String[] pValues = new String[names.size()];
            for (int i=0;i<names.size();i++){
                placeholders[i] = names.get(i);
                pValues[i] = values.get(i);
            }
            
            


            if (action.equals("fillSimple") || action.equals("fillHTML")) {
                // Respond a populated template as a string
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                try {
                    if (action.equals("fillHTML")) {
                        // replace \n with <br> so it shows up in browser
                        out.print(buildPopulatedTemplateString(placeholders, pValues).replaceAll("\n","<br>"));
                    } else {
                        // Simple
                        out.print(buildPopulatedTemplateString(placeholders, pValues));
                    }
                } finally {
                    out.close();
                }
            } else {
                // Respond with parameters + filled template wrapped in xml
                response.setContentType("text/xml");
                PrintWriter out = response.getWriter();
                try {
                    out.print("<wrapper>");
                    out.print(buildParameterXML(placeholders, pValues));
                    out.print(buildPopulatedTemplateXML(placeholders, pValues));
                    out.print("</wrapper>");

                } finally {
                    out.close();
                }
            }


        } else {
            context.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Returns a populated CESM configuration template from passed parameters";
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();
    }
    
}
