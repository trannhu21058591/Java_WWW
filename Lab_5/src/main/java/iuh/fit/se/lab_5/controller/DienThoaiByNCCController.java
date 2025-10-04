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

@WebServlet("/dien-thoai-by-ncc")
public class DienThoaiByNCCController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String mancc = request.getParameter("mancc");
        
        if (mancc == null || mancc.trim().isEmpty()) {
            response.sendRedirect("dien-thoai");
            return;
        }
        
        EntityManager entityManager = EntityManagerFactoryUtil.getEntityManager();
        DienThoaiDAO dienThoaiDAO = new DienThoaiDAOImpl(entityManager);
        
        try {
            // Lấy danh sách điện thoại theo nhà cung cấp
            List<DienThoai> dsDienThoai = dienThoaiDAO.findByNCC(mancc);
            
            // Đặt danh sách vào request attribute
            request.setAttribute("dsDienThoai", dsDienThoai);
            request.setAttribute("mancc", mancc);
            
            // Chuyển hướng đến view
            request.getRequestDispatcher("/view/listDienThoai.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Lỗi khi lấy danh sách điện thoại theo nhà cung cấp: " + e.getMessage());
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
