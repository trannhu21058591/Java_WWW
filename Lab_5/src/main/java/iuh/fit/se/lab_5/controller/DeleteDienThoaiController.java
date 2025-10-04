package iuh.fit.se.lab_5.controller;

import iuh.fit.se.lab_5.dao.DienThoaiDAO;
import iuh.fit.se.lab_5.dao.impl.DienThoaiDAOImpl;
import iuh.fit.se.lab_5.entities.DienThoai;
import iuh.fit.se.lab_5.utils.EntityManagerFactoryUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete-dien-thoai")
public class DeleteDienThoaiController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String madt = request.getParameter("madt");
        
        if (madt == null || madt.trim().isEmpty()) {
            response.sendRedirect("manage-dien-thoai?error=missing_id");
            return;
        }
        
        EntityManager entityManager = null;
        
        try {
            entityManager = EntityManagerFactoryUtil.getEntityManager();
            DienThoaiDAO dienThoaiDAO = new DienThoaiDAOImpl(entityManager);
            
            // Kiểm tra xem điện thoại có tồn tại không
            DienThoai dienThoai = entityManager.find(DienThoai.class, madt);
            if (dienThoai == null) {
                response.sendRedirect("manage-dien-thoai?error=not_found");
                return;
            }
            
            // Xóa điện thoại
            dienThoaiDAO.delete(madt);
            
            // Chuyển hướng về trang quản lý với thông báo thành công
            response.sendRedirect("manage-dien-thoai?success=delete");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-dien-thoai?error=delete_failed");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
