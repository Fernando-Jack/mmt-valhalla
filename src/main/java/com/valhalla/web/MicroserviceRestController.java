package com.valhalla.web;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.valhalla.domain.Microservice;
import com.valhalla.repository.MicroserviceRepository;

@RestController
@RequestMapping("/microservice")
public class MicroserviceRestController {

	private final static Logger log = LoggerFactory.getLogger("stash");

	private final MicroserviceRepository microserviceRepository;

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<Microservice> add(@RequestBody Microservice input) {

		log.info("add microservice");
		log.info("log message {} {} {}", 
				keyValue("requestmethod", RequestMethod.POST.toString()),
				keyValue("requesturi", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()),
				keyValue("requestbody", input.toString())
				);

		Microservice result = this.microserviceRepository.save(input);
		
		if(result == null)
			return new ResponseEntity<Microservice>(HttpStatus.UNPROCESSABLE_ENTITY);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}")
				.buildAndExpand(result.getId()).toUri());
		
		return new ResponseEntity<Microservice>(result, httpHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	ResponseEntity<Microservice> getByName(@PathVariable String name) {	
		
		log.info("get microservice by name");
		log.info("log message {} {}", 
				keyValue("requestmethod", RequestMethod.GET.toString()),
				keyValue("requesturi", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString())
				);
		
		Microservice result = microserviceRepository.findByName(name);
		
		if(result == null)
			return new ResponseEntity<Microservice>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<Microservice>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{name}", method = RequestMethod.PUT)
	ResponseEntity<Microservice> updateByName(@PathVariable String name, @RequestBody Microservice input) {	
		
		log.info("update microservice by name");
		log.info("log message {} {}", 
				keyValue("requestmethod", RequestMethod.PUT.toString()),
				keyValue("requesturi", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString())
				);
		
		Microservice result = microserviceRepository.save(input);
		
		if(result == null)
			return new ResponseEntity<Microservice>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<Microservice>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
	HttpStatus removeByName(@PathVariable String name) {	
		
		log.info("delete microservice by name");
		log.info("log message {} {}", 
				keyValue("requestmethod", RequestMethod.DELETE.toString()),
				keyValue("requesturi", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString())				
				);	

		microserviceRepository.deleteByName(name);
		
		return HttpStatus.ACCEPTED;
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	HttpStatus removeAll() {	
		
		log.info("delete all microservice");
		log.info("log message {} {}", 
				keyValue("requestmethod", RequestMethod.DELETE.toString()),
				keyValue("requesturi", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString())				
				);	

		microserviceRepository.deleteAll();
		
		return HttpStatus.ACCEPTED;
	}

	@RequestMapping(method = RequestMethod.GET)
	ResponseEntity<?> getAll() {
		
		log.info("get all microservices");
		log.info("log message {} {}", 
				keyValue("requestmethod", RequestMethod.GET.toString()),
				keyValue("requesturi", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString())				
				);
		
		Iterable<Microservice> results = this.microserviceRepository.findAll();
		
		if(results == null)
			return new ResponseEntity<Microservice>(HttpStatus.INTERNAL_SERVER_ERROR);

		
		return new ResponseEntity<>(results, HttpStatus.OK);
	}

	@Autowired
	MicroserviceRestController(MicroserviceRepository microserviceRepository) {
		this.microserviceRepository = microserviceRepository;
	}

	@SuppressWarnings("serial")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	class MicroserviceNotFound extends RuntimeException {
		public MicroserviceNotFound(String name) {
			super("could not find '" + name + "'.");
		}
	}
	
	@SuppressWarnings("serial")
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	class MicroserviceNotCreated extends RuntimeException {
		public MicroserviceNotCreated(String name) {
			super("could not create '" + name + "'.");
		}
	}
}