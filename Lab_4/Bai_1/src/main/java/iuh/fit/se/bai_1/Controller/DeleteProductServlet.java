package iuh.fit.se.bai_1.Controller;

import iuh.fit.se.bai_1.dao.ProductDAO;
import iuh.fit.se.bai_1.dao.ProductDAOImpl;
import iuh.fit.se.bai_1.utils.EntityManagerFactoryUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete-product")
public class DeleteProductServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        
        // Lấy ID từ URL parameter
        String idStr = req.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            resp.getWriter().println("<h2>Loi: Khong co ID</h2>");
            resp.getWriter().println("<a href='" + req.getContextPath() + "/simple-list'>Quay lai</a>");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            // Tạo EntityManager
            EntityManager em = EntityManagerFactoryUtil.getEntityManager();
            
            // Bắt đầu transaction
            em.getTransaction().begin();
            
            // Tạo DAO và xóa sản phẩm
            ProductDAO productDAO = new ProductDAOImpl(em);
            productDAO.delete(id);
            
            // Commit transaction
            em.getTransaction().commit();
            
            // Đóng EntityManager
            em.close();
            
            // Hiển thị kết quả
            resp.getWriter().println("<h2>Xoa thanh cong!</h2>");
            resp.getWriter().println("Da xoa san pham co ID: " + id);
            resp.getWriter().println("<br><br><a href='" + req.getContextPath() + "/simple-list'>Xem danh sach</a>");
            
        } catch (NumberFormatException e) {
            resp.getWriter().println("<h2>Loi: ID khong hop le</h2>");
            resp.getWriter().println("<a href='" + req.getContextPath() + "/simple-list'>Quay lai</a>");
        } catch (Exception e) {
            resp.getWriter().println("<h2>Loi: " + e.getMessage() + "</h2>");
            resp.getWriter().println("<a href='" + req.getContextPath() + "/simple-list'>Quay lai</a>");
        }
    }
}
