package hs.aalen.financial_assets_portfolio.persistence;

import hs.aalen.financial_assets_portfolio.domain.Likes;
import hs.aalen.financial_assets_portfolio.domain.LikesId;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface LikesRepository extends ListCrudRepository<Likes, LikesId> {
    public List<Likes> findAllByAccountUsername(String username);

    public void deleteAllByAccountUsername(String username);

}

