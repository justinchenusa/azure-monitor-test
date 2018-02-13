package com.gwos.azure.test.model;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExportResourceGroupTemplateTest {
	
	private static String jsonStr;
	private static String jsonStrMultiple;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ClassLoader classLoader = ExportResourceGroupTemplateTest.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("OneResourceTest.json");
		jsonStr = convertInputStream2String(is);
		//System.out.println(jsonStr);
		
		InputStream isMulti = classLoader.getResourceAsStream("MultiResourcesTest.json");
		jsonStrMultiple = convertInputStream2String(isMulti);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetResources() {		
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
		try {
			ExportResourceGroupTemplate exportTmpl = objMapper.readValue(jsonStr, ExportResourceGroupTemplate.class);
		
			assertNotNull(exportTmpl.getContentVersion());
			List<Resource> resources = exportTmpl.getResources();
			assertNotNull(resources);
			assertNotNull(resources.get(0).getType());
			assertEquals("Microsoft.Storage/storageAccounts", resources.get(0).getType());
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetResourcesMultiple() {		
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
		try {
			ExportResourceGroupTemplate exportTmpl = objMapper.readValue(jsonStrMultiple, ExportResourceGroupTemplate.class);
		
			assertNotNull(exportTmpl.getContentVersion());
			List<Resource> resources = exportTmpl.getResources();
			assertNotNull(resources);
			assertEquals(9, resources.size());
			assertNotNull(resources.get(1).getType());
			assertEquals("Microsoft.Compute/virtualMachines", resources.get(1).getType());
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String convertInputStream2String(InputStream is) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			is.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} 
		return sb.toString();
	}
	
}
