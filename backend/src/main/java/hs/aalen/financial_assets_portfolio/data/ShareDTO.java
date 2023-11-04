package hs.aalen.financial_assets_portfolio.data;

import hs.aalen.financial_assets_portfolio.domain.Share;

public class ShareDTO {
    /* Class to create Data Transfer Objects for shares used for communication with the frontend
     * and not exposing too much information.
     */

    private String wkn;
    private String name;
    private String category;
    private String description;

    /* Constructors */
    public ShareDTO(Share share) {
        this.wkn = share.getWkn();
        this.name = share.getName();
        this.category = share.getCategory();
        this.description = share.getDescription();
    }

    public ShareDTO(){}

    /* Getters and Setters */
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
