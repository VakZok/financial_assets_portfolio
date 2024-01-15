package hs.aalen.financial_assets_portfolio.domain;

import jakarta.persistence.*;

@Entity
public class Likes{
    /**
     * The entity class for Likes, containing the account
     * and the share that are associated, if a user likes a
     * PortfolioItem. Mapping is done by an embedded ID.
     * id:       The embedded id created from the primary keys of account and share
     * account:  The account that likes a PortfolioItem
     * share:    The share that is liked by an account
     */

    @EmbeddedId
    private LikesId id;

    /* MAPPED RELATIONSHIPS */
    @ManyToOne
    @MapsId("accountUsername")
    @JoinColumn(name = "accountUsername")
    private  Account account;

    @ManyToOne
    @MapsId("shareIsin")
    @JoinColumn(name = "shareIsin")
    private Share share;

    /* CONSTRUCTORS */
    public Likes(LikesId id, Account account, Share share) {
        this.id = id;
        this.account = account;
        this.share = share;
    }
    public Likes(){}

    /* GETTERS AND SETTERS */
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


