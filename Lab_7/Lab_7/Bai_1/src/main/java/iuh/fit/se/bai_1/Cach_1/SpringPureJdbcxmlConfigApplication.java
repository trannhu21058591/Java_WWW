package iuh.fit.se.bai_1.Cach_1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

/**
 * cách 1 kết nối db: XML configuration.
 */
public class SpringPureJdbcxmlConfigApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("databaseConfig.xml");
        DataSource dataSource = (DataSource) context.getBean("dataSource");

        System.out.println("Connected to DB: " + dataSource.getConnection().getCatalog());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = "SELECT * FROM employees";
        jdbcTemplate.query(sql, (rs, rowNum) -> {
            System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));
            return null;
        });
    }
}
