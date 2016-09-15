package com.valhalla.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.valhalla.domain.Microservice;
import com.valhalla.repository.MicroserviceRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MicroserviceRestControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private MicroserviceRepository microserviceRepository;

	private static final String BASE_URL = "/microservice/";

	@Before
	public void setup() throws Exception {		
	}

	@Test
	public void shouldReturnMicroserviceByName() {
		this.restTemplate.delete(BASE_URL);
		
		String microserviceName = "Elli";
		String endpoint = "endpointElli";
		ResponseEntity<Microservice> created = postMicroservice(microserviceName, endpoint);
		ResponseEntity<Microservice> returned = getMicroservice(microserviceName);

		assertThat(created.getBody().getName()).isEqualTo(microserviceName);
		assertThat(created.getBody().getEndpoint()).isEqualTo(endpoint);
		assertThat(returned.getBody().getName()).isEqualTo(microserviceName);
		assertThat(returned.getBody().getEndpoint()).isEqualTo(endpoint);
	}
	
	@Test
	public void shouldUpdateMicroservice() {
		this.restTemplate.delete(BASE_URL);
		
		String microserviceName = "Elli";
		String endpoint = "endpointElli";
		
		String microserviceNameUpdate = "Elli2";
		String endpointUpdate = "endpointElli2";
		
		ResponseEntity<Microservice> created = postMicroservice(microserviceName, endpoint);
		ResponseEntity<Microservice> returned = getMicroservice(microserviceName);
		
		updateMicroservice(new Microservice(microserviceName, endpoint), new Microservice(microserviceNameUpdate, endpointUpdate));
		
		ResponseEntity<Microservice> returnedUpdate = getMicroservice(microserviceNameUpdate);

		assertThat(created.getBody().getName()).isEqualTo(microserviceName);
		assertThat(created.getBody().getEndpoint()).isEqualTo(endpoint);
		
		assertThat(returned.getBody().getName()).isEqualTo(microserviceName);
		assertThat(returned.getBody().getEndpoint()).isEqualTo(endpoint);
		
		assertThat(returnedUpdate.getBody().getName()).isEqualTo(microserviceNameUpdate);
		assertThat(returnedUpdate.getBody().getEndpoint()).isEqualTo(endpointUpdate);
	}	

	@Test
	public void shouldReturnAllMicroservices() {
		this.restTemplate.delete(BASE_URL);
		
		postMicroservice("Magni","endpointMagni");
		postMicroservice("Mani","endpointMani");
		postMicroservice("Nott","endpointNott");		
		
		@SuppressWarnings("unchecked")
		Collection<Microservice> microservices = this.restTemplate.getForObject("/microservice", Collection.class);
		assertThat(microservices.size()).isEqualTo(3);
	}
	
	@Test
	public void shouldRemoveMicroserviceByName() {
		this.restTemplate.delete(BASE_URL);
		
		String microserviceName = "Elli";
		String endpoint = "endpointElli";
		
		ResponseEntity<Microservice> created = postMicroservice(microserviceName, endpoint);
		ResponseEntity<Microservice> returned = getMicroservice(microserviceName);
		removeMicroservice(returned.getBody());
		ResponseEntity<Microservice> returnedAfterDelete = getMicroservice(microserviceName);		

		assertThat(created.getBody().getName()).isEqualTo(microserviceName);
		assertThat(created.getBody().getEndpoint()).isEqualTo(endpoint);
		assertThat(returned.getBody().getName()).isEqualTo(microserviceName);
		assertThat(returned.getBody().getEndpoint()).isEqualTo(endpoint);
		assertThat(returnedAfterDelete.getBody()).isNull();		
	}
	
	@Test
	public void shouldNotDuplicateMicroserviceName() {		
		String microserviceName = "Elli";
		String endpoint = "endpointElli";
		
		this.restTemplate.delete(BASE_URL);
		
		ResponseEntity<Microservice> created1 = postMicroservice(microserviceName, endpoint);
		ResponseEntity<Microservice> created2 = postMicroservice(microserviceName, endpoint);
		
		@SuppressWarnings("unchecked")
		Collection<Microservice> microservices = this.restTemplate.getForObject("/microservice", Collection.class);
		
		assertThat(microservices.size()).isEqualTo(1);
		assertThat(created1.getBody().getName()).isEqualTo(microserviceName);
		assertThat(created1.getBody().getEndpoint()).isEqualTo(endpoint);
		assertThat(created2.getBody().getName()).isNull();
		
	}
	
	private void updateMicroservice(Microservice current, Microservice update) {
		this.restTemplate.put(BASE_URL + "{name}", update, current.name);
	}
	
	private ResponseEntity<Microservice> postMicroservice(String microserviceName, String endpoint) {
		ResponseEntity<Microservice> created = this.restTemplate.postForEntity(BASE_URL,
				new Microservice(microserviceName, endpoint), Microservice.class);
		return created;
	}
	
	private void removeMicroservice(Microservice microservice) {
		this.restTemplate.delete(BASE_URL + "{name}", microservice.name);
	}

	private ResponseEntity<Microservice> getMicroservice(String microserviceName) {
		ResponseEntity<Microservice> returned = this.restTemplate.getForEntity(BASE_URL + "{name}", Microservice.class,
				microserviceName);
		return returned;
	}

}