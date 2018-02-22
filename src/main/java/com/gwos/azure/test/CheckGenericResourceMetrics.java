package com.gwos.azure.test;

import java.util.List;

import org.joda.time.DateTime;

import com.gwos.azure.utils.AuthUtil;
import com.gwos.azure.utils.VMMetricsUtils;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.monitor.AggregationType;
import com.microsoft.azure.management.monitor.MetricAvailability;
import com.microsoft.azure.management.monitor.MetricCollection;
import com.microsoft.azure.management.monitor.MetricDefinition;
import com.microsoft.azure.management.monitor.ResultType;
import com.microsoft.azure.management.resources.GenericResource;
import com.microsoft.rest.LogLevel;

public class CheckGenericResourceMetrics {
	public static boolean runMetrics02(Azure azure) {		
        //DateTime recordDateTime = DateTime.parse("2018-01-24T00:07:40.350Z");
		DateTime recordDateTime = DateTime.now();
		
		try {
			// =============================================================
			// List virtual machines in the resource group
			String resourceGroupName = "GWOSGROUP"; // My Ubuntu VM is created under this group
			
			System.out.println("----------------");
			PagedList<GenericResource> genericResources = azure.genericResources().list();
			for (GenericResource genericResource : genericResources) {
				System.out.println("---------- Resource MetaData ----------");
//				System.out.println("Api Version: " + genericResource.apiVersion());
				System.out.println("Resource Id: " + genericResource.id());
				System.out.println("Key: " + genericResource.key());
				System.out.println("Name: " + genericResource.name());
//				System.out.println("ParentResourcePath: " + genericResource.parentResourcePath());
//				System.out.println("RegionName: " + genericResource.regionName());
				System.out.println("ResourceGroupName: " + genericResource.resourceGroupName());
				System.out.println("ResourceProviderNamespace: " + genericResource.resourceProviderNamespace());
				System.out.println("ResourceType: " + genericResource.resourceType());
				System.out.println("Type: " + genericResource.type());
				System.out.println("---------------------------------------");
				
				if (genericResource.id().endsWith("providers/Microsoft.Sql/servers/gwos02sqldb")) {	// exclude providers/Microsoft.Sql/servers
					System.out.println("Debug break Point - to check sqlserver");
				}
				List<MetricDefinition> metricDefinitions = azure.metricDefinitions().listByResource(genericResource.id());
				for (MetricDefinition metricDefinition : metricDefinitions) {	
					AggregationType aggregationType = metricDefinition.primaryAggregationType();
					System.out.println("AggregationType : " + aggregationType.name());
					List<MetricAvailability> availMetrics = metricDefinition.metricAvailabilities();
					 try {
				        // Query resource metrics
				        MetricCollection metricCollection = metricDefinition.defineQuery()
				                .startingFrom(recordDateTime.minusMinutes(5))	// last 10 minutes
				                .endsBefore(recordDateTime)
	                            //.withAggregation("Average, Total, Count")
	                            //.withInterval(Period.minutes(5))				// Commented to use 1 minutes interval by default
				                .withResultType(ResultType.DATA)
				                .execute();
			       
			        	VMMetricsUtils.printMetricCollection(metricCollection, genericResource.id());
			        } catch (Exception pf) {
			        	pf.printStackTrace();
			        }
				}
			}
			return true;
		} catch (Exception f) {
			f.printStackTrace();
		} finally {
			// TODO: nothing here
		}
		return false;
	}
	
	public static void main(String[] args) {
		try {
//			final File credFile = AuthUtil.getCrendentialFile("AZURE_AUTH_LOCATION");
//			Azure azure = Azure.configure().withLogLevel(LogLevel.NONE).authenticate(credFile).withDefaultSubscription();

			ApplicationTokenCredentials credentials = AuthUtil.getTokenCredential();
			Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(credentials).withDefaultSubscription();
			
			System.out.println("Selected subscription: " + azure.subscriptionId());
			
			runMetrics02(azure);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
