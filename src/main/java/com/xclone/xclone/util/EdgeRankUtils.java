package com.xclone.xclone.util;

import com.xclone.xclone.domain.feed.FeedEntry;
import com.xclone.xclone.domain.feed.PostRank;
import com.xclone.xclone.domain.post.Post;

import java.util.ArrayList;
import java.util.List;

public class EdgeRankUtils {

    public static ArrayList<FeedEntry> generateFeedEntriesList (Integer userId, ArrayList<PostRank> feed) {

        ArrayList<FeedEntry> feedEntries = new ArrayList<>();
        for (int i = 0; i < feed.size(); i++) {
            PostRank pr = feed.get(i);
            FeedEntry feedEntry = new FeedEntry();
            feedEntry.setUserId(userId);
            feedEntry.setPostId(pr.post.getId());
            feedEntry.setScore(pr.totalScore);
            feedEntry.setPosition(i);
            feedEntries.add(feedEntry);
        }
        return feedEntries;
    }

    public static ArrayList<PostRank> generatePostRankList (List<Post> posts) {
        ArrayList<PostRank> postRanks = new ArrayList<>();
        for (Post post : posts) {
            postRanks.add(new PostRank(post));
        }
        return postRanks;
    }

}
