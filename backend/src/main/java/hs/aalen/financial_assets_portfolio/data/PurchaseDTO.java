package hs.aalen.financial_assets_portfolio.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import hs.aalen.financial_assets_portfolio.domain.Purchase;
import hs.aalen.financial_assets_portfolio.domain.Share;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFilter("purchaseFilter")
public class PurchaseDTO {
    /* Class to create Data Transfer Objects for portfolio items used for communication with the frontend
     * and not exposing too much information.
     */

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    private double purchasePrice;
    private double totalPrice;
    private int quantity;
    private ShareDTO shareDTO;

    /* Constructors inclusive copy constructors */
    public PurchaseDTO(
            Long id, LocalDate purchaseDate,
            double purchasePrice, int quantity
    ) {
        this.id = id;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.totalPrice = purchasePrice * quantity;
        this.quantity = quantity;
    }

    public PurchaseDTO(Purchase purchase) {
        this.id = purchase.getId();
        this.purchaseDate = purchase.getPurchaseDate();
        this.purchasePrice = purchase.getPurchasePrice();
        this.totalPrice = purchase.getPurchasePrice() * purchase.getQuantity();
        this.quantity = purchase.getQuantity();
        this.shareDTO = new ShareDTO(purchase.getShare());
    }

    public PurchaseDTO(){}

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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ShareDTO getShareDTO() {
        return shareDTO;
    }

    public void setShareDTO(ShareDTO shareDTO) {
        this.shareDTO = shareDTO;
    }
}

