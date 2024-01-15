package hs.aalen.financial_assets_portfolio.persistence;

import hs.aalen.financial_assets_portfolio.domain.Share;
import org.springframework.data.repository.ListCrudRepository;
public interface ShareRepository extends ListCrudRepository<Share, String> {
    Share findByIsin(String isin);

    boolean existsByIsin(String isin);
}
