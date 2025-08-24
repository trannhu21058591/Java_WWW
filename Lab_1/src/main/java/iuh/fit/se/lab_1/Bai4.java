package iuh.fit.se.lab_1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "bai4", value = "/bai4")
public class Bai4 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();

        writer.println("<html><body>");
        writer.println("<h1>Từ cấu hình (init/context-param)</h1>");

        // Lấy init-param (khai báo trong <servlet> của web.xml)
        String username = getServletConfig().getInitParameter("username");
        String email = getServletConfig().getInitParameter("email");

        writer.println("<p>Init-param username = " + username + "</p>");
        writer.println("<p>Init-param email = " + email + "</p>");

        // Lấy context-param (khai báo trong <context-param> của web.xml)
        String appName = getServletContext().getInitParameter("appName");
        writer.println("<p>Context-param appName = " + appName + "</p>");

        // Form gửi dữ liệu POST
        writer.println("<h1>Lấy dữ liệu từ người dùng (request param) </h1>");

        writer.println("<form action='bai4' method='post'>");
        writer.println("Tên: <input name='name'>");
        writer.println("<button type='submit'>Gửi</button>");
        writer.println("</form>");

        writer.println("</body></html>");
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();

        String name = req.getParameter("name");
        writer.println("<html><body>");
        writer.println("<h2>Tôi là " + name + "!</h2>");
        writer.println("</body></html>");
        writer.close();
    }
}
