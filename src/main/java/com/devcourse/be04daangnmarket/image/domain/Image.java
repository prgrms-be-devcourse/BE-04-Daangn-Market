package com.devcourse.be04daangnmarket.image.domain;

import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Image extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DomainName domainName;

    @Column(nullable = false)
    private Long domainId;

    protected Image() {
    }

    public Image(String name, String type, long size, String path, DomainName domainName, Long domainId) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.path = path;
        this.status = Status.ALIVE;
        this.domainName = domainName;
        this.domainId = domainId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public Status getStatus() {
        return status;
    }

    public DomainName getDomainName() {
        return domainName;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void changeStatus() {
        this.status = Status.DELETED;
    }
}
