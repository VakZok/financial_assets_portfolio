package hs.aalen.financial_assets_portfolio.domain;

import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Share {
    /* PROPERTIES OF ENTITY */
    @Id
    @Column(unique = true)
    private String wkn;
    private String name;
    private String category;
    private String description;

    /* MAPPED RELATIONSHIPS */
    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL)
    Set<PortfolioItem> portfolioItems;

    /* Constructors */
    public Share(String wkn, String name, String category, String description) {
        this.wkn = wkn;
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public Share(ShareDTO shareDTO){
        this.wkn = shareDTO.getWkn().toUpperCase();
        this.name = shareDTO.getName();
        this.category = shareDTO.getCategory();
        this.description = shareDTO.getDescription();
    }

    public Share(ShareDTO shareDTO, Share share){
        this.wkn = share.getWkn().toUpperCase();
        this.name = shareDTO.getName();
        this.category = shareDTO.getCategory();
        this.description = shareDTO.getDescription();
    }

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