package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    final String saveUser = "INSERT INTO users (name, lastName, age) VALUES (?,?,?)";
    final String removeUserById = "DELETE FROM users WHERE id =?";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement()) {

            st.execute("""
                        CREATE TABLE IF NOT EXISTS users
                        (id INTEGER NOT NULL AUTO_INCREMENT,
                        name VARCHAR(50),
                        lastName VARCHAR(50),
                        age TINYINT not NULL,
                        PRIMARY KEY (id))
                        """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement()) {

            st.execute("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection con = Util.getConnection();
             PreparedStatement prSt = con.prepareStatement(saveUser)) {

            prSt.setString(1, name);
            prSt.setString(2, lastName);
            prSt.setByte(3, age);
            prSt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (Connection con = Util.getConnection();
             PreparedStatement prSt = con.prepareStatement(removeUserById)) {

            prSt.setLong(1, id);
            prSt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try (Connection con = Util.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastName");
                byte age = rs.getByte("age");

                User user = new User(name, lastName, age);
                user.setId(id);

                userList.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement()) {
            st.execute("TRUNCATE users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
