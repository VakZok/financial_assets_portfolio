package hs.aalen.financial_assets_portfolio.data;
import com.fasterxml.jackson.annotation.JsonFilter;
import hs.aalen.financial_assets_portfolio.domain.Share;

@JsonFilter("shareFilter")
public class ShareDTO {
    /* Class to create Data Transfer Objects for shares used for communication with the frontend
     * and not exposing too much information.
     */

    private String isin;
    private String name;
    private String category;
    private String description;

    /* Constructors */
    public ShareDTO(Share share) {
        this.isin = share.getIsin();
        this.name = share.getName();
        this.category = share.getCategory();
        this.description = share.getDescription();
    }

    public ShareDTO(ShareSwaggerDTO shareSwaggerDTO) {
        this.isin = shareSwaggerDTO.getIsin();
        this.name = shareSwaggerDTO.getName();
        this.category = shareSwaggerDTO.getType();
        this.description = shareSwaggerDTO.getDescription();
    }
    public ShareDTO(){}

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