<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Danh sách sản phẩm</title></head>
<body>
<h2>📦 Danh sách sản phẩm</h2>

<a href="${pageContext.request.contextPath}/simple-add">➕ Thêm sản phẩm</a><br><br>

<table border="1" style="width: 100%;">
  <tr style="background-color: #f0f0f0;">
    <th>ID</th>
    <th>Anh</th>
    <th>Ten</th>
    <th>Gia</th>
    <th>Thao tac</th>
  </tr>
  
  <c:forEach var="p" items="${products}">
    <tr>
      <td>${p.id}</td>
      <td>
        <c:if test="${not empty p.imagePath}">
          <img src="${pageContext.request.contextPath}/images/${p.imagePath}" 
               style="width: 80px; height: 80px; border: 1px solid #ccc;">
        </c:if>
        <c:if test="${empty p.imagePath}">
          <div style="width: 80px; height: 80px; background: #f0f0f0; border: 1px solid #ccc;">
            No Image
          </div>
        </c:if>
      </td>
      <td>${p.name}</td>
      <td>$${p.price}</td>
      <td>
        <a href="${pageContext.request.contextPath}/delete-product?id=${p.id}" 
           onclick="return confirm('Ban co chac chan muon xoa san pham nay?')"
           style="color: red; text-decoration: none;">Xoa</a>
      </td>
    </tr>
  </c:forEach>
</table>

<c:if test="${empty products}">
  <p>Chưa có sản phẩm nào. <a href="${pageContext.request.contextPath}/simple-add">Thêm sản phẩm đầu tiên</a></p>
</c:if>

</body>
</html>
