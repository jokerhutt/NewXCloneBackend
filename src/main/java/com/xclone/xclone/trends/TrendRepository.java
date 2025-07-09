package com.xclone.xclone.trends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrendRepository  extends JpaRepository<TrendEntity, Integer> {

    @Query(value = "SELECT * FROM trends ORDER BY tweet_volume DESC LIMIT 5", nativeQuery = true)
    List<TrendEntity> findTop5ByTweetVolume();

}
