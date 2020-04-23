/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Book;
import entity.Person;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jsonbuilders.JsonBookBuilder;
import jsonbuilders.JsonPersonBuilder;
import jsonbuilders.JsonUserBuilder;
import session.BookFacade;
import session.PersonFacade;

@WebServlet(name = "UserController", loadOnStartup = 1, urlPatterns = {
    "/addBook",
    
})
public class UserController extends HttpServlet {
    @EJB private BookFacade bookFacade;
    @EJB private PersonFacade personFacade;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        String json = "";
        HttpSession session = request.getSession(false);
        if(session == null){
            job.add("authStatus", "false")
                .add("user", "null")
                .add("token","null");
            try(Writer writer =new StringWriter()) {
                Json.createWriter(writer).write(job.build());
                json = writer.toString(); 
            }
            try (PrintWriter out = response.getWriter()) {
                out.println(json);        
            }
            return;
        };
        User user = (User) session.getAttribute("user");
        if(user == null){
            job.add("authStatus", "false")
                .add("user", "null")
                .add("token","null");
            try(Writer writer =new StringWriter()) {
                Json.createWriter(writer).write(job.build());
                json = writer.toString(); 
            }
            try (PrintWriter out = response.getWriter()) {
                out.println(json);        
            }
            return;
        };
        JsonUserBuilder ujb = new JsonUserBuilder();
        JsonObject jsonUser = ujb.createJsonUserObject(user);
        String path = request.getServletPath();
        switch (path) {
            case "/getListPersons":
                List<Person> listCustomers = personFacade.findAll();
                jab = Json.createArrayBuilder();
                JsonPersonBuilder cjb = new JsonPersonBuilder();
                for(Person c : listCustomers){
                    jab.add(cjb.createJsonPersonObject(c));
                }
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                jsonObjectBuilder.add("customers", jab)
                        .add("authStatus","true")
                        .add("user", jsonUser)
                        .add("token", session.getId());
                try(Writer writer =new StringWriter()) {
                  Json.createWriter(writer).write(jsonObjectBuilder.build());
                  json = writer.toString(); 
                }
                break;
            case "/addBook":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
                String caption = jsonObject.getString("caption");
                String author = jsonObject.getString("author");
                String publishedYear = jsonObject.getString("publishedYear");
                String cover = jsonObject.getString("cover");
                Date date = Calendar.getInstance().getTime();
                String textBook = jsonObject.getString("textBook");
                Book book = new Book(caption, author, Integer.parseInt(publishedYear), cover, textBook);
                bookFacade.create(book);
                JsonBookBuilder bjb = new JsonBookBuilder();
                jsonObjectBuilder = Json.createObjectBuilder();
                jsonObjectBuilder
                        .add("actionStatus", "true")
                        .add("book",bjb.createJsonObject(book))
                        .add("user", jsonUser)
                        .add("token", session.getId());
                try(Writer writer =new StringWriter()){
                  Json.createWriter(writer).write(jsonObjectBuilder.build());
                  json = writer.toString(); 
                }
                break;
        }
        try (PrintWriter out = response.getWriter()) {
          out.println(json);        
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
