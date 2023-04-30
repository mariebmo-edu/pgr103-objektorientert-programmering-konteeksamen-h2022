package repo;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//Her lager vi en generisk repo-klasse som gjør universelle handlinger.
public abstract class AbstractRepo<T> {

    protected DataSource dataSource;
    protected String repo;

    public AbstractRepo(String repo) throws IOException {

        //Her bruker jeg en egen fil som kun ligger lokalt, for å lage en mysql-connection, i steden for å hardkode det inn.
        Properties properties = new Properties();
        FileReader fileReader = new FileReader("src/main/resources/environment.properties");
        properties.load(fileReader);

        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(properties.getProperty("REPO_URL"));
        mysqlDataSource.setUser(properties.getProperty("REPO_USER"));
        mysqlDataSource.setPassword(properties.getProperty("REPO_PASSWORD"));

        dataSource = mysqlDataSource;
        this.repo = repo;
    }

    public boolean ExecuteSQL(String sql) {

        try (Connection con = dataSource.getConnection()) {
            Statement s = con.createStatement();
            s.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<T> RetrieveAll(){

            ArrayList<T> list = new ArrayList<>();

            try (Connection con = dataSource.getConnection()) {
                Statement s = con.createStatement();
                ResultSet resultSet = s.executeQuery("SELECT * FROM " + repo);

                while (resultSet.next()) {
                    list.add(resultMapper(resultSet));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
    }

    public T RetrieveById(int id){

            T t = null;

            try (Connection con = dataSource.getConnection()) {
                Statement s = con.createStatement();
                ResultSet resultSet = s.executeQuery("SELECT * FROM " + repo + " WHERE id = " + id);

                if (resultSet.next()) {
                    t = resultMapper(resultSet);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return t;
    }

    public boolean DeleteById(int id){

            try (Connection con = dataSource.getConnection()) {
                Statement s = con.createStatement();
                s.execute("DELETE FROM " + repo + " WHERE id = " + id);

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
    }

    public boolean Update(T t){

            try (Connection con = dataSource.getConnection()) {
                String query = "UPDATE " + repo + " SET " + updateMapper(t) + " WHERE id = " + getId(t);
                PreparedStatement preparedStatement = con.prepareStatement(query);

                int parameterIndex = 1;
                for (Object o : modelValues(t).values()) {
                    preparedStatement.setObject(parameterIndex++, o);
                }

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
    }

    public boolean Insert(T t){

            try (Connection con = dataSource.getConnection()) {
                Statement s = con.createStatement();
                String query = "INSERT INTO " + repo + insertMapper(t);
                s.execute(query);

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
    }

    public String insertMapper(T t){
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for(Map.Entry<String, Object> entry : modelValues(t).entrySet()){
            columns.append(entry.getKey()).append(", ");

            if(entry.getValue() instanceof String)
                values.append("'").append(entry.getValue()).append("', ");
            else
                values.append(entry.getValue()).append(", ");
        }

        return "(" + columns.substring(0, columns.length() - 2) + ") VALUES (" + values.substring(0, values.length() - 2) + ")";
    }

    public  String updateMapper(T t){
        StringBuilder stringBuilder = new StringBuilder();

        for(Map.Entry<String, Object> entry : modelValues(t).entrySet()){
            stringBuilder.append(entry.getKey()).append(" = ?, ");
        }

        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }

    public abstract T resultMapper(ResultSet resultSet) throws SQLException;

    public abstract HashMap<String, Object> modelValues(T t);

    public abstract int getId(T t);

}
