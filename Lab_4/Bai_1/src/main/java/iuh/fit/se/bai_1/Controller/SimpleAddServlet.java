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

@WebServlet("/simple-add")
public class SimpleAddServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println("<h2>Them san pham</h2>");
        resp.getWriter().println("<form method='POST'>");
        resp.getWriter().println("Ten: <input type='text' name='name'><br><br>");
        resp.getWriter().println("Gia: <input type='number' name='price'><br><br>");
        resp.getWriter().println("Anh: <input type='text' name='image' placeholder='vd: 120211050_p0_master1200.jpg'><br><br>");
        resp.getWriter().println("<input type='submit' value='Them'>");
        resp.getWriter().println("</form>");
        resp.getWriter().println("<br><a href='" + req.getContextPath() + "/products'>Xem danh sach</a>");
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String name = req.getParameter("name");
        String priceStr = req.getParameter("price");
        String image = req.getParameter("image");
        
        try {
            double price = Double.parseDouble(priceStr);
            
            // Tạo EntityManager
            EntityManager em = EntityManagerFactoryUtil.getEntityManager();
            
            // Bắt đầu transaction
            em.getTransaction().begin();
            
            // Tạo sản phẩm mới
            Product product = new Product(name, price, image);
            
            // Lưu vào database
            em.persist(product);
            
            // Commit transaction
            em.getTransaction().commit();
            
            // Đóng EntityManager
            em.close();
            
            // Hiển thị kết quả
            resp.getWriter().println("<h2>Thanh cong!</h2>");
            resp.getWriter().println("Da them: " + name + " - " + price);
            
            // Hiển thị ảnh nếu có
            if (image != null && !image.isEmpty()) {
                resp.getWriter().println("<br><br><img src='" + req.getContextPath() + "/images/" + image + "' style='width: 200px;'>");
            }
            
            resp.getWriter().println("<br><br><a href='" + req.getContextPath() + "/products'>Xem danh sach</a>");
            
        } catch (Exception e) {
            resp.getWriter().println("<h2>Loi: " + e.getMessage() + "</h2>");
        }
    }
}
