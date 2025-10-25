package iuh.fit.se.bai_1.Cach_3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class SpringBootJdbcApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJdbcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("onnected to DB successfully!!!!");

        String sql = "SELECT * FROM employees";
        jdbcTemplate.query(sql, (rs, rowNum) -> {
            System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));
            return null;
        });
    }
}