package hs.aalen.financial_assets_portfolio.data;

import hs.aalen.financial_assets_portfolio.domain.Share;

public class ShareSwaggerDTO {
    /** This is the Data Transfer Object (DTO) for a share requested from the external API.
     * ShareSwaggerDTO is used to objectify the response information from the external API which
     * then can be converted to a PortfolioItemDTO by a copy constructor.
     * isin:          Identifier of the share.
     * name:          The name of the share.
     * type:          The category of the share.
     * description:   A detailed description of the share.
     *
     */

    private String isin;
    private String name;
    private String type;
    private String description;
    private double price;

    /* CONSTRUCTORS */

    public ShareSwaggerDTO(){}

    /* GETTERS AND SETTERS */
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }
}
