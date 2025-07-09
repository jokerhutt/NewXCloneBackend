package com.xclone.xclone.trends;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trends")
public class TrendController {

    private final TrendRepository trendRepository;

    public TrendController(TrendRepository trendRepository) {
        this.trendRepository = trendRepository;
    }

    @GetMapping("/get")
    public ResponseEntity<?> getTrends() {
        List<TrendEntity> trendEntityList = trendRepository.findAll();
        return ResponseEntity.ok(trendEntityList);
    }

    @GetMapping("/get-top-five")
    public ResponseEntity<?> getTopFiveTrends() {
        return ResponseEntity.ok(trendRepository.findTop5ByTweetVolume());
    }

}