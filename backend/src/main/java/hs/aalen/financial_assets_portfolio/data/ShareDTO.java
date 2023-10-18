package hs.aalen.financial_assets_portfolio.data;

import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import hs.aalen.financial_assets_portfolio.domain.Share;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

public class ShareDTO {

    private String wkn;
    private String name;
    private String category;
    private String description;


    public ShareDTO(Share share) {
        this.wkn = share.getWkn();
        this.name = share.getName();
        this.category = share.getCategory();
        this.description = share.getDescription();
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
