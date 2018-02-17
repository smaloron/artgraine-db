package artgraine.model;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExhibitionDAO implements FindableInterface<Exhibition, ExhibitionDAO>, PersistableInterface<Exhibition> {
    private Connection dbConnection;

    private ResultSet rs;

    private PreparedStatement pStatement;

    public ExhibitionDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public ExhibitionDAO findOneById(Long id) throws SQLException {
        String sql = "SELECT * FROM EXHIBITIONS WHERE ID = ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setLong(1, id);

        this.pStatement.executeUpdate();

        return this;
    }

    @Override
    public ExhibitionDAO findAll() throws SQLException {
        String sql = "SELECT * FROM EXHIBITIONS";
        Statement stm = this.dbConnection.createStatement();
        this.rs = stm.executeQuery(sql);
        return this;
    }

    @Override
    public Exhibition getOneResult() throws SQLException {
        Exhibition exhibition = new Exhibition();

        if (this.rs.next()) {
            exhibition = this.getOneExhibition();
        }

        return exhibition;
    }

    @Override
    public Map<String, String> getOneResultAsArray() throws SQLException {
        Map<String, String> result = new HashMap<>();

        if (this.rs.next()) {
            result.put("id", this.rs.getString("id"));
            result.put("title", this.rs.getString("title"));
            result.put("startdate", this.rs.getString("start_date"));
            result.put("endDate", this.rs.getString("end_date"));
            result.put("departureDate", this.rs.getString("departure_date"));
            result.put("returnDate", this.rs.getString("return_date"));
        }

        return result;
    }

    private Exhibition getOneExhibition() throws SQLException {
        Exhibition exhibition = new Exhibition();
        exhibition.setId(this.rs.getLong("ID"));
        exhibition.setTitle(this.rs.getString("TITLE"));
        exhibition.setStartDate(this.rs.getDate("START_DATE"));
        exhibition.setEndDate(this.rs.getDate("END_DATE"));
        exhibition.setDepartureDate(this.rs.getDate("DEPARTURE_DATE"));
        exhibition.setReturnDate(this.rs.getDate("RETURN_DATE"));

        return exhibition;
    }

    @Override
    public List<Exhibition> getResults() throws SQLException {
        List<Exhibition> exhibitionList = new ArrayList<>();

        while (this.rs.next()) {
            exhibitionList.add(this.getOneExhibition());
        }

        return exhibitionList;
    }

    @Override
    public List<Map<String,String>> getResultsAsArray() throws SQLException{
        List<Map<String, String>> ExhibitionList = new ArrayList<>();
        Map<String,String> Exhibition;

        while(this.rs.next()){
            Exhibition = getOneResultAsArray();

            ExhibitionList.add(Exhibition);

        }

        return ExhibitionList;
    }

    @Override
    public int deleteOneById(Long id) throws SQLException {
        String sql = "DELETE FROM EXHIBITIONS WHERE ID = ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setLong(1, id);

        return this.pStatement.executeUpdate();
    }

    public int save(Exhibition Exhibition) throws SQLException {
        int affectedRows;
        Long id = Exhibition.getId();
        if (id != null) {
            affectedRows = this.update(Exhibition);
        } else {
            affectedRows = this.insert(Exhibition);
        }
        return affectedRows;
    }

    private int insert(Exhibition exhibition) throws SQLException {
        String sql = "INSERT INTO EXHIBITIONS (TITLE, START_DATE, END_DATE, DEPARTURE_DATE, RETURN_DATE) VALUES (?,?,?,?,?)";
        this.pStatement = this.dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        this.pStatement.setString(1, exhibition.getTitle());
        this.pStatement.setDate(2, (Date) exhibition.getStartDate());
        this.pStatement.setDate(3, (Date) exhibition.getEndDate());
        this.pStatement.setDate(4, (Date) exhibition.getDepartureDate());
        this.pStatement.setDate(5, (Date) exhibition.getReturnDate());

        int result = this.pStatement.executeUpdate();

        ResultSet insertRs = this.pStatement.getGeneratedKeys();

        if (insertRs.next()) {
            exhibition.setId(insertRs.getLong(1));
        }

        return result;

    }

    private int update(Exhibition exhibition) throws SQLException {
        String sql = "UPDATE EXHIBITIONS SET TITLE=?, START_DATE=?, END_DATE=?, DEPARTURE_DATE=?, RETURN_DATE=? WHERE ID=?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setString(1, exhibition.getTitle());
        this.pStatement.setDate(2, (Date) exhibition.getStartDate());
        this.pStatement.setDate(3, (Date) exhibition.getEndDate());
        this.pStatement.setDate(4, (Date) exhibition.getDepartureDate());
        this.pStatement.setDate(5, (Date) exhibition.getReturnDate());
        this.pStatement.setLong(6, exhibition.getId());

        return this.pStatement.executeUpdate();
    }
}
