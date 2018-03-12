package artgraine.controller;

import artgraine.database.DatabaseConnection;
import artgraine.model.Exhibition;
import artgraine.model.ExhibitionDAO;
import artgraine.model.Sculpture;
import artgraine.model.SculptureDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class SculptureFormController extends AbstractController implements Initializable{

    @FXML public TextField categoryTextField;
    @FXML public TextField titleTextField;
    @FXML public TextField descriptionTextField;
    @FXML public TextField sizeTextField;
    @FXML public TextField insuranceValueTextField;
    @FXML public Label formTitleLabel;
    @FXML public ListView<Exhibition> exhibitionListView;

    private Sculpture sculpture;
    private ExhibitionDAO exhibitionDAO;
    private SculptureDAO sculptureDAO;

    private ObservableList<Exhibition> exhibitionList;


    public SculptureFormController() {
        sculpture = new Sculpture();
    }

    public void validateForm(ActionEvent actionEvent) {
        sculpture.setTitle(titleTextField.getText())
                .setDescription(descriptionTextField.getText())
                .setCategory(categoryTextField.getText())
                .setSizeInCm(Integer.valueOf(sizeTextField.getText()))
                .setInsurancePrice(Integer.valueOf(insuranceValueTextField.getText()));

        try {

            sculptureDAO.save(sculpture);
            closeWindow();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setFormData(Sculpture sculpture){
        this.sculpture = sculpture;

        formTitleLabel.setText("Modification d'une sculpture");

        titleTextField.setText(sculpture.getTitle());
        descriptionTextField.setText(sculpture.getDescription());
        categoryTextField.setText(sculpture.getCategory());
        sizeTextField.setText(sculpture.getSizeInCm().toString());
        insuranceValueTextField.setText(sculpture.getInsurancePrice().toString());

        try {
            setExhibitiondata();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Connection cn = DatabaseConnection.getInstance();
            sculptureDAO = new SculptureDAO(cn);
            exhibitionDAO = new ExhibitionDAO(cn);
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }


        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> sizeTexFieldFormatter = new TextFormatter<>(integerFilter);
        TextFormatter<String> insuranceTextFieldFormatter = new TextFormatter<>(integerFilter);

        sizeTextField.setTextFormatter(sizeTexFieldFormatter);
        insuranceValueTextField.setTextFormatter(insuranceTextFieldFormatter);
    }

    private void setExhibitiondata() throws SQLException {
        exhibitionList = FXCollections.observableArrayList(exhibitionDAO.findAllBySculptureReservation(sculpture).getResults());
        exhibitionListView.setItems(exhibitionList);
    }
}
