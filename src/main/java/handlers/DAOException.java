package handlers;

import java.sql.SQLException;

public class DAOException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DAOException(SQLException sqle){
        super(sqle);
    }

}
