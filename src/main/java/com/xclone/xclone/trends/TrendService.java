package com.xclone.xclone.trends;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TrendService {

    private final RestTemplate restTemplate;
    private final TrendRepository trendRepository;

    @Value("${x.bearer.token}")
    private String xBearerToken;

    public TrendService(TrendRepository trendRepository, RestTemplate restTemplate) {
        this.trendRepository = trendRepository;
        this.restTemplate = restTemplate;
    }

//    @PostConstruct
//    public void initFetch() {
//        System.out.println("[Startup] Fetching trends...");
//        fetchTrends();
//    }
//
//    @Scheduled(cron = "0 0 0 */2 * *")
//    public void fetchTrends() {
//        try {
//            System.out.println("[Scheduler] Fetching trends from X API...");
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + xBearerToken);
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(
//                    "https://api.x.com/2/trends/by/woeid/44418",
//                    HttpMethod.GET,
//                    entity,
//                    String.class
//            );
//
//            JSONArray trends = new JSONObject(response.getBody()).getJSONArray("data");
//
//            for (int i = 0; i < trends.length(); i++) {
//                JSONObject trend = trends.getJSONObject(i);
//                String name = trend.getString("trend_name");
//                int volume = trend.optInt("tweet_count", -1);
//
//                TrendEntity trendEntity = new TrendEntity();
//                trendEntity.setName(name);
//                trendEntity.setTweetVolume(volume);
//                trendRepository.save(trendEntity);
//            }
//
//            System.out.println("[Scheduler] Trends saved successfully.");
//        } catch (Exception e) {
//            System.err.println("[Scheduler] Failed to fetch or save trends: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
}