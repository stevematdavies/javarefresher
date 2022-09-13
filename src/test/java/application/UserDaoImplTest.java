package application;

import static org.junit.Assert.*;

import interfaces.IUserDao;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

public class UserDaoImplTest {

    private Connection conn;
    private List<String> testUsers;

    private List<String> loadTestUsers() throws IOException {
        List<String> us = new ArrayList<>();
        Stream<String> tmp = Files.lines(Paths.get("src/test/mockdata/names.txt"));
        tmp.forEach(us::add);
        return us;
    }

    private List<User> getUsersInRange(int minId, int maxId) throws SQLException {
       List<User> users = new ArrayList<>();
        PreparedStatement pstmt = conn.prepareStatement("select id, name from user where id >= ? and  id <= ?");
       pstmt.setInt(1, minId);
       pstmt.setInt(2, maxId);
       ResultSet rs = pstmt.executeQuery();
       while (rs.next()){
           users.add(new User(
                   rs.getInt("id"),
                   rs.getString("name")
           ));
       }
       return users;
    }


    private int getMaxId() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select max(id) as id from user");
        rs.next();
        int id = rs.getInt("id");
        stmt.close();
        return id;
    }

    private void clearTable() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("truncate table  user");
        stmt.close();
    }

    @Before
    public void setUp() throws SQLException, IOException {
        Properties props = Profile.getProperties("db");
        Database db = Database.getInstance();
        db.connect(props);
        conn = db.getConnection();
        conn.setAutoCommit(false);
        testUsers = loadTestUsers();

    }

    @After
    public void tearDown() throws SQLException {
        clearTable();
        Database.getInstance().close();

    }

    @Test
    public void testSave() throws SQLException {
        User testUser = new User("TestUser");
        IUserDao userDao = new UserDaoImpl();
        userDao.save(testUser);
        ResultSet rs = conn.createStatement().executeQuery("select  id, name from user order by id desc");
        assertTrue("Should return a user from the database!", rs.next());
        assertEquals("Username should match the one retrieved from the database", testUser.getName(), rs.getString("name"));
        clearTable();
    }



    @Test
    public void testSaveMultiple() throws SQLException {
        IUserDao userDao = new UserDaoImpl();
        for (String u : testUsers) {
            userDao.save(new User(u));
        }
        int maxId = getMaxId();
        List<User> retrieved = getUsersInRange(1, maxId);
        assertEquals("Max id must equal number of retrieved users",retrieved.size(), maxId);
    }

    @Test
    public void testFind() throws SQLException {
        User testUser = new User(1,"TestUser");
        IUserDao userDao = new UserDaoImpl();
        userDao.save(testUser);
        Optional<User> foundUser = userDao.findById(testUser.getId());
        assertEquals("found user Id should be same as added user", foundUser.get().getId(),testUser.getId());
    }

    @Test
    public void testUpdate() throws SQLException {
        User testUser = new User(1,"TestUser");
        IUserDao userDao = new UserDaoImpl();
        userDao.save(testUser);
        Optional<User> foundUser = userDao.findById(testUser.getId());
        assertEquals("found user name should be same as added user", foundUser.get().getName(),testUser.getName());
        foundUser.get().setName("NewTestUser");
        userDao.update(foundUser.get());
        Optional<User> updatedUser = userDao.findById(foundUser.get().getId());
        assertEquals("found user name should be same as updated user",  updatedUser.get().getName(),"NewTestUser");
    }
}

