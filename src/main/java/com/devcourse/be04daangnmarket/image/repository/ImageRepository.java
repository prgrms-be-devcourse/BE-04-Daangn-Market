package com.devcourse.be04daangnmarket.image.repository;

import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByDomainNameAndDomainId(DomainName domainName, Long domainId);

    @Modifying(clearAutomatically = true)
    void deleteAllByDomainNameAndDomainId(DomainName domainName, Long domainId);
}
