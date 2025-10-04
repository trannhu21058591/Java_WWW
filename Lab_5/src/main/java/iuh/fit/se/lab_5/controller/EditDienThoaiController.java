package iuh.fit.se.lab_5.controller;

import iuh.fit.se.lab_5.dao.DienThoaiDAO;
import iuh.fit.se.lab_5.dao.NhaCungCapDAO;
import iuh.fit.se.lab_5.dao.impl.DienThoaiDAOImpl;
import iuh.fit.se.lab_5.dao.impl.NhaCungCapDAOImpl;
import iuh.fit.se.lab_5.entities.DienThoai;
import iuh.fit.se.lab_5.entities.NhaCungCap;
import iuh.fit.se.lab_5.utils.EntityManagerFactoryUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/edit-dien-thoai")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class EditDienThoaiController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String madt = request.getParameter("madt");
        
        if (madt == null || madt.trim().isEmpty()) {
            response.sendRedirect("dien-thoai?error=missing_id");
            return;
        }
        
        EntityManager entityManager = EntityManagerFactoryUtil.getEntityManager();
        DienThoaiDAO dienThoaiDAO = new DienThoaiDAOImpl(entityManager);
        NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAOImpl(entityManager);
        
        try {
            // Lấy thông tin điện thoại cần sửa
            DienThoai dienThoai = entityManager.find(DienThoai.class, madt);
            
            if (dienThoai == null) {
                request.setAttribute("errorMessage", "Không tìm thấy điện thoại với mã: " + madt);
                request.getRequestDispatcher("/view/editDienThoai.jsp").forward(request, response);
                return;
            }
            
            // Lấy danh sách nhà cung cấp
            List<NhaCungCap> dsNhaCungCap = nhaCungCapDAO.findAll();
            
            request.setAttribute("dienThoai", dienThoai);
            request.setAttribute("dsNhaCungCap", dsNhaCungCap);
            
            // Chuyển hướng đến form sửa điện thoại
            request.getRequestDispatcher("/view/editDienThoai.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/view/editDienThoai.jsp").forward(request, response);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String madt = request.getParameter("madt");
        
        if (madt == null || madt.trim().isEmpty()) {
            response.sendRedirect("dien-thoai?error=missing_id");
            return;
        }
        
        EntityManager entityManager = EntityManagerFactoryUtil.getEntityManager();
        DienThoaiDAO dienThoaiDAO = new DienThoaiDAOImpl(entityManager);
        NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAOImpl(entityManager);
        
        try {
            // Lấy thông tin điện thoại hiện tại
            DienThoai dienThoai = entityManager.find(DienThoai.class, madt);
            
            if (dienThoai == null) {
                response.sendRedirect("dien-thoai?error=not_found");
                return;
            }
            
            // Lấy dữ liệu từ form
            String tendt = request.getParameter("tendt");
            int namsanxuat = Integer.parseInt(request.getParameter("namsanxuat"));
            String cauhinh = request.getParameter("cauhinh");
            String mancc = request.getParameter("mancc");
            
            // Xử lý upload ảnh
            String hinhanh = dienThoai.getHinhanh(); // Giữ ảnh cũ làm mặc định
            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                // Kiểm tra loại file
                String contentType = filePart.getContentType();
                if (!contentType.startsWith("image/")) {
                    request.setAttribute("errorMessage", "Chỉ được upload file ảnh!");
                    request.setAttribute("dienThoai", dienThoai);
                    request.setAttribute("dsNhaCungCap", nhaCungCapDAO.findAll());
                    request.getRequestDispatcher("/view/editDienThoai.jsp").forward(request, response);
                    return;
                }
                
                // Kiểm tra kích thước file (10MB)
                if (filePart.getSize() > 10 * 1024 * 1024) {
                    request.setAttribute("errorMessage", "File quá lớn! Vui lòng chọn file nhỏ hơn 10MB.");
                    request.setAttribute("dienThoai", dienThoai);
                    request.setAttribute("dsNhaCungCap", nhaCungCapDAO.findAll());
                    request.getRequestDispatcher("/view/editDienThoai.jsp").forward(request, response);
                    return;
                }
                
                // Upload file mới
                hinhanh = uploadImage(filePart, request);
            }
            
            // Kiểm tra dữ liệu đầu vào
            if (tendt == null || tendt.trim().isEmpty() ||
                cauhinh == null || cauhinh.trim().isEmpty() ||
                mancc == null || mancc.trim().isEmpty()) {
                
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin bắt buộc!");
                request.setAttribute("dienThoai", dienThoai);
                request.setAttribute("dsNhaCungCap", nhaCungCapDAO.findAll());
                request.getRequestDispatcher("/view/editDienThoai.jsp").forward(request, response);
                return;
            }
            
            // Lấy thông tin nhà cung cấp
            NhaCungCap nhaCungCap = nhaCungCapDAO.findById(mancc);
            if (nhaCungCap == null) {
                request.setAttribute("errorMessage", "Nhà cung cấp không tồn tại!");
                request.setAttribute("dienThoai", dienThoai);
                request.setAttribute("dsNhaCungCap", nhaCungCapDAO.findAll());
                request.getRequestDispatcher("/view/editDienThoai.jsp").forward(request, response);
                return;
            }
            
            // Cập nhật thông tin điện thoại
            dienThoai.setTendt(tendt.trim());
            dienThoai.setNamsanxuat(namsanxuat);
            dienThoai.setCauhinh(cauhinh.trim());
            dienThoai.setHinhanh(hinhanh != null ? hinhanh.trim() : "");
            dienThoai.setNhaCungCap(nhaCungCap);
            
            // Lưu thay đổi vào database
            entityManager.getTransaction().begin();
            entityManager.merge(dienThoai);
            entityManager.getTransaction().commit();
            
            // Chuyển hướng về danh sách điện thoại với thông báo thành công
            response.sendRedirect("dien-thoai?success=edit");
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Năm sản xuất phải là số!");
            try {
                DienThoai dienThoai = entityManager.find(DienThoai.class, madt);
                request.setAttribute("dienThoai", dienThoai);
                request.setAttribute("dsNhaCungCap", nhaCungCapDAO.findAll());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            request.getRequestDispatcher("/view/editDienThoai.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi cập nhật điện thoại: " + e.getMessage());
            try {
                DienThoai dienThoai = entityManager.find(DienThoai.class, madt);
                request.setAttribute("dienThoai", dienThoai);
                request.setAttribute("dsNhaCungCap", nhaCungCapDAO.findAll());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            request.getRequestDispatcher("/view/editDienThoai.jsp").forward(request, response);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
    
    private String uploadImage(Part filePart, HttpServletRequest request) throws IOException {
        // Lấy đường dẫn thực tế của thư mục webapp
        String appPath = request.getServletContext().getRealPath("");
        String uploadPath = appPath + File.separator + "images";
        
        // Tạo thư mục nếu chưa tồn tại
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Tạo tên file unique
        String originalFileName = getFileName(filePart);
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        
        // Lưu file
        String filePath = uploadPath + File.separator + uniqueFileName;
        filePart.write(filePath);
        
        return uniqueFileName;
    }
    
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            String[] tokens = contentDisposition.split(";");
            for (String token : tokens) {
                if (token.trim().startsWith("filename")) {
                    return token.substring(token.indexOf("=") + 2, token.length() - 1);
                }
            }
        }
        return null;
    }
}
