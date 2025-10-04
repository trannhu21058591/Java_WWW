# Lab 5 - Quản lý điện thoại

Ứng dụng web quản lý điện thoại sử dụng Java Servlet, JSP và JPA.

## Cấu trúc dự án

```
src/
├── main/
│   ├── java/
│   │   └── iuh/fit/se/lab_5/
│   │       ├── controller/          # Các servlet controller
│   │       │   ├── DienThoaiController.java
│   │       │   └── DienThoaiByNCCController.java
│   │       ├── dao/                 # Data Access Object
│   │       │   ├── DienThoaiDAO.java
│   │       │   └── impl/
│   │       │       └── DienThoaiDAOImpl.java
│   │       ├── entities/            # Các entity JPA
│   │       │   ├── DienThoai.java
│   │       │   └── NhaCungCap.java
│   │       └── utils/
│   │           └── EntityManagerFactoryUtil.java
│   ├── resources/
│   │   └── META-INF/
│   │       └── persistence.xml      # Cấu hình JPA
│   └── webapp/
│       ├── images/                  # Thư mục chứa hình ảnh điện thoại
│       ├── view/
│       │   └── listDienThoai.jsp    # View hiển thị danh sách điện thoại
│       ├── index.jsp                # Trang chủ
│       └── WEB-INF/
│           └── web.xml              # Cấu hình web application
```

## Tính năng

1. **Xem danh sách điện thoại**: Hiển thị tất cả điện thoại trong hệ thống
2. **Lọc theo nhà cung cấp**: Xem điện thoại theo nhà cung cấp cụ thể
3. **Giao diện responsive**: Thiết kế đẹp mắt, thân thiện với người dùng

## Cách sử dụng

### 1. Chạy ứng dụng

```bash
# Sử dụng Maven wrapper
./mvnw clean compile
./mvnw tomcat7:run

# Hoặc sử dụng Maven trực tiếp
mvn clean compile
mvn tomcat7:run
```

### 2. Truy cập ứng dụng

- **Trang chủ**: `http://localhost:8080/lab_5/`
- **Danh sách điện thoại**: `http://localhost:8080/lab_5/dien-thoai`
- **Lọc theo nhà cung cấp**: `http://localhost:8080/lab_5/dien-thoai-by-ncc?mancc=MANCC001`

### 3. Các URL endpoint

| URL | Mô tả |
|-----|-------|
| `/` | Trang chủ |
| `/dien-thoai` | Danh sách tất cả điện thoại |
| `/dien-thoai-by-ncc?mancc=XXX` | Danh sách điện thoại theo nhà cung cấp |

## Cấu hình Database

Đảm bảo cấu hình database trong file `src/main/resources/META-INF/persistence.xml`:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/your_database"/>
<property name="jakarta.persistence.jdbc.user" value="your_username"/>
<property name="jakarta.persistence.jdbc.password" value="your_password"/>
```

## Yêu cầu hệ thống

- Java 17+
- Maven 3.6+
- MySQL 8.0+ (hoặc database tương thích)
- Tomcat 9.0+ (hoặc servlet container tương thích)

## Công nghệ sử dụng

- **Backend**: Java Servlet, JPA/Hibernate
- **Frontend**: JSP, HTML, CSS, JavaScript
- **Database**: MySQL
- **Build Tool**: Maven
- **Server**: Apache Tomcat
