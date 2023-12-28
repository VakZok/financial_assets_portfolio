package hs.aalen.financial_assets_portfolio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LikesId implements Serializable {
    @Column(name = "accountUsername")
    private String accountUsername;

    @Column(name = "shareIsin")
    private String shareIsin;

    public LikesId(String accountUsername, String shareIsin){
        this.accountUsername = accountUsername;
        this.shareIsin = shareIsin;
    }

    public LikesId(){}

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

