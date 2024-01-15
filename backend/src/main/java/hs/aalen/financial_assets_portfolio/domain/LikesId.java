package hs.aalen.financial_assets_portfolio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LikesId implements Serializable {
    /** The LikesId class that is used as primary key for the
     * n-m relationships of the entity class Likes. The equals
     * and hashCode methods are overridden, such that the backend
     * can compare the "Likes" objects properly by accountUsername and shareISIN.
     * accountUsername: The username of the account object
     * shareIsin:       The isin of the share object related with the PortfolioItem
     */
    @Column(name = "accountUsername")
    private String accountUsername;
    @Column(name = "shareIsin")
    private String shareIsin;

    /* CONSTRUCTORS */

    public LikesId(String accountUsername, String shareIsin){
        this.accountUsername = accountUsername;
        this.shareIsin = shareIsin;
    }

    public LikesId(){}

    /* METHODS */

    @Override
    public int hashCode(){
        return Objects.hash(accountUsername, shareIsin);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return  true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LikesId other = (LikesId) obj;
        return accountUsername == other.accountUsername && shareIsin == other.shareIsin;
    }
}

