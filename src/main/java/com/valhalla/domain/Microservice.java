package com.valhalla.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import lombok.Getter;
import lombok.Setter;

@NodeEntity
public class Microservice {

	public Microservice(String name, String endpoint) {
		this.name = name;
		this.endpoint = endpoint;
	}

	public Microservice() {
	}

		
	@Override
	public String toString() {
		return "Microservice [id=" + id + ", name=" + name + ", endpoint=" + endpoint + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endpoint == null) ? 0 : endpoint.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Microservice other = (Microservice) obj;
		if (endpoint == null) {
			if (other.endpoint != null)
				return false;
		} else if (!endpoint.equals(other.endpoint))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	@Getter
	@GraphId
	private Long id;

	@Getter
	@Setter	
	public String name;
	
	@Getter
	@Setter	
	public String endpoint;

}