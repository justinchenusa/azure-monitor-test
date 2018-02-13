package com.gwos.azure.test.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect
public class Resource {
	private String comments;
	
	private String type;
	
	@JsonIgnore
	private Object sku;
	
	private String kind;
	
	private String name;
	
	private String apiVersion;
	
	private String location;
	
	@JsonIgnore
	private Object tags;
	
	@JsonIgnore
	private String scale;
	
	@JsonIgnore
	private Object properties;
	
	@JsonIgnore
	private List<Object> dependsOn;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getSku() {
		return sku;
	}

	public void setSku(Object sku) {
		this.sku = sku;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Object getTags() {
		return tags;
	}

	public void setTags(Object tags) {
		this.tags = tags;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public Object getProperties() {
		return properties;
	}

	public void setProperties(Object properties) {
		this.properties = properties;
	}

	public List<Object> getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(List<Object> dependsOn) {
		this.dependsOn = dependsOn;
	}
	
}
