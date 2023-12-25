package hs.aalen.financial_assets_portfolio.client;

import hs.aalen.financial_assets_portfolio.data.ShareSwaggerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "ShareSwagger", url = "https://hsaa-stock-exchange-service.azurewebsites.net/v1/stocks")
public interface ShareSwaggerClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{isin}", consumes = "application/json")
    ShareSwaggerDTO getShare(@RequestHeader("Api-Key") String apiKey);
}
