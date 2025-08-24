package iuh.fit.se.lab_1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "registerServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");


        PrintWriter writer = resp.getWriter();
        writer.println("<html><body>");

        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String facebook = req.getParameter("facebook");
        String bio = req.getParameter("bio");

        writer.println("<p>Name: " + name + "</p>");
        writer.println("<p>Username: " + username + "</p>");
        writer.println("<p>Password: " + password + "</p>");
        writer.println("<p>Email: " + email + "</p>");
        writer.println("<p>Facebook: " + facebook + "</p>");
        writer.println("<p>Bio: " + bio + "</p>");

        writer.println("</body></html>");
        writer.close();

    }
}
