package artgraine.controller;

import artgraine.Main;
import artgraine.database.DatabaseConnection;
import artgraine.model.Sculpture;
import artgraine.model.SculptureDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController extends AbstractController implements Initializable{
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
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("sizeInCm"));
        insuranceColumn.setCellValueFactory(new PropertyValueFactory<>("insurancePrice"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        setTableData();
    }

    private void setTableData(){
        try {
            Connection cn = DatabaseConnection.getInstance();
            SculptureDAO dao = new SculptureDAO(cn);
            sculptureList = FXCollections.observableArrayList(dao.findAll().getResults());
            sculptureTableView.setItems(sculptureList);
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void showSculptureWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/SculptureForm.fxml"));
            Pane pane = loader.load();

            Stage sculptureStage = new Stage();
            sculptureStage.initModality(Modality.APPLICATION_MODAL);

            sculptureStage.setTitle("Artgraine");
            sculptureStage.setScene(new Scene(pane, 500, 300));
            sculptureStage.setAlwaysOnTop(true);

            SculptureFormController controller = loader.getController();
            controller.setMain(this.main, sculptureStage);

            sculptureStage.showAndWait();

            setTableData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSculptureForm(){
        this.showSculptureWindow();
    }


}
