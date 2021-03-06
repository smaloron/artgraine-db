package artgraine.model;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SculptureDAO implements FindableInterface<Sculpture, SculptureDAO>, PersistableInterface<Sculpture> {
    private Connection dbConnection;

    private ResultSet rs;

    private PreparedStatement pStatement;

    public SculptureDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public SculptureDAO findOneById(Long id) throws SQLException {
        String sql = "SELECT * FROM SCULPTURES WHERE ID = ?";
        this.pStatement = this.dbConnection.prepareStatement(sql);

        this.pStatement.setLong(1, id);

        this.rs = this.pStatement.executeQuery();

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
    public List<Map<String, String>> getResultsAsArray() throws SQLException {
        List<Map<String, String>> SculptureList = new ArrayList<>();
        Map<String, String> Sculpture;

        while (this.rs.next()) {
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
        if (id != null) {
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

    public List<Reservation> findAvailableSculptures(Exhibition exhibition) {
        List<Reservation> SculptureList = new ArrayList<>();
        Reservation Sculpture;

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT DISTINCT S.id, S.title, S.description, 0 as selected\n");
        sb.append("FROM APP.SCULPTURES AS S\n");
        sb.append("  LEFT JOIN APP.SCULPTURES_RESERVATIONS AS R\n");
        sb.append("      ON S.ID=R.SCULPTURE_ID\n");
        sb.append("  LEFT JOIN APP.EXHIBITIONS AS E\n");
        sb.append("      ON E.ID=R.EXHIBITION_ID AND\n");
        sb.append("        (E.DEPARTURE_DATE BETWEEN ? AND ?\n");
        sb.append("         OR E.RETURN_DATE BETWEEN ? AND ?\n");
        sb.append("         OR E.RETURN_DATE < ? AND E.DEPARTURE_DATE > ?)\n");
        sb.append("WHERE E.ID IS NULL\n");
        sb.append("AND S.id NOT IN(SELECT SCULPTURE_ID FROM APP.SCULPTURES_RESERVATIONS WHERE EXHIBITION_ID = ?)\n");
        sb.append("UNION\n");

        sb.append("SELECT DISTINCT S.id, S.title, S.description, 1 as selected\n");
        sb.append("FROM APP.SCULPTURES AS S\n");
        sb.append("WHERE S.ID IN (SELECT SCULPTURE_ID FROM APP.SCULPTURES_RESERVATIONS WHERE EXHIBITION_ID = ?)\n");


        String sql = sb.toString();


        try {
            this.pStatement = this.dbConnection.prepareStatement(sql);
            this.pStatement.setDate(1, Date.valueOf(exhibition.getDepartureDate().toString()));
            this.pStatement.setDate(2, Date.valueOf(exhibition.getReturnDate().toString()));
            this.pStatement.setDate(3, Date.valueOf(exhibition.getDepartureDate().toString()));
            this.pStatement.setDate(4, Date.valueOf(exhibition.getReturnDate().toString()));
            this.pStatement.setDate(5, Date.valueOf(exhibition.getDepartureDate().toString()));
            this.pStatement.setDate(6, Date.valueOf(exhibition.getReturnDate().toString()));
            this.pStatement.setLong(7, exhibition.getId());
            this.pStatement.setLong(8, exhibition.getId());

            this.rs = this.pStatement.executeQuery();

            while (this.rs.next()) {
                Sculpture = getOneResevationAsArray();
                SculptureList.add(Sculpture);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return SculptureList;
    }


    public SculptureDAO findSculptureByExhibition(Exhibition exhibition) throws SQLException {
        String sql = "SELECT S.* FROM SCULPTURES S INNER JOIN SCULPTURES_RESERVATIONS R ON S.ID = R.SCULPTURE_ID AND R.EXHIBITION_ID= ?";
        PreparedStatement stm = this.dbConnection.prepareStatement(sql);
        stm.setLong(1, exhibition.getId());
        this.rs = stm.executeQuery();
        return this;

    }

    private Reservation getOneResevationAsArray() throws SQLException {
        Reservation result = new Reservation();

        //if (this.rs.next()) {
        result.setSculptureId(this.rs.getLong("id"));
        result.setTitle(this.rs.getString("title"));
        result.setDescription(this.rs.getString("description"));
        result.setSelected(this.rs.getBoolean("selected"));
        //}

        return result;
    }

    public void clearReservations(Long exhibitionId) {
        String sql = "DELETE FROM SCULPTURES_RESERVATIONS WHERE EXHIBITION_ID = ?";
        try {
            PreparedStatement stm = this.dbConnection.prepareStatement(sql);
            stm.setLong(1, exhibitionId);
            stm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAllReservations(Long exhibitionId, ArrayList<Long> sculptureIds) {
        String sql = "INSERT INTO SCULPTURES_RESERVATIONS  (EXHIBITION_ID, SCULPTURE_ID) VALUES (?,?)";
        try {
            this.dbConnection.setAutoCommit(false);

            this.clearReservations(exhibitionId);

            PreparedStatement stm = this.dbConnection.prepareStatement(sql);
            stm.setLong(1, exhibitionId);

            for (Long id : sculptureIds) {
                stm.setLong(2, id);
                stm.executeUpdate();
            }
            this.dbConnection.commit();
            this.dbConnection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exhibitionHasSculptures(Long exhibitionId){
        String sql = "SELECT COUNT(*) FROM SCULPTURES_RESERVATIONS  WHERE EXHIBITION_ID=?";
        return checkIfExists(sql, exhibitionId);
    }

    public boolean sculptureIsExhibited(Long sculptureId){
        String sql = "SELECT COUNT(*) FROM SCULPTURES_RESERVATIONS  WHERE SCULPTURE_ID=?";
        return checkIfExists(sql, sculptureId);
    }

    private boolean checkIfExists(String sql, Long id){
        boolean selected = false;
        try {
            PreparedStatement stm = this.dbConnection.prepareStatement(sql);
            stm.setLong(1, id);

            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                selected = rs.getInt(1)> 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return selected;
    }

    public boolean sculptureIsSelected(Long exhibitionId, Reservation item) {
        boolean selected = false;
        String sql = "SELECT COUNT(*) FROM SCULPTURES_RESERVATIONS  WHERE EXHIBITION_ID=? AND SCULPTURE_ID=?";
        try {
            PreparedStatement stm = this.dbConnection.prepareStatement(sql);
            stm.setLong(1, exhibitionId);
            stm.setLong(2, item.getSculptureId());

            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                selected = rs.getInt(1)> 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return selected;

    }

    public void saveOneReservation(Long exhibitionId, Reservation item) {
        String sql = "";
        boolean alreadySelected = sculptureIsSelected(exhibitionId, item);

        if (item.isSelected() && ! alreadySelected) {
            sql = "INSERT INTO SCULPTURES_RESERVATIONS  (EXHIBITION_ID, SCULPTURE_ID) VALUES (?,?)";
        } else if(alreadySelected){
            sql = "DELETE FROM SCULPTURES_RESERVATIONS  WHERE EXHIBITION_ID=? AND SCULPTURE_ID=?";
        } else {
            return;
        }
        try {
            PreparedStatement stm = this.dbConnection.prepareStatement(sql);
            stm.setLong(1, exhibitionId);
            stm.setLong(2, item.getSculptureId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
