package iuh.fit.se.bai_1.Controller;

import iuh.fit.se.bai_1.dao.ProductDAO;
import iuh.fit.se.bai_1.dao.ProductDAOImpl;
import iuh.fit.se.bai_1.entities.Product;
import iuh.fit.se.bai_1.utils.EntityManagerFactoryUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/simple-list")
public class SimpleListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Tạo EntityManager
        EntityManager em = EntityManagerFactoryUtil.getEntityManager();
        
        // Tạo DAO
        ProductDAO productDAO = new ProductDAOImpl(em);
        
        // Lấy danh sách sản phẩm
        List<Product> products = productDAO.findAll();
        
        // Đóng EntityManager
        em.close();
        
        // Đặt dữ liệu vào request
        req.setAttribute("products", products);
        
        // Chuyển đến JSP
        req.getRequestDispatcher("views/product/simple.jsp").forward(req, resp);
    }
}
