package artgraine.controller;

import artgraine.database.DatabaseConnection;
import artgraine.model.Exhibition;
import artgraine.model.ExhibitionDAO;
import artgraine.model.Reservation;
import artgraine.model.SculptureDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReservationController extends AbstractController implements Initializable {
    @FXML public TableView<Reservation> sculptureTableView;
    @FXML public TableColumn<Reservation, String> sculptureColumn;
    @FXML public TableColumn<Reservation, Boolean> selectedColumn;
    @FXML public TableColumn<Reservation,String> descriptionColumn;
    @FXML public ListView<Exhibition> exhibitionListView;
    @FXML public Label selectedSculptureCountLabel;


    private int selectedSculptureNumber= 0;
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

        //Largeur des colonnes de la table des sculptures
        sculptureColumn.prefWidthProperty().bind(sculptureTableView.widthProperty().multiply(.4));
        descriptionColumn.prefWidthProperty().bind(sculptureTableView.widthProperty().multiply(.55));
        selectedColumn.prefWidthProperty().bind(sculptureTableView.widthProperty().multiply(.05));

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

                        updateReservations();

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

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        selectedColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));


        //Sélection d'une sculpture en cochant sa case
        selectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            //
            sculptureList.get(param).selectedProperty().addListener((observable, oldValue, newValue) -> {
                updateSculptureCount();
                updateSculptureSelection(sculptureList.get(param));
            });

            sculptureTableView.getSelectionModel().select(param);
            return sculptureList.get(param).selectedProperty();
        }));

        selectedColumn.setEditable(true);

        //Sélection d'une sculpture lors de la sélection d'une ligne
        /*
        sculptureTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    observable.getValue().setSelected(! observable.getValue().isSelected());
                    updateSculptureCount();
                    updateSculptureSelection(observable.getValue());

                }
        );*/

        sculptureTableView.setItems(sculptureList);

        /*
        sculptureList.addListener(new ListChangeListener<Reservation>() {
            @Override
            public void onChanged(Change<? extends Reservation> c) {
                updateSculptureCount();
            }
        });*/

        updateSculptureCount();

    }

    private void updateSculptureSelection(Reservation item){
        if(selectedExhibition != null){
            sculptureDAO.saveOneReservation(selectedExhibition.getId(), item);
        }
    }

    /**
     * Compte du nombre de sculptures sélectionnées
     */
    private void updateSculptureCount(){
        selectedSculptureNumber = sculptureList.filtered(Reservation::isSelected).size();
        showSculptureCount();
    }

    private void showSculptureCount(){
        String plural = selectedSculptureNumber>1?"s":"";
        selectedSculptureCountLabel.setText(selectedSculptureNumber + " sculpture"+plural+" sélectionnée"+plural);
    }



    /**
     * Mise à jour des sculptures sélectionnées dans la base de données
     */
    private void updateReservations(){
        if(selectedExhibition != null){
            ArrayList<Long> sculptureIdList = new ArrayList<>();
            sculptureList.filtered(Reservation::isSelected).forEach(
                    (item)->{
                        System.out.println(item.getTitle() + " " + item.isSelected());
                        sculptureIdList.add(item.getSculptureId());
                    }
            );

            sculptureDAO.saveAllReservations(selectedExhibition.getId(), sculptureIdList);
        }
    }


    public void onValidate(ActionEvent actionEvent) {
        updateReservations();
    }

    public void closeWindow(){
        updateReservations();
        stage.close();
    }
}
