package hs.aalen.financial_assets_portfolio.web;

import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class ShareController {
    private final ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    /* GET REQUESTS */
    @GetMapping("/shares/{wkn}")
    public ResponseEntity<Object> getShare(@PathVariable String wkn){
        Share share = shareService.getShare(wkn);
        if(share == null) {
            return new ResponseEntity<>(new ShareDTO(), HttpStatus.OK);
        } else {
            ShareDTO shareDTO = new ShareDTO(share);
            return new ResponseEntity<>(shareDTO, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/shares")
    public ResponseEntity<Object> getShareList() {
        List<Share> shareList = shareService.getShareList();
        ArrayList<ShareDTO> shareDTOList = new ArrayList<ShareDTO>();

        if(shareList.isEmpty()) {
            shareDTOList.add(new ShareDTO());
            return new ResponseEntity<>(shareDTOList, HttpStatus.OK);
        } else {
            shareDTOList.addAll(shareList.stream().map(ShareDTO::new).toList());
            return new ResponseEntity<>(shareDTOList, HttpStatus.OK);
        }
    }

    /* POST REQUESTS */
    @PostMapping("/shares/add")
    public ResponseEntity<Object> addShare(@RequestBody ShareDTO shareDTO){
        try{
            shareService.addShare(shareDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FormNotValidException e){
            return new ResponseEntity<>(e.getExceptions(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /* PUT REQUESTS */
    @PutMapping("/shares/update/{wkn}")
    public ResponseEntity<Object> updateShare(@PathVariable String wkn,
                                              @RequestBody ShareDTO shareDTO){
        try{
            shareService.updateShare(wkn, shareDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FormNotValidException e){
            return new ResponseEntity<>(e.getExceptions(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }
}
