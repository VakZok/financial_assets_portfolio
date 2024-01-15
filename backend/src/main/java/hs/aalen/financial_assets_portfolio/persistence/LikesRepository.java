package hs.aalen.financial_assets_portfolio.persistence;

import hs.aalen.financial_assets_portfolio.domain.Likes;
import hs.aalen.financial_assets_portfolio.domain.LikesId;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface LikesRepository extends ListCrudRepository<Likes, LikesId> {
    List<Likes> findAllByAccountUsername(String username);

    void deleteAllByAccountUsername(String username);

}

