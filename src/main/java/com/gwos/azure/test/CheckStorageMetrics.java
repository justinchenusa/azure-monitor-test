package com.gwos.azure.test;

import java.util.List;

import org.joda.time.DateTime;

import com.gwos.azure.utils.AuthUtil;
import com.gwos.azure.utils.VMMetricsUtils;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.monitor.MetricCollection;
import com.microsoft.azure.management.monitor.MetricDefinition;
import com.microsoft.azure.management.monitor.ResultType;
import com.microsoft.azure.management.storage.StorageAccount;
import com.microsoft.rest.LogLevel;

public class CheckStorageMetrics {
	public static boolean runMetrics02(Azure azure) {		
        //DateTime recordDateTime = DateTime.parse("2018-01-24T00:07:40.350Z");
		DateTime recordDateTime = DateTime.now();
		
		try {
			// =============================================================
			// List virtual machines in the resource group
			String resourceGroupName = "cloud-shell-storage-westus"; // Storage account is under this group
			
			System.out.println("----------------");
			PagedList<StorageAccount> storageActs = azure.storageAccounts().listByResourceGroup(resourceGroupName);
			for (StorageAccount storageAct : storageActs) {
				List<MetricDefinition> metricDefinitions = azure.metricDefinitions().listByResource(storageAct.id());
				for (MetricDefinition metricDefinition : metricDefinitions) {					
			        // Query resource metrics
			        MetricCollection metricCollection = metricDefinition.defineQuery()
			                .startingFrom(recordDateTime.minusMinutes(10))	// last 10 minutes
			                .endsBefore(recordDateTime)
                            //.withAggregation("Average")
                            //.withInterval(Period.minutes(5))				// Commented to use 1 minutes interval by default
			                .withResultType(ResultType.DATA)
			                .execute();

			        VMMetricsUtils.printMetricCollection(metricCollection, storageAct.id());
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
