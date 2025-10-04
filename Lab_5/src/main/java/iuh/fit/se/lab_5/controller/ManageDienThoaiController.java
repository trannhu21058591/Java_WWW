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
import java.util.List;

@WebServlet("/manage-dien-thoai")
public class ManageDienThoaiController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        EntityManager entityManager = EntityManagerFactoryUtil.getEntityManager();
        DienThoaiDAO dienThoaiDAO = new DienThoaiDAOImpl(entityManager);
        
        try {
            // Lấy danh sách điện thoại từ database
            List<DienThoai> dsDienThoai = dienThoaiDAO.findAll();
            
            // Đặt danh sách vào request attribute
            request.setAttribute("dsDienThoai", dsDienThoai);
            
            // Chuyển hướng đến trang quản lý
            request.getRequestDispatcher("/view/manageDienThoai.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Lỗi khi lấy danh sách điện thoại: " + e.getMessage());
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
