package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class RestApi {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@GetMapping(path="/hello")
	public String sayHello(@RequestParam(required=false, defaultValue="mms") String name) {
		return "Hello " + name;
	}
	
	 // 3.1.1 Single file upload
    @PostMapping("/api/upload")
    // If not @RestController, uncomment this
    //@ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile) {

        LOGGER.debug("Single file upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity<>("please select a file!", HttpStatus.OK);
        }

        try {

            //saveUploadedFiles(Arrays.asList(uploadfile));
        	LOGGER.info("\n\n ****** File name: {}, type {}! ************", uploadfile.getOriginalFilename(), uploadfile.getContentType());
        	
        	this.processExcelFile(uploadfile.getInputStream());

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }
    
    private List<String> processExcelFile(InputStream stream) throws Exception {
    	List<String> result = Lists.newArrayList();    	
    	 
         //Create Workbook instance holding reference to .xlsx file
         try(XSSFWorkbook workbook = new XSSFWorkbook(stream);) {
	         //Get first/desired sheet from the workbook
	         XSSFSheet sheet = workbook.getSheetAt(0);
	         //Iterate through each rows one by one
	         Iterator<Row> rowIterator = sheet.iterator();
	         while (rowIterator.hasNext()) {
	             Row row = rowIterator.next();
	             String cellValue = row.getCell(0).getRichStringCellValue().toString();
	             result.add(cellValue);
	             LOGGER.info("\n\n ****** Cell value: {} ************", cellValue);
	         }
	         
	    	return result;
         }
    }

}
