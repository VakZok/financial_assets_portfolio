package hs.aalen.financial_assets_portfolio.domain;

import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Share {
    /** The entity class for share objects that are stored and
     * managed by the database.
     * isin:          Identifier of the share.
     * name:          The name of the share.
     * category:      The category of the share.
     * description:   A detailed description of the share.
     *
     */


    @Id
    @Column(unique = true)
    private String isin;
    private String name;
    private String category;
    private String description;

    /* MAPPED RELATIONSHIPS */
    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL)
    Set<Purchase> purchases;

    /* CONSTRUCTORS*/
    public Share(String isin, String name, String category, String description) {
        this.isin = isin;
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public Share(ShareDTO shareDTO){
        this.isin = shareDTO.getIsin().toUpperCase();
        this.name = shareDTO.getName();
        this.category = shareDTO.getCategory();
        this.description = shareDTO.getDescription();
    }

    public Share(ShareDTO shareDTO, Share share){
        this.isin = share.getIsin().toUpperCase();
        this.name = shareDTO.getName();
        this.category = shareDTO.getCategory();
        this.description = shareDTO.getDescription();
    }

    public Share(){}

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