<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách điện thoại</title>
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
            max-width: 1000px;
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
        
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin: 5px;
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
        
        .btn-warning {
            background-color: #ffc107;
            color: #333;
        }
        
        .btn-warning:hover {
            background-color: #e0a800;
        }
        
        .btn-secondary {
            background-color: #6c757d;
        }
        
        .btn-secondary:hover {
            background-color: #545b62;
        }
        
        .stats {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
            text-align: center;
            border: 1px solid #dee2e6;
        }
        
        table {
            border-collapse: collapse;
            width: 100%;
            margin: 20px 0;
        }
        
        th, td {
            border: 1px solid #dee2e6;
            padding: 12px;
            text-align: left;
        }
        
        th {
            background-color: #f8f9fa;
            font-weight: bold;
            color: #333;
        }
        
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        
        tr:hover {
            background-color: #f5f5f5;
        }
        
        .phone-image {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 4px;
        }
        
        .no-data {
            text-align: center;
            color: #6c757d;
            padding: 40px;
            background: #f8f9fa;
            border-radius: 4px;
        }
        
        .success-message {
            background: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        
        .error-message {
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Danh sách điện thoại</h1>
    
    <div class="stats">
        <strong>Tổng số điện thoại: ${dsDienThoai.size()}</strong>
    </div>
    
    <c:if test="${not empty param.success}">
        <c:choose>
            <c:when test="${param.success == 'add'}">
                <div class="success-message">Thêm điện thoại thành công!</div>
            </c:when>
            <c:when test="${param.success == 'edit'}">
                <div class="success-message">Cập nhật điện thoại thành công!</div>
            </c:when>
            <c:when test="${param.success == 'delete'}">
                <div class="success-message">Xóa điện thoại thành công!</div>
            </c:when>
        </c:choose>
    </c:if>
    
    <c:if test="${not empty param.error}">
        <c:choose>
            <c:when test="${param.error == 'missing_id'}">
                <div class="error-message">Thiếu mã điện thoại!</div>
            </c:when>
            <c:when test="${param.error == 'not_found'}">
                <div class="error-message">Không tìm thấy điện thoại!</div>
            </c:when>
            <c:when test="${param.error == 'delete_failed'}">
                <div class="error-message">Lỗi khi xóa điện thoại!</div>
            </c:when>
        </c:choose>
    </c:if>
    
    <div style="text-align: center; margin-bottom: 20px;">
        <a href="manage-dien-thoai" class="btn btn-warning">Quản lý điện thoại</a>
        <a href="add-dien-thoai" class="btn btn-success">Thêm điện thoại mới</a>
        <a href="index.jsp" class="btn btn-secondary">Quay lại trang chủ</a>
    </div>

    <c:choose>
        <c:when test="${empty dsDienThoai}">
            <div class="no-data">
                <h3>Không có dữ liệu điện thoại</h3>
                <p>Hiện tại chưa có điện thoại nào trong hệ thống.</p>
            </div>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>Mã ĐT</th>
                        <th>Tên điện thoại</th>
                        <th>Năm sản xuất</th>
                        <th>Cấu hình</th>
                        <th>Nhà cung cấp</th>
                        <th>Hình ảnh</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="dt" items="${dsDienThoai}">
                        <tr>
                            <td><strong>${dt.madt}</strong></td>
                            <td>${dt.tendt}</td>
                            <td>${dt.namsanxuat}</td>
                            <td>${dt.cauhinh}</td>
                            <td>${dt.nhaCungCap.tennhacc}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty dt.hinhanh}">
                                        <img src="images/${dt.hinhanh}" class="phone-image" alt="${dt.tendt}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #999;">Không có hình</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
    <div class="student-name">
        <p>Trần Thị Quỳnh Như - 21058591 - LTWWW_Java_420300362101_DHKTPM18</p>
    </div>
</div>
</body>
</html>