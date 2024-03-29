package com.devcourse.be04daangnmarket.comment.repository;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT c FROM Comment c JOIN fetch c.member WHERE c.post.id=:postId AND c.seq=0")
    List<Comment> findAllByPostIdToSeqIsZero(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c WHERE c.commentGroup=:commentGroup AND c.seq > 0 ORDER BY c.seq ASC")
    List<Comment> findRepliesByCommentGroup(@Param("commentGroup") int commentGroup);

    @Query("SELECT DISTINCT m FROM Comment c JOIN c.member m WHERE c.post.id=:postId AND c.member.id <> :writerId")
    Page<Member> findDistinctMembersByPostIdAndNotInWriterId(@Param("postId") Long postId, @Param("writerId") Long writerId, Pageable pageable);
}
