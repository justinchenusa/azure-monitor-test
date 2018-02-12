package com.gwos.azure.test;

import java.io.File;
import java.util.List;

import org.joda.time.DateTime;

import com.gwos.azure.utils.VMMetricsUtils;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.VirtualMachine;
import com.microsoft.azure.management.cosmosdb.CosmosDBAccount;
import com.microsoft.azure.management.monitor.Metric;
import com.microsoft.azure.management.monitor.MetricCollection;
import com.microsoft.azure.management.monitor.MetricDefinition;
import com.microsoft.azure.management.monitor.MetricDefinitions;
import com.microsoft.azure.management.monitor.ResultType;
import com.microsoft.azure.management.monitor.TimeSeriesElement;
import com.microsoft.rest.LogLevel;

public class CheckDbMetrics {
	public static boolean runMetrics(Azure azure) {

        //DateTime recordDateTime = DateTime.parse("2018-01-24T00:07:40.350Z");
		DateTime recordDateTime = DateTime.now();
		
		try {
			// =============================================================
			// List virtual machines in the resource group
			String resourceGroupName = "GWOSGROUP"; // My Ubuntu VM is created under this group

			System.out.println("----------------");
			PagedList<CosmosDBAccount> cmosDbActs = azure.cosmosDBAccounts().list();
			for (CosmosDBAccount cmosDbAct : cmosDbActs) {
				MetricDefinitions mds = azure.metricDefinitions();
				for (MetricDefinition m : mds.listByResource(cmosDbAct.id())) {
					VMMetricsUtils.printMetricDefinition(m);
					//System.out.println(m.resourceId());
					
			        // Query resource metrics
			        MetricCollection colMetrics = m.defineQuery()
			                .startingFrom(recordDateTime.minusMinutes(10))
			                .endsBefore(recordDateTime)
			                .withResultType(ResultType.DATA)
			                .execute();
			        
			        for (Metric metric : colMetrics.metrics()) {
			        	VMMetricsUtils.printMetricMetadata(metric);

			        	List<TimeSeriesElement> tses = metric.timeseries();
			        	for (TimeSeriesElement tse : tses) {
			        		VMMetricsUtils.printMetrics(tse.data());
			        	}
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
			// =============================================================
			// Authenticate by a generated azure auth file configured in env. variable
			final File credFile = new File(System.getenv("AZURE_AUTH_LOCATION"));

			Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(credFile).withDefaultSubscription();
			
			// Print selected subscription
			System.out.println("Selected subscription: " + azure.subscriptionId());
			runMetrics(azure);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
