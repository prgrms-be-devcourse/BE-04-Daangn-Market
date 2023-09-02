package com.devcourse.be04daangnmarket.beombu.image.repository;

import com.devcourse.be04daangnmarket.beombu.image.domain.DomainName;
import com.devcourse.be04daangnmarket.beombu.image.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ImageRepository extends JpaRepository<File, Long> {

    List<File> findAllByDomainNameAndDomainId(DomainName domainName, Long domainId);

    @Modifying(clearAutomatically = true)
    void deleteAllByDomainNameAndDomainId(DomainName domainName, Long domainId);
}
