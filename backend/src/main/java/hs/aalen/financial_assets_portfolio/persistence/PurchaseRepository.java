package hs.aalen.financial_assets_portfolio.persistence;

import hs.aalen.financial_assets_portfolio.domain.Purchase;
import org.springframework.data.repository.ListCrudRepository;
import java.util.ArrayList;


public interface PurchaseRepository extends ListCrudRepository<Purchase, Long> {
    /** Repository class for purchases */
    ArrayList<Purchase> findAllByShare_Isin(String isin);
}


