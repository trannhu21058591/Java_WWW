package iuh.fit.se.lab_1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "bai3", value = "/bai3")
public class Bai3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = new User("Nguyen Van A", 20, "Male", "99 Nguyen Thai Son");
        resp.setContentType("application/json");

        // Dùng Jackson để chuyển Object -> JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);

        // Ghi ra response
        resp.getWriter().write(jsonString);

    }
}
