package com.xclone.xclone.domain.like;
import com.xclone.xclone.domain.bookmark.Bookmark;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class LikeService {

    private LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public ArrayList<Integer> getAllUserBookmarks (Integer likerId) {
        ArrayList<Integer> likeIds = new ArrayList<>();
        ArrayList<Like> likes =  likeRepository.findAllByLikerId(likerId);
        for (Like like : likes) {
            likeIds.add(like.getLikedPostId());
        }
        return likeIds;
    }

    @Transactional
    public boolean addNewLike(Integer likerId, Integer likedPostId) {
        Like like = new Like();
        like.setLikedPostId(likedPostId);
        like.setLikerId(likerId);

        if (likeRepository.existsByLikerIdAndLikedPostId(likerId, likedPostId)) {
            return false;
        } else {
            likeRepository.save(like);
            return true;
        }

    }

    @Transactional
    public boolean deleteLike(Integer likerId, Integer likedPostId) {

        Optional<Like> toDelete = likeRepository.findByLikerIdAndLikedPostId(likerId, likedPostId);
        if (toDelete.isPresent()) {
            likeRepository.delete(toDelete.get());
            return true;
        } else {
            return false;
        }

    }

}
