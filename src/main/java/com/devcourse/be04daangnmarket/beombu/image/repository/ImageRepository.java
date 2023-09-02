package com.devcourse.be04daangnmarket.beombu.image.repository;

import com.devcourse.be04daangnmarket.beombu.image.domain.DomainName;
import com.devcourse.be04daangnmarket.beombu.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByDomainNameAndDomainId(DomainName domainName, Long domainId);

    @Modifying(clearAutomatically = true)
    void deleteAllByDomainNameAndDomainId(DomainName domainName, Long domainId);
}
