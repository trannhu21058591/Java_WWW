package iuh.fit.se.lab_1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "bai2", value = "/bai2")
public class Bai2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        PrintWriter writer = resp.getWriter();
        writer.println("<html><body>");
        writer.println("<h1>Đây là bài 2</h1>");
        writer.println("</body></html>");

        writer.close();
    }
}
