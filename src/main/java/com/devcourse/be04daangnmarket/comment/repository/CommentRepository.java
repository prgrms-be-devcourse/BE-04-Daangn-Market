package com.devcourse.be04daangnmarket.comment.repository;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT max(c.commentGroup) FROM Comment c")
    Optional<Integer> findMaxCommentGroup();

    @Query("SELECT max(c.seq) from Comment c where c.commentGroup=:commentGroup")
    Optional<Integer> findMaxSeqFromCommentGroup(@Param("commentGroup") int commentGroup);

    List<Comment> findAllByCommentGroup(int commentGroup);

    @Query("SELECT c FROM Comment c WHERE c.postId=:postId AND c.seq=0")
    List<Comment> findAllByPostIdToSeqIsZero(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c WHERE c.commentGroup=:commentGroup AND c.seq > 0 ORDER BY c.seq ASC")
    List<Comment> findRepliesByCommentGroup(@Param("commentGroup") int commentGroup);
}
