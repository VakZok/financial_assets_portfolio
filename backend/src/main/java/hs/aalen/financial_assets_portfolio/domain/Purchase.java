package hs.aalen.financial_assets_portfolio.domain;

import hs.aalen.financial_assets_portfolio.data.PurchaseDTO;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class Purchase {
    /* PROPERTIES OF ENTITY */
    @Id
    @Column(unique = true)
    @GeneratedValue(generator = "increment")
    private Long id;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate purchaseDate;
    private double purchasePrice;
    private int quantity;

    /* MAPPED RELATIONSHIPS */
    @ManyToOne
    @JoinColumn(name = "wkn")
    private Share share;

    /* Constructors */
    public Purchase(LocalDate purchaseDate, double purchasePrice, int quantity, Share share) {
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        this.share = share;

    }

    public Purchase(PurchaseDTO purchaseDTO) {
        this.purchaseDate = purchaseDTO.getPurchaseDate();
        this.purchasePrice = purchaseDTO.getPurchasePrice();
        this.quantity = purchaseDTO.getQuantity();
        this.share = new Share(purchaseDTO.getShareDTO());

    }

    public Purchase() {
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

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

}

