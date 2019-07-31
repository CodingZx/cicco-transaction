package lol.cicco.test;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DataSourceTest {

    @Qualifier("test1DataSource")
    @Autowired
    private DataSource ds1;
    @Qualifier("test2DataSource")
    @Autowired
    private DataSource ds2;

    @Test
    public void testDataSource() throws Exception {
        Assert.assertNotNull(ds1);
        Assert.assertNotNull(ds2);


        try (var conn = ds1.getConnection()){
            var stat = conn.createStatement();
            stat.execute("insert into t_test1(name) values('aaa')");
            conn.commit();
        }

        try (var conn = ds1.getConnection()){
            var stat = conn.createStatement();
            var rs = stat.executeQuery("select name from t_test1");
            while(rs.next()) {
                var col = rs.getString("name");
                Assert.assertEquals(col,"aaa");
            }
        }

        try (var conn = ds2.getConnection()){
            var stat = conn.createStatement();
            stat.execute("insert into t_test2(name) values('aaa')");
            conn.commit();
        }

        try (var conn = ds2.getConnection()){
            var stat = conn.createStatement();
            var rs = stat.executeQuery("select name from t_test2");
            while(rs.next()) {
                var col = rs.getString("name");
                Assert.assertEquals(col,"aaa");
            }
        }

        try (var conn = ds2.getConnection()){
            var stat = conn.createStatement();
            var rs = stat.executeQuery("select 1 from t_test1 limit 1");

            rs.next();
            Assert.assertEquals(1, rs.getInt(1));
        }

        try (var conn = ds1.getConnection()){
            var stat = conn.createStatement();
            var rs = stat.executeQuery("select 1 from t_test1 limit 1");

            rs.next();
            Assert.assertEquals(1, rs.getInt(1));
        }

        try (var conn = ds2.getConnection()){
            var stat = conn.createStatement();
            var rs = stat.executeQuery("select 1 from t_test2 limit 1");

            rs.next();
            Assert.assertEquals(1, rs.getInt(1));
        }

        try (var conn = ds1.getConnection()){
            var stat = conn.createStatement();
            stat.execute("delete from t_test1");
            conn.commit();
        }

        try (var conn = ds2.getConnection()){
            var stat = conn.createStatement();
            stat.execute("delete from t_test2");
            conn.commit();
        }
    }
}
