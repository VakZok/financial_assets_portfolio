package hs.aalen.financial_assets_portfolio.client;

import feign.RetryableException;
import feign.Retryer;

public class NaiveRetryer implements feign.Retryer {
    @Override
    public void continueOrPropagate(RetryableException e) {
        try {
            Thread.sleep(300L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    @Override
    public Retryer clone() {
        return new NaiveRetryer();
    }
}