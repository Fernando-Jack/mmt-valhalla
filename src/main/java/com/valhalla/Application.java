package com.valhalla;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.template.Neo4jOperations;


@SpringBootApplication
@EnableNeo4jRepositories
public class Application extends Neo4jConfiguration{
	
	private final static Logger log = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private Neo4jOperations operations;
	
	public static void main(String[] args) throws IOException {
		log.info("Initialization...");
        SpringApplication.run(Application.class, args);
    }
	
	@Bean
	public org.neo4j.ogm.config.Configuration getConfiguration() {
        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
        config
                .driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver");
        return config;
    }
	
	@Bean
    public GraphDatabaseService graphDatabaseService() {
		String databasePath = "./temp_db/testdb";
		GraphDatabaseService service = new GraphDatabaseFactory().newEmbeddedDatabase(new File(databasePath));
		
		String applyUniquenessOnMicroserviceName = "CREATE CONSTRAINT ON (m:Microservice) ASSERT m.name IS UNIQUE";
		Map<String, String> emptyParamaters = new HashMap<String, String>(); 
		operations.query(applyUniquenessOnMicroserviceName, emptyParamaters);
		
        return service; 
    }

	@Override
	public SessionFactory getSessionFactory() {
		return new SessionFactory(getConfiguration(),"com.valhalla.domain");
	}

}
