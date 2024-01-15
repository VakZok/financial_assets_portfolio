package hs.aalen.financial_assets_portfolio.data;

public class ExceptionDTO {
    /** Class to create Data Transfer Objects for
     * validation exceptions used for communication with the frontend.
     * name:    The name of the attribute which failed validation
     * message: The message that should be displayed by the frontend
     */

    private String name;
    private String message;

    /* Constructors */
    public ExceptionDTO(String name, String message) {
        this.name = name;
        this.message = message;
    }

    /* Getters and Setters */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
