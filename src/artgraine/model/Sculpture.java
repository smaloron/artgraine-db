package artgraine.model;

public class Sculpture {

    private Long id;

    private String title;

    private String description;

    private String category;

    private Integer sizeInCm;

    private Integer insurancePrice;

    public Sculpture() {
    }

    public Sculpture(String title, String description, String category, Integer size, Integer insurancePrice) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.sizeInCm = size;
        this.insurancePrice = insurancePrice;
    }

    public Sculpture(Long id, String title, String description, String category, Integer size, Integer insurancePrice) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.sizeInCm = size;
        this.insurancePrice = insurancePrice;
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
}
