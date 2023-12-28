package hs.aalen.financial_assets_portfolio.domain;

import jakarta.persistence.*;

@Entity
public class Likes{

    @EmbeddedId
    private LikesId id;

    @ManyToOne
    @MapsId("accountUsername")
    @JoinColumn(name = "accountUsername")
    private  Account account;

    @ManyToOne
    @MapsId("shareIsin")
    @JoinColumn(name = "shareIsin")
    private Share share;

    public Likes(LikesId id, Account account, Share share) {
        this.id = id;
        this.account = account;
        this.share = share;
    }
    public Likes(){}

    public LikesId getId() {
        return id;
    }

    public void setId(LikesId id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }
}


