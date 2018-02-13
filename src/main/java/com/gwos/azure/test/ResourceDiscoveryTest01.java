package com.gwos.azure.test;

import com.microsoft.azure.AzureEnvironment;
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
			// https://docs.microsoft.com/en-us/java/azure/java-sdk-azure-authenticate
			ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
					"{Client Id}", 	
			        "{Tenant Id}",	
			        "{Client Secret}", 	
			        AzureEnvironment.AZURE);

			Azure azure = Azure
			        .configure()
			        .withLogLevel(LogLevel.BASIC)
			        .authenticate(credentials)
			        //.withSubscription("{SubscriptionId}");
			        .withDefaultSubscription();
			/*
			// =============================================================
			// Authenticate by a generated azure auth file configured in env. variable
			final File credFile = new File(System.getenv("AZURE_AUTH_LOCATION"));

			Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(credFile).withDefaultSubscription();
			*/
			// Print selected subscription
			System.out.println("Selected subscription: " + azure.subscriptionId());
			runSample(azure);
						
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
