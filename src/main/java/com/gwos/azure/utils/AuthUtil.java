package com.gwos.azure.utils;

import java.io.File;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;

public class AuthUtil {

	public static ApplicationTokenCredentials getTokenCredential() {
		// https://docs.microsoft.com/en-us/java/azure/java-sdk-azure-authenticate#authenticate-with-an-applicationtokencredentials-object
		// https://docs.microsoft.com/en-us/java/azure/java-sdk-azure-authenticate
/*		ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
				"{Client Id}", 	
		        "{Tenant Id}",	
		        "{Client Secret}", 	
		        AzureEnvironment.AZURE);
*/		        
		ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
				"164452dc-012b-4767-8e00-f1e345db1702", 	// client id
		        "2c00c573-412b-4d19-946d-2e488b7e1929",		// tenant id
		        "b0e08c0a-6857-4b0f-91f6-36b9c7553adf", 	// key - clientSecret
		        AzureEnvironment.AZURE);

		return credentials;
	}
	
	public static File getCrendentialFile(String envName) {
		File credFile = new File(System.getenv(envName));
		return credFile;
	}
	
}
