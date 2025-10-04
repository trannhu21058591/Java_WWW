<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hệ thống quản lý điện thoại</title>
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
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 10px;
        }
        
        .subtitle {
            text-align: center;
            color: #666;
            margin-bottom: 40px;
        }
        
        .menu {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }
        
        .menu-item {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            border: 1px solid #dee2e6;
        }
        
        .menu-item h3 {
            margin: 0 0 10px 0;
            color: #333;
        }
        
        .menu-item p {
            margin: 0 0 15px 0;
            color: #666;
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
        
        .btn-warning {
            background-color: #ffc107;
            color: #333;
        }
        
        .btn-warning:hover {
            background-color: #e0a800;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Hệ thống quản lý điện thoại</h1>
    <p class="subtitle">Quản lý thông tin điện thoại một cách đơn giản và hiệu quả</p>
    
    <div class="menu">
        <div class="menu-item">
            <h3>Xem danh sách</h3>
            <p>Xem danh sách tất cả điện thoại trong hệ thống</p>
            <a href="dien-thoai" class="btn">Xem danh sách</a>
        </div>
        
        <div class="menu-item">
            <h3>Quản lý điện thoại</h3>
            <p>Quản lý điện thoại với đầy đủ chức năng thêm, sửa, xóa</p>
            <a href="manage-dien-thoai" class="btn btn-warning">Quản lý</a>
        </div>
        
        <div class="menu-item">
            <h3>Thêm điện thoại mới</h3>
            <p>Thêm điện thoại mới vào hệ thống</p>
            <a href="add-dien-thoai" class="btn btn-success">Thêm mới</a>
        </div>
    </div>
</div>
<div class="student-name">
    <p>Trần Thị Quỳnh Như - 21058591 - LTWWW_Java_420300362101_DHKTPM18</p>
</div>
</body>
</html>