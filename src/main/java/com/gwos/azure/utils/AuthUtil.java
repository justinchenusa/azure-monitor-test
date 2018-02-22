package com.gwos.azure.utils;

import java.io.File;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;

public class AuthUtil {

	public static ApplicationTokenCredentials getTokenCredential() {
		// https://docs.microsoft.com/en-us/java/azure/java-sdk-azure-authenticate
		// https://docs.microsoft.com/en-us/java/azure/java-sdk-azure-authenticate#authenticate-with-an-applicationtokencredentials-object
		ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
				"{Client Id}", 	
		        "{Tenant Id}",	
		        "{Client Secret}", 	
		        AzureEnvironment.AZURE);

		return credentials;
	}
	
	public static File getCrendentialFile(String envName) {
		File credFile = new File(System.getenv(envName));
		return credFile;
	}
	
}
