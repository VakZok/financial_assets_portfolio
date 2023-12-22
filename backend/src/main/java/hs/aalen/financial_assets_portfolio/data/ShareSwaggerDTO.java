package hs.aalen.financial_assets_portfolio.data;

import hs.aalen.financial_assets_portfolio.domain.Share;

public class ShareSwaggerDTO {
    private String isin;
    private String name;
    private String category;
    private String description;

    /* Constructors */
    public ShareSwaggerDTO(Share share) {
        //this.isin = share.getIsin();
        this.name = share.getName();
        this.category = share.getCategory();
        this.description = share.getDescription();
    }

    public ShareSwaggerDTO(){}

    /* Getters and Setters */
    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
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
