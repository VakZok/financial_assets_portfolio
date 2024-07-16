package hs.aalen.financial_assets_portfolio.client;

import feign.RetryableException;
import feign.Retryer;

public class NaiveRetryer implements feign.Retryer {
    /** Implementation of the Feign Retryer interface for handling retries.
     *  Waits 50 milliseconds after a Retryable Exception (e.g. timeout) occurs,
     *  then retries.
     */

    @Override
    public void continueOrPropagate(RetryableException e) {
        try {
            Thread.sleep(50L);
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