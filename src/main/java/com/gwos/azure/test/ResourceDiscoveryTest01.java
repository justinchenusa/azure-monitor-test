package com.gwos.azure.test;

import com.gwos.azure.utils.AuthUtil;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.resources.ResourceGroup;
import com.microsoft.azure.management.resources.ResourceGroupExportResult;
import com.microsoft.azure.management.resources.ResourceGroupExportTemplateOptions;
import com.microsoft.azure.management.resources.ResourceGroups;
import com.microsoft.azure.management.resources.Subscription;
import com.microsoft.azure.management.resources.Subscriptions;
import com.microsoft.rest.LogLevel;

public class ResourceDiscoveryTest01 {
	public static boolean runSample(Azure azure) {
		
		// get Subscriptions
		Subscriptions subscriptions = azure.subscriptions();
		for (Subscription subscription : subscriptions.list()) {
			System.out.println("Subscription Display Name: " + subscription.displayName());
			System.out.println("Subscription Id: " + subscription.subscriptionId());
			System.out.println("Subscription State: " + subscription.state());
		}
		
		// get resource groups
		ResourceGroups resourceGroups = azure.resourceGroups();
		for (ResourceGroup resourceGroup : resourceGroups.list()) {
			System.out.println("ResourceGroup Id: " + resourceGroup.id());
			System.out.println("ResourceGroup Name: " + resourceGroup.name());
			
			ResourceGroupExportTemplateOptions options = ResourceGroupExportTemplateOptions.INCLUDE_BOTH;
			ResourceGroupExportResult rgExpResult = resourceGroup.exportTemplate(options);
			System.out.println("ResourceGroup Export Result Json: " + rgExpResult.templateJson());
			
		}
		/*
		Providers providers = azure.providers();
		for (Provider provider : providers.list()) {
			System.out.println("Provider Key: " + provider.key());
			System.out.println("Provider Namespace: " + provider.namespace());
			System.out.println("Registeration State: " + provider.registrationState());
			for (ProviderResourceType pResType : provider.resourceTypes()) {
				System.out.println("\tResource Type: " + pResType.resourceType());
				Map<String, String> resProperties = pResType.properties();
			}
		}
		*/
		return true;
	}
	
	public static void main(String[] args) {
		try {
//			final File credFile = AuthUtil.getCrendentialFile("AZURE_AUTH_LOCATION");
//			Azure azure = Azure.configure().withLogLevel(LogLevel.NONE).authenticate(credFile).withDefaultSubscription();

			ApplicationTokenCredentials credentials = AuthUtil.getTokenCredential();
			Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(credentials).withDefaultSubscription();

			System.out.println("Selected subscription: " + azure.subscriptionId());

			runSample(azure);
						
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
