package application;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Properties;



public class ProfileTest {

    @Test
    public void testLoadDbConfig(){
        Properties props = Profile.getProperties("db");
        String dbName = props.getProperty("database");
        assertNotNull("Properties file should not be null",props);
        assertEquals("Database name should be 'test'","users_test_db", dbName);
    }

}
