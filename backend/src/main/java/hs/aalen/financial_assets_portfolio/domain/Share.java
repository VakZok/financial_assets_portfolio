package hs.aalen.financial_assets_portfolio.domain;

import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
public class Share {
    /* PROPERTIES OF ENTITY */
    @Id
    @Column(unique = true)
    @Size(max = 6, message = "Die WKN darf nicht aus mehr als 6 Zeichen bestehen")
    @NotBlank(message = "WKN ist ein Pflichtfeld")
    private String wkn;
    @Size(max = 256, message = "Der Name darf nicht mehr als 256 Zeichen besitzen")
    @NotBlank(message = "Name ist ein Pflichtfeld")
    private String name;
    @Size(max = 256, message = "Die Kategorie darf nicht mehr als 256 Zeichen besitzen")
    @NotBlank(message = "Kategorie ist ein Pflichtfeld")
    private String category;
    @Size(max = 256, message = "Die Beschreibung darf nicht mehr als 256 Zeichen besitzen")
    @NotBlank(message = "Beschreibung ist ein Pflichtfeld")
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

    public Share(){}

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