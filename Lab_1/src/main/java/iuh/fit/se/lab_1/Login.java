package iuh.fit.se.lab_1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "login", value = "/login")
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Lấy username từ query string
        String username = req.getParameter("username");

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();

        writer.println("<html><body>");
        writer.println("<h3>GET method</h3>");
        if (username != null && !username.isEmpty()) {
            writer.println("Username: <strong>" + username + "</strong>");
        } else {
            writer.println("Chưa nhập username.");
        }
        writer.println("</body></html>");

        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Lấy username từ body form
        String username = req.getParameter("username");

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();

        writer.println("<html><body>");
        writer.println("<h3>POST method</h3>");
        if (username != null && !username.isEmpty()) {
            writer.println("Username: <strong>" + username + "</strong>");
        } else {
            writer.println("Chưa nhập username.");
        }
        writer.println("</body></html>");

        writer.close();
    }
}
