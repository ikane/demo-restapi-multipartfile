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
	public void testSayHello() {
		
		ResponseEntity<String> response = this.restTemplate.getForEntity("/hello", String.class);
		
		Assertions.assertThat(response.getBody()).isEqualToIgnoringCase("hello mms");
	}
	
	@Test
	public void testUploadFile() throws Exception {
		
		//ClassPathResource resource = new ClassPathResource("classpath:test.xlsx");
		
		Resource resource = this.loader.getResource("classpath:test.xlsx");
		
		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();           
//		parts.add("file", new FileSystemResource(resource.getFile()));
//		parts.add("file", new ByteArrayResource(IOUtils.toByteArray(resource.getInputStream())));
		parts.add("file", new FileInputStream(resource.getFile()));
		
		FileBody uploadFilePart = new FileBody(resource.getFile());
		byte[] byteArray = IOUtils.toByteArray(resource.getInputStream());
		ByteArrayResource byteArrayResource = new ByteArrayResource(byteArray);
		
		HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
         
        
				
//        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(byteArray, headers);
//        HttpEntity<ByteArrayResource> requestEntity = new HttpEntity<>(byteArrayResource, headers);
        //HttpEntity<ByteArrayResource> requestEntity = new HttpEntity<>(byteArrayResource, headers);
		
//		String response = this.restTemplate.postForObject("/api/upload", parts, String.class);
		String response = this.restTemplate.postForObject("/api/upload", requestEntity, String.class);
		
		//ResponseEntity<String> response = this.restTemplate.getForEntity("/hello", String.class);
		
		Assertions.assertThat(response).containsIgnoringCase("success");
	}
	
	

}
