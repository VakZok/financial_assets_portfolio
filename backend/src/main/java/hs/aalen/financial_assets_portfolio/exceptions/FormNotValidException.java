package hs.aalen.financial_assets_portfolio.exceptions;

import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;

import java.util.ArrayList;

public class FormNotValidException extends Exception {
    /** Custom exception class that is used to return
     * form validation errors
     * exceptions: Array List that contains the exceptions occured during validation
     */

    private ArrayList<ExceptionDTO> exceptions;

    /* CONSTRUCTOR */

    public FormNotValidException(String errorMessage, ArrayList<ExceptionDTO> exceptions){
        super(errorMessage);
        this.exceptions = exceptions;
    }

    public ArrayList<ExceptionDTO> getExceptions() {
        return exceptions;
    }
}
