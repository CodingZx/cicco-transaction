package lol.cicco;

import org.junit.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.sql.DataSource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application implements InitializingBean {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Qualifier("test1DataSource")
    @Autowired
    private DataSource ds1;
    @Qualifier("test2DataSource")
    @Autowired
    private DataSource ds2;

    @Override
    public void afterPropertiesSet() throws Exception {
        try (var conn = ds1.getConnection()){
            var stat = conn.createStatement();
            stat.execute("create table t_test1(id int not null auto_increment, name varchar(255) not null);");
            conn.commit();
        }

        try (var conn = ds2.getConnection()){
            var stat = conn.createStatement();
            stat.execute("create table t_test2(id int not null auto_increment, name varchar(255) not null);");
            conn.commit();
        }

    }
}
