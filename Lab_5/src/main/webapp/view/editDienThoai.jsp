<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Sửa điện thoại</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .student-name{
            display: flex;
            justify-content: center;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        
        input[type="text"], input[type="number"], select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            box-sizing: border-box;
        }
        
        input[type="text"]:focus, input[type="number"]:focus, select:focus {
            outline: none;
            border-color: #007bff;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 24px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border: none;
            border-radius: 4px;
            margin: 10px 5px;
            cursor: pointer;
            font-size: 16px;
        }
        
        .btn:hover {
            background-color: #0056b3;
        }
        
        .btn-secondary {
            background-color: #6c757d;
        }
        
        .btn-secondary:hover {
            background-color: #545b62;
        }
        
        .form-actions {
            text-align: center;
            margin-top: 30px;
        }
        
        .error {
            color: #dc3545;
            font-size: 14px;
            margin-top: 5px;
        }
        
        .success {
            color: #28a745;
            font-size: 14px;
            margin-top: 5px;
        }
        
        .readonly-field {
            background-color: #f8f9fa;
            color: #6c757d;
        }
        
        .help-text {
            color: #666;
            font-size: 14px;
            margin-top: 5px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Sửa điện thoại</h1>
    
    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>
    
    <c:if test="${not empty successMessage}">
        <div class="success">${successMessage}</div>
    </c:if>
    
    <c:if test="${not empty dienThoai}">
        <form action="edit-dien-thoai" method="post" enctype="multipart/form-data">
            <input type="hidden" name="madt" value="${dienThoai.madt}">
            
            <div class="form-group">
                <label for="madt">Mã điện thoại</label>
                <input type="text" id="madt" value="${dienThoai.madt}" 
                       class="readonly-field" readonly>
                <div class="help-text">Mã điện thoại không thể thay đổi</div>
            </div>
            
            <div class="form-group">
                <label for="tendt">Tên điện thoại *</label>
                <input type="text" id="tendt" name="tendt" required 
                       value="${dienThoai.tendt}" placeholder="VD: iPhone 15 Pro">
            </div>
            
            <div class="form-group">
                <label for="namsanxuat">Năm sản xuất *</label>
                <input type="number" id="namsanxuat" name="namsanxuat" required 
                       value="${dienThoai.namsanxuat}" min="2000" max="2030" placeholder="VD: 2024">
            </div>
            
            <div class="form-group">
                <label for="cauhinh">Cấu hình *</label>
                <input type="text" id="cauhinh" name="cauhinh" required 
                       value="${dienThoai.cauhinh}" placeholder="VD: 6.1 inch, A17 Pro, 128GB">
            </div>
            
        <div class="form-group">
            <label for="imageFile">Hình ảnh điện thoại</label>
            <input type="file" id="imageFile" name="imageFile" accept="image/*">
            <div class="help-text">Chọn file ảnh mới (JPG, PNG, GIF) - Tối đa 10MB</div>
            <c:if test="${not empty dienThoai.hinhanh}">
                <div style="margin-top: 10px;">
                    <p>Ảnh hiện tại:</p>
                    <img src="images/${dienThoai.hinhanh}" style="max-width: 200px; max-height: 200px; border: 1px solid #ddd; border-radius: 4px;">
                </div>
            </c:if>
        </div>
            
            <div class="form-group">
                <label for="mancc">Nhà cung cấp *</label>
                <select id="mancc" name="mancc" required>
                    <option value="">-- Chọn nhà cung cấp --</option>
                    <c:forEach var="ncc" items="${dsNhaCungCap}">
                        <option value="${ncc.mancc}" 
                                ${dienThoai.nhaCungCap.mancc == ncc.mancc ? 'selected' : ''}>
                            ${ncc.tennhacc}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn">Cập nhật điện thoại</button>
                <a href="dien-thoai" class="btn btn-secondary">Hủy</a>
            </div>
        </form>
    </c:if>
    
    <c:if test="${empty dienThoai}">
        <div class="error">
            <h3>Không tìm thấy điện thoại</h3>
            <p>Điện thoại bạn muốn sửa không tồn tại hoặc đã bị xóa.</p>
            <a href="dien-thoai" class="btn btn-secondary">Quay lại danh sách</a>
        </div>
    </c:if>

</div>
<div class="student-name">
    <p>Trần Thị Quỳnh Như - 21058591 - LTWWW_Java_420300362101_DHKTPM18</p>
</div>
</body>
</html>