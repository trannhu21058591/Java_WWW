package iuh.fit.se.bai_1.Cach_2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

public class SpringPureJdbcJavaConfigApplication {
    public static void main(String[] args) throws Exception {
        // Load cấu hình từ AppConfig
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println(" Connected to DB: " + dataSource.getConnection().getCatalog());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // Truy vấn dữ liệu
        String sql = "SELECT * FROM employees";
        jdbcTemplate.query(sql, (rs, rowNum) -> {
            System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));
            return null;
        });

        context.close();
    }
}