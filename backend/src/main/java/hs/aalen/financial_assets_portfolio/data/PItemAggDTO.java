package hs.aalen.financial_assets_portfolio.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PItemAggDTO {
    private ShareDTO shareDTO;
    private double avgPrice;
    private int totalQuantity;
    private ArrayList<PItemDTO> pItemDTOList;

    public PItemAggDTO(ShareDTO shareDTO, double avgPrice, int totalQuantity, ArrayList<PItemDTO> pItemDTOList) {
        this.shareDTO = shareDTO;
        this.avgPrice = avgPrice;
        this.totalQuantity = totalQuantity;
        this.pItemDTOList = pItemDTOList;
    }

    public PItemAggDTO(){};

    public ShareDTO getShareDTO() {
        return shareDTO;
    }

    public void setShareDTO(ShareDTO shareDTO) {
        this.shareDTO = shareDTO;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public ArrayList<PItemDTO> getpItemDTOList() {
        return pItemDTOList;
    }

    public void setpItemDTOList(ArrayList<PItemDTO> pItemDTOList) {
        this.pItemDTOList = pItemDTOList;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}




