package artgraine.model;


import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Exhibition {

    private Long id;

    private String title;

    private Date startDate;

    private Date endDate;

    private Date departureDate;

    private Date returnDate;

    public Exhibition() {
    }

    public Exhibition(String title, Date startDate, Date endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Exhibition(String title, Date startDate, Date endDate, Date departureDate, Date returnDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
    }

    public Exhibition(Long id, String title, Date startDate, Date endDate, Date departureDate, Date returnDate) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }

    public Exhibition setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Exhibition setTitle(String title) {
        this.title = title;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Exhibition setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Exhibition setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Exhibition setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public Exhibition setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    @Override
    public String toString() {
        DateFormat dtf = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("FR", "fr"));
        return (title + " du " + dtf.format(departureDate) + " au " + dtf.format(returnDate));
    }
}
