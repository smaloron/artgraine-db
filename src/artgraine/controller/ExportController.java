package artgraine.controller;

import artgraine.database.DatabaseConnection;
import artgraine.model.Exhibition;
import artgraine.model.ExhibitionDAO;
import artgraine.model.Sculpture;
import artgraine.model.SculptureDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ExportController extends AbstractController implements Initializable{

    @FXML public ComboBox<Exhibition> exhibitionCombo;
    private ExhibitionDAO exhibitionDAO;
    private SculptureDAO sculptureDAO;
    private Exhibition selectedExhibition;

    private ObservableList<Exhibition> exhibitionList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Connection cn = DatabaseConnection.getInstance();
            exhibitionDAO = new ExhibitionDAO(cn);
            sculptureDAO = new SculptureDAO(cn);
            setExhibitionComboData();
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void setExhibitionComboData() throws SQLException {
        exhibitionList = FXCollections.observableArrayList(exhibitionDAO.findAll().getResults());
        exhibitionCombo.setItems(exhibitionList);



        exhibitionCombo.valueProperty().addListener(new ChangeListener<Exhibition>() {
            @Override
            public void changed(ObservableValue<? extends Exhibition> observable, Exhibition oldValue, Exhibition newValue) {
                selectedExhibition = newValue;
            }
        });
    }

    public void exportExhibition(){
        try {
            if(selectedExhibition != null){
                List<Sculpture> sculptureList = sculptureDAO.findSculptureByExhibition(selectedExhibition).getResults();
                InputStream is = new FileInputStream("templates/exhibition-sculpture-list.xls");
                OutputStream os = new FileOutputStream("expo.xls");

                Context context = new Context();
                context.putVar("exportTitle", selectedExhibition);
                context.putVar("sculptureList", sculptureList);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
