package com.valhalla.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.valhalla.domain.Microservice;

@Repository
public interface MicroserviceRepository extends GraphRepository<Microservice> {
	
	Microservice findByName(String name);
	
	@Query("MATCH ( microservice:Microservice {name:{0}} ) DELETE microservice")
	void deleteByName(String name);
}
