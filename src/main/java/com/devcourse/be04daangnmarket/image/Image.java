package com.devcourse.be04daangnmarket.image;

import com.devcourse.be04daangnmarket.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = " images")
public class Image extends BaseEntity {

	@Column(length = 500, nullable = false)
	private String imageName;

	@Column(length = 1000, nullable = false)
	private String imagePath;

	public Image() {
	}

	public Image(String imageName, String imagePath) {
		this.imageName = imageName;
		this.imagePath = imagePath;
	}

	public String getImageName() {
		return imageName;
	}

	public String getImagePath() {
		return imagePath;
	}

}
