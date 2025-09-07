package iuh.fit.se.bai_1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "registration-form", value = "/registration-form")
public class registrationForm extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public registrationForm() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().append("Server at: ").append(req.getContextPath());

        String fname = req.getParameter("firstName");
        String lname = req.getParameter("lastName");
        String date = req.getParameter("dateOfBirth");
        String email = req.getParameter("email");
        String phone = req.getParameter("mobileNumber");
        String gender = req.getParameter("gender");
        String address = req.getParameter("address");
        String city = req.getParameter("city");
        String pinCode = req.getParameter("pinCode");
        String state = req.getParameter("state");
        String country = req.getParameter("country");
        String hobbies = req.getParameter("hobbies");
        String classXBoard = req.getParameter("classXBoard");
        String classXPercent = req.getParameter("classXPercent");
        String classXYear = req.getParameter("classXYear");
        String classXIIBoard = req.getParameter("classXIIBoard");
        String classXIIPercent = req.getParameter("classXIIPercent");
        String classXIIYear = req.getParameter("classXIIYear");
        String gradBoard = req.getParameter("gradBoard");
        String gradPercent = req.getParameter("gradPercent");
        String gradYear = req.getParameter("gradYear");
        String mastersBoard = req.getParameter("mastersBoard");
        String mastersPercent = req.getParameter("mastersPercent");
        String mastersYear = req.getParameter("mastersYear");
        String course = req.getParameter("course");


        Student sv = new Student();
        sv.setFirstName(fname);
        sv.setLastName(lname);
        sv.setDateOfBirth(date);
        sv.setEmail(email);


        req.setAttribute("student", sv);

        RequestDispatcher rd = req.getRequestDispatcher("result-form.jsp");
        rd.forward(req,resp);

    }
}
