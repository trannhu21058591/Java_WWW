package iuh.fit.se.bai_1;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationFilters extends HttpFilter implements Filter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        // lấy dữ liệu từ form login.html
        String u = req.getParameter("username");
        String p = req.getParameter("password");

        String usernameFilter = this.getFilterConfig().getInitParameter("usernameFilter1");
        String passwordFilter = this.getFilterConfig().getInitParameter("passwordFilter1");

        if(u.equals(usernameFilter) && p.equals(passwordFilter)){
            chain.doFilter(req,res);
        }
        else {
            res.setContentType("text/html");
            PrintWriter writer = res.getWriter();
            writer.println("Thông tin đăng nhập không đúng!");
            writer.close();
        }


    }
}
