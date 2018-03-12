package artgraine.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sculpture {

    private Long id;

    private String title;

    private String description;

    private String category;

    private Integer sizeInCm;

    private Integer insurancePrice;

    private Integer unit;

    public Sculpture() {
    }

    public Sculpture(String title, String description, String category, Integer size, Integer insurancePrice) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.sizeInCm = size;
        this.insurancePrice = insurancePrice;
        setUnit();
    }

    public Sculpture(Long id, String title, String description, String category, Integer size, Integer insurancePrice) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.sizeInCm = size;
        this.insurancePrice = insurancePrice;
        setUnit();
    }

    private void setUnit(){
        int qt = 1;
        Pattern pattern = Pattern.compile("^.*[xX][ ]?([0-9]).*[ ]?$");
        Matcher matcher = pattern.matcher(this.title);
        boolean found = matcher.matches();
        if(found){
            qt = Integer.valueOf(matcher.group(1));
        }

        this.unit = qt;
    }

    public Long getId() {
        return id;
    }

    public Sculpture setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Sculpture setTitle(String title) {
        this.title = title;
        setUnit();
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Sculpture setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Sculpture setCategory(String category) {
        this.category = category;
        return this;
    }

    public Integer getSizeInCm() {
        return sizeInCm;
    }

    public Sculpture setSizeInCm(Integer sizeInCm) {
        this.sizeInCm = sizeInCm;
        return this;
    }

    public Integer getInsurancePrice() {
        return insurancePrice;
    }

    public Sculpture setInsurancePrice(Integer insurancePrice) {
        this.insurancePrice = insurancePrice;
        return this;
    }

    public Integer getTotalInsuranceValue(){
        return this.unit * this.insurancePrice;
    }
}
