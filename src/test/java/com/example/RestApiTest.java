package com.example;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.entity.mime.content.FileBody;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestApiTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private ResourceLoader loader;
	
	@Test
	public void testUploadFile() throws Exception {
		
		Resource resource = this.loader.getResource("classpath:test.xlsx");
		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();           
//		parts.add("file", new FileSystemResource(resource.getFile()));
		parts.add("file", new ByteArrayResource(IOUtils.toByteArray(resource.getInputStream())));
		
		String response = this.restTemplate.postForObject("/api/upload", parts, String.class);
		
		Assertions.assertThat(response).containsIgnoringCase("success");
	}
}
