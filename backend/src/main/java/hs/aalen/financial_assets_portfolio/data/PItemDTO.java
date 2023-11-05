package hs.aalen.financial_assets_portfolio.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFilter("pItemFilter")
public class PItemDTO {
    /* Class to create Data Transfer Objects for portfolio items used for communication with the frontend
     * and not exposing too much information.
     */

    private Long id;
    private ShareDTO shareDTO;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    private double purchasePrice;
    private double totalPrice;
    private int quantity;

    /* Constructors inclusive copy constructors */
    public PItemDTO(
            Long id, LocalDate purchaseDate,
            double purchasePrice, int quantity, ShareDTO shareDTO
    ) {

        this.id = id;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.totalPrice = purchasePrice * quantity;
        this.quantity = quantity;
        this.shareDTO = shareDTO;

    }

    public PItemDTO(PortfolioItem portfolioItem) {
        this.id = portfolioItem.getId();
        this.purchaseDate = portfolioItem.getPurchaseDate();
        this.purchasePrice = portfolioItem.getPurchasePrice();
        this.totalPrice = portfolioItem.getPurchasePrice()* portfolioItem.getQuantity();
        this.quantity = portfolioItem.getQuantity();
        this.shareDTO = new ShareDTO(portfolioItem.getShare());
    }

    public PItemDTO(){}

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

