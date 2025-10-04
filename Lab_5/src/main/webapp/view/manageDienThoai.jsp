<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Quản lý điện thoại</title>
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
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid #dee2e6;
        }
        
        .header h1 {
            color: #333;
            margin: 0;
        }
        
        .header-actions {
            display: flex;
            gap: 10px;
        }
        
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
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
        
        .stats {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 30px;
            text-align: center;
            border: 1px solid #dee2e6;
        }
        
        .stats h3 {
            margin: 0 0 10px 0;
            color: #333;
        }
        
        .count {
            font-size: 2em;
            font-weight: bold;
            color: #007bff;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #dee2e6;
        }
        
        th {
            background-color: #f8f9fa;
            font-weight: bold;
            color: #333;
        }
        
        tr:hover {
            background-color: #f8f9fa;
        }
        
        .phone-image {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 4px;
        }
        
        .no-data {
            text-align: center;
            color: #6c757d;
            padding: 40px;
            background: #f8f9fa;
            border-radius: 5px;
        }
        
        .action-buttons {
            display: flex;
            gap: 5px;
        }
        
        .action-btn {
            padding: 5px 10px;
            font-size: 12px;
            border-radius: 3px;
            text-decoration: none;
        }
        
        .edit-btn {
            background-color: #ffc107;
            color: #333;
        }
        
        .edit-btn:hover {
            background-color: #e0a800;
        }
        
        .delete-btn {
            background-color: #dc3545;
            color: white;
        }
        
        .delete-btn:hover {
            background-color: #c82333;
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
    <div class="header">
        <h1>Quản lý điện thoại</h1>
        <div class="header-actions">
            <a href="add-dien-thoai" class="btn btn-success">Thêm mới</a>
            <a href="index.jsp" class="btn btn-secondary">Trang chủ</a>
        </div>
    </div>
    
    <div class="stats">
        <h3>Tổng số điện thoại trong hệ thống</h3>
        <div class="count">${dsDienThoai.size()}</div>
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
    
    <c:choose>
        <c:when test="${empty dsDienThoai}">
            <div class="no-data">
                <h3>Chưa có điện thoại nào</h3>
                <p>Hệ thống chưa có điện thoại nào. Hãy thêm điện thoại đầu tiên!</p>
                <a href="add-dien-thoai" class="btn btn-success" style="margin-top: 20px;">Thêm điện thoại đầu tiên</a>
            </div>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>Hình ảnh</th>
                        <th>Mã ĐT</th>
                        <th>Tên điện thoại</th>
                        <th>Năm SX</th>
                        <th>Cấu hình</th>
                        <th>Nhà cung cấp</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="dt" items="${dsDienThoai}">
                        <tr>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty dt.hinhanh}">
                                        <img src="images/${dt.hinhanh}" class="phone-image" alt="${dt.tendt}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="width: 50px; height: 50px; background: #f8f9fa; border-radius: 4px; display: flex; align-items: center; justify-content: center; color: #6c757d;">
                                            No Image
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><strong>${dt.madt}</strong></td>
                            <td>${dt.tendt}</td>
                            <td>${dt.namsanxuat}</td>
                            <td style="max-width: 200px; word-wrap: break-word;">${dt.cauhinh}</td>
                            <td>${dt.nhaCungCap.tennhacc}</td>
                            <td>
                                <div class="action-buttons">
                                    <a href="edit-dien-thoai?madt=${dt.madt}" class="action-btn edit-btn">Sửa</a>
                                    <a href="delete-dien-thoai?madt=${dt.madt}" class="action-btn delete-btn" 
                                       onclick="return confirm('Bạn có chắc chắn muốn xóa điện thoại ${dt.tendt}?')">Xóa</a>
                                </div>
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
