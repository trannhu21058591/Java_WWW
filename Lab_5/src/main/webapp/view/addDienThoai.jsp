<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Thêm điện thoại mới</title>
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
        
        .btn-success {
            background-color: #28a745;
        }
        
        .btn-success:hover {
            background-color: #218838;
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
        
        .help-text {
            color: #666;
            font-size: 14px;
            margin-top: 5px;
        }
        
    </style>
</head>
<body>
<div class="container">
    <h1>Thêm điện thoại mới</h1>
    
    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>
    
    <c:if test="${not empty successMessage}">
        <div class="success">${successMessage}</div>
    </c:if>
    
    <form action="add-dien-thoai" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="madt">Mã điện thoại *</label>
            <input type="text" id="madt" name="madt" required 
                   value="${param.madt}" placeholder="VD: DT001">
        </div>
        
        <div class="form-group">
            <label for="tendt">Tên điện thoại *</label>
            <input type="text" id="tendt" name="tendt" required 
                   value="${param.tendt}" placeholder="VD: iPhone 15 Pro">
        </div>
        
        <div class="form-group">
            <label for="namsanxuat">Năm sản xuất *</label>
            <input type="number" id="namsanxuat" name="namsanxuat" required 
                   value="${param.namsanxuat}" min="2000" max="2030" placeholder="VD: 2024">
        </div>
        
        <div class="form-group">
            <label for="cauhinh">Cấu hình *</label>
            <input type="text" id="cauhinh" name="cauhinh" required 
                   value="${param.cauhinh}" placeholder="VD: 6.1 inch, A17 Pro, 128GB">
        </div>
        
        <div class="form-group">
            <label for="imageFile">Hình ảnh điện thoại</label>
            <input type="file" id="imageFile" name="imageFile" accept="image/*">
            <div class="help-text">Chọn file ảnh (JPG, PNG, GIF) - Tối đa 10MB</div>
        </div>
        
        <div class="form-group">
            <label for="mancc">Nhà cung cấp *</label>
            <select id="mancc" name="mancc" required>
                <option value="">-- Chọn nhà cung cấp --</option>
                <c:forEach var="ncc" items="${dsNhaCungCap}">
                    <option value="${ncc.mancc}" 
                            ${param.mancc == ncc.mancc ? 'selected' : ''}>
                        ${ncc.tennhacc}
                    </option>
                </c:forEach>
            </select>
        </div>
        
        <div class="form-actions">
            <button type="submit" class="btn btn-success">Thêm điện thoại</button>
            <a href="dien-thoai" class="btn btn-secondary">Hủy</a>
        </div>
    </form>
</div>
<div class="student-name">
    <p>Trần Thị Quỳnh Như - 21058591 - LTWWW_Java_420300362101_DHKTPM18</p>
</div>
</body>
</html>