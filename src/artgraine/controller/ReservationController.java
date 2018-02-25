package artgraine.controller;

import artgraine.database.DatabaseConnection;
import artgraine.model.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ReservationController extends AbstractController implements Initializable {
    @FXML
    public TableView<Reservation> sculptureTableView;
    @FXML
    public TableColumn<Reservation, String> sculptureColumn;
    @FXML
    public TableColumn<Reservation, Boolean> selectedColumn;
    @FXML
    public ListView<Exhibition> exhibitionListView;

    private ExhibitionDAO exhibitionDAO;
    private SculptureDAO sculptureDAO;

    private ObservableList<Reservation> sculptureList;
    private ObservableList<Exhibition> exhibitionList;

    private Exhibition selectedExhibition;

    public ReservationController() {
        try {
            Connection cn = DatabaseConnection.getInstance();
            exhibitionDAO = new ExhibitionDAO(cn);
            sculptureDAO = new SculptureDAO(cn);
        } catch (SQLException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setExhibitionListData();
    }

    private void setExhibitionListData() {
        try {
            exhibitionList = FXCollections.observableArrayList(exhibitionDAO.findAll().getResults());
            exhibitionListView.setItems(exhibitionList);

            // Cell factory gère l'affichage des éléments de la liste des expos
            exhibitionListView.setCellFactory(new Callback<ListView<Exhibition>, ListCell<Exhibition>>() {
                @Override
                public ListCell<Exhibition> call(ListView<Exhibition> p) {
                    return new ListCell<Exhibition>() {
                        @Override
                        protected void updateItem(Exhibition t, boolean bln) {
                            super.updateItem(t, bln);
                            if (t != null) {
                                DateFormat dtf = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("FR", "fr"));
                                setText(t.getTitle() + " du " + dtf.format(t.getDepartureDate()) + " au " + dtf.format(t.getReturnDate()));
                            }
                        }

                    };
                }
            });

            //Evénement de sélection d'un élément de la liste des expos
            exhibitionListView.getSelectionModel().selectedItemProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        this.selectedExhibition = observable.getValue();
                        setSculptureData();
                    });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setSculptureData(){
        if(this.selectedExhibition == null){
            return;
        }

        sculptureTableView.setEditable(true);

        sculptureList = FXCollections.observableArrayList(sculptureDAO.findAvailableSculptures(selectedExhibition));

        sculptureColumn.setCellValueFactory(new PropertyValueFactory<>("title"));


        selectedColumn.setCellValueFactory(item -> {
                    if(item != null && item.getValue().selectedProperty() != null){
                        return item.getValue().selectedProperty();
                    } else {
                        return new SimpleBooleanProperty(false);
                    }
        });

        //selectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectedColumn));


        selectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            //System.out.println("Cours "+sculptureList.get(param).getTitle()+" changed value to " +sculptureList.get(param).isSelected());
            return new SimpleBooleanProperty(sculptureList.get(param).isSelected());
        }));


        //selectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectedColumn));



        sculptureTableView.setOnMouseClicked(event -> System.out.println(sculptureTableView.getSelectionModel().getSelectedItem()));

        selectedColumn.setEditable(true);

        sculptureTableView.setItems(sculptureList);



    }


    public void onValidate(ActionEvent actionEvent) {
        /*
        sculptureList.forEach((item)->{
            System.out.println(item.getTitle() + " " + item.getSelected());
        });
           */

        sculptureList.filtered(Reservation::isSelected).forEach(
                (item)->{
                    System.out.println(item.getTitle() + " " + item.isSelected());
                }
        );

        /*
        sculptureTableView.getItems().forEach((item)->{
            System.out.println(item.getTitle() + " " + item.isSelected());
        });*/

        /*
        for (int row = 0; row < sculptureTableView.getItems().size(); row++) {
            System.out.println(selectedColumn.getCellData(row));
        }
        */
    }
}
