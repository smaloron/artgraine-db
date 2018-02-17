package artgraine.controller;

import artgraine.Main;
import artgraine.database.DatabaseConnection;
import artgraine.model.Exhibition;
import artgraine.model.ExhibitionDAO;
import artgraine.model.Sculpture;
import artgraine.model.SculptureDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ExhibitionController extends AbstractController implements Initializable{
    @FXML public TableView<Exhibition> exhibitionTableView;
    public TableColumn<Exhibition, String> titleColumn;
    public TableColumn<Exhibition, Date> startDateColumn;
    public TableColumn<Exhibition, Date> endDateColumn;
    public TableColumn<Exhibition, Date> departureDateColumn;
    public TableColumn<Exhibition, Date> returnDateColumn;
    public TableColumn<Exhibition, Long> idColumn;

    private ObservableList<Exhibition> exhibitionList;

    private ExhibitionDAO exhibitionDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        try {
            Connection cn = DatabaseConnection.getInstance();
            exhibitionDAO = new ExhibitionDAO(cn);
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }

        setTableData();
    }

    private void setTableData(){
        try {
            exhibitionList = FXCollections.observableArrayList(exhibitionDAO.findAll().getResults());
            exhibitionTableView.setItems(exhibitionList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showExhibitionWindow(Exhibition exhibition) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/ExhibitionForm.fxml"));
            Pane pane = loader.load();

            Stage sculptureStage = new Stage();
            sculptureStage.initModality(Modality.APPLICATION_MODAL);

            sculptureStage.setTitle("Artgraine");
            sculptureStage.setScene(new Scene(pane));
            sculptureStage.setAlwaysOnTop(true);

            ExhibitionFormController controller = loader.getController();
            controller.setMain(this.main, sculptureStage);

            if(exhibition != null){
                controller.setFormData(exhibition);
            }

            sculptureStage.showAndWait();

            setTableData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddExhibition(ActionEvent actionEvent) {
        showExhibitionWindow(null);
    }
}
