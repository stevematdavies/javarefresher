package application;

import handlers.DAOException;
import interfaces.IUserDao;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements IUserDao {
    @Override
    public void save(User user) {
        Connection conn = Database.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("insert into user (name) values (?)");
            stmt.setString(1, user.getName());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public void update(User user) {
        Connection conn = Database.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("update user set name=? where id=?");
            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e){
            throw new DAOException(e);
        }
        System.out.println("User successfully updated!");
    }

    @Override
    public void delete(User user) {
        Connection conn = Database.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("delete from user where id=?");
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        Connection conn = Database.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("select * from user where id=?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                User user = new User(id, name);
                return Optional.of(user);
            }
        } catch (SQLException e){
            throw new DAOException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<User>();
        Connection conn = Database.getInstance().getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from user");
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                users.add(new User(id, name));
            }
        } catch (SQLException e){
            throw new DAOException(e);
        }
        return users;
    }
}
