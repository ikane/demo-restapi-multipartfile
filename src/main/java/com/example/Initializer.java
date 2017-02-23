package com.example;

import java.lang.invoke.MethodHandles;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//@Component
public class Initializer implements CommandLineRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	
	@Autowired
	private ResourceLoader loader;


	private RestTemplate restTemplate = new RestTemplate();
	

	@Override
	public void run(String... arg0) throws Exception {
		
		Resource resource = this.loader.getResource("classpath:test.xlsx");
		
		LOGGER.info("\n\n*********** Sending file {}", resource.getFilename());
		
		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();           
//		parts.add("file", new FileSystemResource(resource.getFile()));
		parts.add("file", new ByteArrayResource(IOUtils.toByteArray(resource.getInputStream())));
		
		String response = this.restTemplate.postForObject("http://localhost:8080/api/upload", parts, String.class);

		LOGGER.info("\n\n*********** Response {}", resource.getFilename());
	}

}
