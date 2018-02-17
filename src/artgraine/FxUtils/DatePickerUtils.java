package artgraine.FxUtils;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.LocalDate;

public class DatePickerUtils {

    public static Callback<DatePicker, DateCell> getDateCellFactory(LocalDate targetDate, boolean isAfter){
        final Callback<DatePicker, DateCell> cellCallback =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                boolean dateTest;
                                if(isAfter){
                                    dateTest = item.isAfter(targetDate);
                                } else {
                                    dateTest = item.isBefore(targetDate);
                                }

                                if (dateTest) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };

        return  cellCallback;
    }

    public static Callback<DatePicker, DateCell> getDateCellFactory(DatePicker targetDatePicker, boolean isAfter){
        final Callback<DatePicker, DateCell> cellCallback =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                boolean dateTest;
                                if(isAfter){
                                    dateTest = item.isAfter(targetDatePicker.getValue());
                                } else {
                                    dateTest = item.isBefore(targetDatePicker.getValue());
                                }

                                if (dateTest) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };

        return  cellCallback;
    }
}
