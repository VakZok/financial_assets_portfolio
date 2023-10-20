package hs.aalen.financial_assets_portfolio.exceptions;

public class ShareAlreadyExistsException extends Exception{
    public ShareAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}
