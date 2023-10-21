package hs.aalen.financial_assets_portfolio.exceptions;

import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;

import java.util.ArrayList;

public class FormNotValidException extends Exception {
    private ArrayList<ExceptionDTO> exceptions;

    public FormNotValidException(String errorMessage, ArrayList<ExceptionDTO> exceptions){
        super(errorMessage);
        this.exceptions = exceptions;
    }

    public ArrayList<ExceptionDTO> getExceptions() {
        return exceptions;
    }
}
