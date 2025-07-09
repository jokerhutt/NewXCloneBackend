package com.xclone.xclone.trends;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "trends")
public class TrendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "tweet_volume")
    private Integer tweetVolume;

    @Column(name = "recorded_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp recordedAt;

    public TrendEntity() {

    }

    public TrendEntity (String name, String url, Integer tweetVolume) {
        this.name = name;
        this.url = url;
        this.tweetVolume = tweetVolume;

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Integer getTweetVolume() {
        return tweetVolume;
    }

    public Timestamp getRecordedAt() {
        return recordedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTweetVolume(Integer tweetVolume) {
        this.tweetVolume = tweetVolume;
    }

    public void setRecordedAt(Timestamp recordedAt) {
        this.recordedAt = recordedAt;
    }
}