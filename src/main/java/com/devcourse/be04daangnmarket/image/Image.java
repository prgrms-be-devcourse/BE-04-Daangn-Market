package com.devcourse.be04daangnmarket.image;

import com.devcourse.be04daangnmarket.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = " images")
public class Image extends BaseEntity {

	@Column(length = 500, nullable = false)
	private String name;

	@Column(length = 1000, nullable = false)
	private String path;

	protected Image() {
	}

	public Image(String name, String path) {
		this.name = name;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

}
