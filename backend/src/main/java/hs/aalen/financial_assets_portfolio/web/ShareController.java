package hs.aalen.financial_assets_portfolio.web;

import hs.aalen.financial_assets_portfolio.data.PItemDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class ShareController {
    @Autowired
    private ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    @GetMapping("/shares/{wkn}")
    public ResponseEntity<Object> getShare(@PathVariable String wkn){
        Share share = shareService.getShare(wkn);
        ShareDTO shareDTO = new ShareDTO(share);
        return new ResponseEntity<>(shareDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/shares", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getShareList() {
        List<Share> shareList = shareService.getShareList();
        List<ShareDTO> shareDTOList = shareList.stream().map(ShareDTO::new).toList();
        return new ResponseEntity<Object>(shareDTOList, HttpStatus.OK);
    }
}
