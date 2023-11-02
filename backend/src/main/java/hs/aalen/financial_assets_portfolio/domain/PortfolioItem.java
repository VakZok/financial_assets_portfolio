package hs.aalen.financial_assets_portfolio.domain;

import hs.aalen.financial_assets_portfolio.data.PItemDTO;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class PortfolioItem {
    /* PROPERTIES OF ENTITY */
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate purchaseDate;
    private double purchasePrice;
    private int quantity;
    private String wkn;
    private String name;
    private String category;
    private String description;

    /* Constructors */
    public PortfolioItem(
            String wkn, String name,
            String category, String description,
            LocalDate purchaseDate, double purchasePrice,
            int quantity) {

        this.wkn = wkn;
        this.name = name;
        this.category = category;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;

    }

    public PortfolioItem(PItemDTO pItemDTO) {
        this.wkn = pItemDTO.getWkn();
        this.name = pItemDTO.getName();
        this.category = pItemDTO.getCategory();
        this.description = pItemDTO.getDescription();
        this.purchaseDate = pItemDTO.getPurchaseDate();
        this.purchasePrice = pItemDTO.getPurchasePrice();
        this.quantity = pItemDTO.getQuantity();

    }

    public PortfolioItem() {
    }

    /* Getters and Setters */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getWkn() {
        return wkn;
    }

    public void setWkn(String wkn) {
        this.wkn = wkn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

