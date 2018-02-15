package artgraine.model;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SculptureDAO  implements FindableInterface<Sculpture, SculptureDAO>, PersistableInterface<Sculpture> {
    private Connection dbConnection;

    private ResultSet rs;

    private PreparedStatement pStatement;

    public SculptureDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public SculptureDAO findOneById(int id) throws SQLException {
        String sql = "SELECT * FROM SCULPTURES WHERE ID = ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setInt(1, id);

        this.pStatement.executeUpdate();

        return this;
    }

    @Override
    public SculptureDAO findAll() throws SQLException {
        String sql = "SELECT * FROM SCULPTURES";
        Statement stm = this.dbConnection.createStatement();
        this.rs = stm.executeQuery(sql);
        return this;
    }

    @Override
    public Sculpture getOneResult() throws SQLException {
        Sculpture Sculpture = new Sculpture();

        if (this.rs.next()) {
            Sculpture = this.getOneSculpture();
        }

        return Sculpture;
    }

    @Override
    public Map getOneResultAsArray() throws SQLException {
        Map<String, String> result = new HashMap<>();

        if (this.rs.next()) {
            result.put("id", this.rs.getString("id"));
            result.put("title", this.rs.getString("title"));
            result.put("category", this.rs.getString("category"));
            result.put("description", this.rs.getString("description"));
            result.put("sizeInCm", this.rs.getString("sizeInCm"));
            result.put("insurancePrice", this.rs.getString("insurancePrice"));
        }

        return result;
    }

    private Sculpture getOneSculpture() throws SQLException {
        Sculpture Sculpture = new Sculpture();
        Sculpture.setId(this.rs.getLong("ID"));
        Sculpture.setTitle(this.rs.getString("TITLE"));
        Sculpture.setCategory(this.rs.getString("CATEGORY"));
        Sculpture.setDescription(this.rs.getString("DESCRIPTION"));
        Sculpture.setSizeInCm(this.rs.getInt("SIZE_IN_CM"));
        Sculpture.setInsurancePrice(this.rs.getInt("INSURANCE_PRICE"));

        return Sculpture;
    }

    @Override
    public List<Sculpture> getResults() throws SQLException {
        List<Sculpture> SculptureList = new ArrayList<>();

        while (this.rs.next()) {
            SculptureList.add(this.getOneSculpture());
        }

        return SculptureList;
    }

    @Override
    public List<Map<String,String>> getResultsAsArray() throws SQLException{
        List<Map<String, String>> SculptureList = new ArrayList<>();
        Map<String,String> Sculpture;

        while(this.rs.next()){
            Sculpture = getOneResultAsArray();

            SculptureList.add(Sculpture);

        }

        return SculptureList;
    }

    @Override
    public int deleteOneById(Long id) throws SQLException {
        String sql = "DELETE FROM SCULPTURES WHERE ID = ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setLong(1, id);

        return this.pStatement.executeUpdate();
    }

    public int save(Sculpture sculpture) throws SQLException {
        int affectedRows;
        Long id = sculpture.getId();
        if (id!=null) {
            affectedRows = this.update(sculpture);
        } else {
            affectedRows = this.insert(sculpture);
        }
        return affectedRows;
    }

    private int insert(Sculpture Sculpture) throws SQLException {
        String sql = "INSERT INTO SCULPTURES (TITLE, DESCRIPTION, CATEGORY, SIZE_IN_CM, INSURANCE_PRICE) VALUES (?,?,?,?,?)";
        this.pStatement = this.dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        this.pStatement.setString(1, Sculpture.getTitle());
        this.pStatement.setString(2, Sculpture.getDescription());
        this.pStatement.setString(3, Sculpture.getCategory());
        this.pStatement.setInt(4, Sculpture.getSizeInCm());
        this.pStatement.setInt(5, Sculpture.getInsurancePrice());

        int result = this.pStatement.executeUpdate();

        ResultSet insertRs = this.pStatement.getGeneratedKeys();

        if (insertRs.next()) {
            Sculpture.setId(insertRs.getLong(1));
        }

        return result;

    }

    private int update(Sculpture Sculpture) throws SQLException {
        String sql = "UPDATE SCULPTURES SET TITLE=?, DESCRIPTION=?, CATEGORY=?, SIZE_IN_CM=?, INSURANCE_PRICE=? WHERE ID=?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setString(1, Sculpture.getTitle());
        this.pStatement.setString(2, Sculpture.getDescription());
        this.pStatement.setString(3, Sculpture.getCategory());
        this.pStatement.setInt(4, Sculpture.getSizeInCm());
        this.pStatement.setInt(5, Sculpture.getInsurancePrice());
        this.pStatement.setLong(6, Sculpture.getId());

        return this.pStatement.executeUpdate();
    }
}
