package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String URL = "jdbc:mysql://localhost/test";
    private static final String USER = "root";
    private static final String PASS = "";

    private static SessionFactory sf = null;

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sf == null) {
            try {
                Configuration cfg = new Configuration();

                cfg.setProperties(getProperties());
                cfg.addAnnotatedClass(User.class);

                StandardServiceRegistry sR = new StandardServiceRegistryBuilder()
                        .applySettings(cfg.getProperties())
                        .build();
                sf = cfg.buildSessionFactory(sR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return sf;
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.URL, URL);
        properties.put(Environment.USER, USER);
        properties.put(Environment.PASS, PASS);

        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");

        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "");
        return properties;
    }
}
