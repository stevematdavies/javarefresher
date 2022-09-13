package application;

import java.io.IOException;
import java.util.Properties;

public class Profile {
    public static Properties getProperties(String name){
        Properties props = new Properties();
        String env = System.getProperty("env") != null ? System.getProperty("env") : "dev";
        String propPath = String.format("/config/%s.%s.properties", name, env);
        try {
            props.load(App.class.getResourceAsStream(propPath));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot load properties file: %s", propPath));
        }
        return props;

    }
}
