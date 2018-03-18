import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.json.simple.JSONArray;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WebToBD {
    private HikariDataSource dataSource;

    HikariConfig hikariConfig;

    public WebToBD() {
        hikariConfig = new HikariConfig("/hikary.properties");
        dataSource = new HikariDataSource(hikariConfig);
      
    }

    public void renameTable(int id, String name) {
        try(PreparedStatement st = dataSource.getConnection().
                prepareStatement("UPDATE test_jee SET name = ? WHERE id = ?")) {
            st.setString(1, name);
            st.setInt(2, id+1);
            st.execute();
            System.out.println("renamed table");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String createTable(){
        JSONArray defaultBody = new JSONArray();
        JSONArray bufArray = new JSONArray();
        bufArray.add(" ");
        bufArray.add(" ");
        defaultBody.add(bufArray);
        defaultBody.add(bufArray);
        System.out.println("Created new table: "+defaultBody.toJSONString());
        try(PreparedStatement st = dataSource.getConnection().
                prepareStatement("INSERT INTO test_jee (name, body) VALUES ('NewTable', ?)")) {

            st.setString(1, defaultBody.toJSONString());
            st.execute();
            return defaultBody.toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveTable(int id, String data) {
        try(PreparedStatement st = dataSource.getConnection().
                prepareStatement("UPDATE test_jee SET body = ? WHERE id = ?")) {
            st.setString(1, data);
            st.setInt(2, id+1);
            st.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> loadTables() {
        try(PreparedStatement st = dataSource.getConnection().
                prepareStatement("SELECT * FROM test_jee")) {

            ResultSet rs = st.executeQuery();
            List<String> tables = new ArrayList<>();
            while (rs.next()){
                tables.add(rs.getString("name"));
                System.out.println(rs.getString("name"));
            }
            return tables;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getTableBody(int id) {
        try(PreparedStatement st = dataSource.getConnection().
                prepareStatement("SELECT * FROM test_jee where id = ?")) {
            st.setInt(1, id+1);
            ResultSet rs = st.executeQuery();


            if (rs.next()){
                return rs.getString("body");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }
}
