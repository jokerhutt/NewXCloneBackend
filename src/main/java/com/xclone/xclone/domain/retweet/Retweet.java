package com.xclone.xclone.domain.retweet;

import jakarta.persistence.*;

@Entity
@Table (name = "retweets")
public class Retweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reference_Id")
    private Integer referenceId;

    @Column(name = "retweeter_id")
    private Integer retweeterId;

    @Column(name = "type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getRetweeterId() {
        return retweeterId;
    }

    public void setRetweeterId(Integer retweeterId) {
        this.retweeterId = retweeterId;
    }
}
