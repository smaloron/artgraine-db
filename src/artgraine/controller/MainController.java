package artgraine.controller;

import artgraine.database.DatabaseConnection;
import artgraine.model.Sculpture;
import artgraine.model.SculptureDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML public TableView<Sculpture> sculptureTableView;
    @FXML public TableColumn<Sculpture, String> titleColumn;
    @FXML public TableColumn<Sculpture, String> categoryColumn;
    @FXML public TableColumn<Sculpture, Integer> sizeColumn;
    @FXML public TableColumn<Sculpture, Integer> insuranceColumn;
    @FXML public TableColumn<Sculpture, String> descriptionColumn;
    @FXML public TableColumn<Sculpture, Long> idColumn;

    private ObservableList<Sculpture> sculptureList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableData();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("sizeInCm"));
        insuranceColumn.setCellValueFactory(new PropertyValueFactory<>("insurancePrice"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        sculptureTableView.setItems(sculptureList);
    }

    private void setTableData(){
        try {
            Connection cn = DatabaseConnection.getInstance();
            SculptureDAO dao = new SculptureDAO(cn);
            sculptureList = FXCollections.observableArrayList(dao.findAll().getResults());
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }


    }
}
