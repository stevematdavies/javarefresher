package application;

import com.sun.javafx.runtime.SystemProperties;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class App {
    public static void main(String[] args) {

        Properties props = Profile.getProperties("db");

        Database db = Database.getInstance();
        try {
            db.connect(props);
        } catch (SQLException e) {
            System.err.println("Cannot connect to the database!");
            throw new RuntimeException(e);
        }
        System.out.println("Connected!");
        UserDaoImpl userDao = new UserDaoImpl();

        List<User> selectedUsers = userDao.getAll();

        for(User u : selectedUsers){
            System.out.printf("Id: %d User: %s\n", u.getId(), u.getName());
        }

        Optional<User> user = userDao.findById(2);
        user.ifPresent(User -> System.out.printf("User found with id %d and name %s\n", User.getId(), User.getName()));


        System.out.println("Updating Pluto's name");
        Optional<User> pluto = userDao.findById(8);

        if (pluto.isPresent()){
            User u = pluto.get();
            u.setName("Pluto, (almost a planet)");
            userDao.update(u);
        } else {
            System.out.println("No user present with this id");
        }


        System.out.println("Updated!");

        try {
            db.close();
        } catch (SQLException e){
            System.err.println("Cannot close database connection");
        }

    }
}
