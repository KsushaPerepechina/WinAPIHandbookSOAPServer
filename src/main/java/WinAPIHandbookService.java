import model.WinAPIFunction;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WinAPIHandbookService {
    private final String JDBC_NAME = "jdbc/winapi_handbook_db";
    private static final Logger log = LogManager.getLogger(WinAPIHandbookService.class);

    public WinAPIFunction[] getAllFunctions() {
        String getAllFunctionsQuery;
        ResultSet result;
        List<WinAPIFunction> functions = new ArrayList<>();
        try {
            getAllFunctionsQuery = "SELECT * " +
                    "FROM winapi_function;";
            Database db = new Database(JDBC_NAME);
            Statement stmt = db.getConnection().createStatement();
            result = stmt.executeQuery(getAllFunctionsQuery);
            while (result.next()) {
                functions.add(fromResultSetToWinAPIFunctionObject(result));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return functions.toArray(new WinAPIFunction[0]);
    }

    public void addFunction(WinAPIFunction func) {
        String addFunctionQuery;
        PreparedStatement preparedStatement;
        try {
            Database db = new Database(JDBC_NAME);
            addFunctionQuery = "INSERT INTO winapi_function(name, params, return_value, description) " +
                    "VALUES(?, ?, ?, ?);";
            preparedStatement = db.getConnection().prepareStatement(addFunctionQuery);
            preparedStatement.setString(1, func.getName());
            preparedStatement.setString(2, func.getParams());
            preparedStatement.setString(3, func.getReturnValue());
            preparedStatement.setString(4, func.getDescription());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void removeFunction(WinAPIFunction function) {
        String removeFunctionQuery;
        PreparedStatement preparedStatement;
        try {
            Database db = new Database(JDBC_NAME);
            removeFunctionQuery = "DELETE FROM winapi_function " +
                    "WHERE id=?;";
            preparedStatement = db.getConnection().prepareStatement(removeFunctionQuery);
            preparedStatement.setInt(1, function.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void updateFunction(WinAPIFunction func) {
        String updateFunctionQuery;
        PreparedStatement preparedStatement;
        try {
            Database db = new Database(JDBC_NAME);
            updateFunctionQuery = "UPDATE winapi_function " +
                    "SET name=?, params=?, return_value=?, description=? " +
                    "WHERE id=?;";
            preparedStatement = db.getConnection().prepareStatement(updateFunctionQuery);
            preparedStatement.setString(1, func.getName());
            preparedStatement.setString(2, func.getParams());
            preparedStatement.setString(3, func.getReturnValue());
            preparedStatement.setString(4, func.getDescription());
            preparedStatement.setInt(5,func.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private WinAPIFunction fromResultSetToWinAPIFunctionObject(ResultSet rs) throws SQLException {
        WinAPIFunction function = new WinAPIFunction();
        function.setId(rs.getInt("id"));
        function.setName(rs.getString("name"));
        function.setParams(rs.getString("params"));
        function.setReturnValue(rs.getString("return_value"));
        function.setDescription(rs.getString("description"));
        return function;
    }
}
