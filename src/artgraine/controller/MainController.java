package artgraine.controller;

import artgraine.Main;
import artgraine.database.DatabaseConnection;
import artgraine.model.Sculpture;
import artgraine.model.SculptureDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableFilter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController extends AbstractController implements Initializable{
    @FXML public TableView<Sculpture> sculptureTableView;
    @FXML public TableColumn<Sculpture, String> titleColumn;
    @FXML public TableColumn<Sculpture, String> categoryColumn;
    @FXML public TableColumn<Sculpture, Integer> sizeColumn;
    @FXML public TableColumn<Sculpture, Integer> insuranceColumn;
    @FXML public TableColumn<Sculpture, String> descriptionColumn;
    @FXML public TableColumn<Sculpture, Long> idColumn;
    public TextField filterTextField;

    private ObservableList<Sculpture> sculptureList;

    private SculptureDAO dao;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("sizeInCm"));
        insuranceColumn.setCellValueFactory(new PropertyValueFactory<>("insurancePrice"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        try {
        Connection cn = DatabaseConnection.getInstance();
        dao = new SculptureDAO(cn);
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
        setFilterEvent();

    }

    private void setFilterEvent(){
        setTableData();

        FilteredList<Sculpture> filteredList = new FilteredList<>(sculptureList, sculpture -> true);

        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(sculpture -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String filterValue = newValue.toLowerCase();

                return sculpture.getTitle().toLowerCase().contains(filterValue)
                        || sculpture.getCategory().toLowerCase().contains(filterValue)
                        || sculpture.getDescription().toLowerCase().contains(filterValue);

            });
        });

        SortedList<Sculpture> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(sculptureTableView.comparatorProperty());
        sculptureTableView.setItems(sortedList);

    }

    private void setTableData(){
        try {
            sculptureList = FXCollections.observableArrayList(dao.findAll().getResults());
            sculptureTableView.setItems(sculptureList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showSculptureWindow(Sculpture sculpture) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/SculptureForm.fxml"));
            Pane pane = loader.load();

            Stage sculptureStage = new Stage();
            sculptureStage.initModality(Modality.APPLICATION_MODAL);

            sculptureStage.setTitle("Artgraine");
            sculptureStage.setScene(new Scene(pane, 500, 300));
            //sculptureStage.setAlwaysOnTop(true);

            SculptureFormController controller = loader.getController();
            controller.setMain(this.main, sculptureStage);

            if(sculpture != null){
                controller.setFormData(sculpture);
            }

            sculptureStage.showAndWait();

            filterTextField.setText("");
            setFilterEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSculptureForm(){
        this.showSculptureWindow(null);
    }

    public void onEdit(ActionEvent actionEvent) {
        Sculpture sculpture = sculptureTableView.getSelectionModel().getSelectedItem();
        showSculptureWindow(sculpture);
    }

    private void deleteSculpture(){
        Sculpture sculpture = sculptureTableView.getSelectionModel().getSelectedItem();
        try {
            dao.deleteOneById(sculpture.getId());
            setTableData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer cette sculpture");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            deleteSculpture();
        }
    }

    public void openExhibitionWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Exhibitions.fxml"));
            Pane pane = loader.load();

            Stage exhibitionStage = new Stage();
            exhibitionStage.initModality(Modality.APPLICATION_MODAL);

            exhibitionStage.setTitle("Artgraine");
            exhibitionStage.setScene(new Scene(pane));
            exhibitionStage.setAlwaysOnTop(true);

            ExhibitionController controller = loader.getController();
            controller.setMain(this.main, exhibitionStage);

            exhibitionStage.setOnCloseRequest(event -> controller.closeWindow());

            exhibitionStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openReservationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/ReservationForm.fxml"));
            Pane pane = loader.load();

            Stage reservationStage = new Stage();
            reservationStage.initModality(Modality.APPLICATION_MODAL);

            reservationStage.setTitle("Artgraine");
            reservationStage.setScene(new Scene(pane));
            reservationStage.setAlwaysOnTop(true);

            ReservationController controller = loader.getController();
            controller.setMain(this.main, reservationStage);

            reservationStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
