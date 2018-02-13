package com.gwos.azure.test;

import java.util.List;

import org.joda.time.DateTime;

import com.gwos.azure.utils.VMMetricsUtils;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.VirtualMachine;
import com.microsoft.azure.management.monitor.Metric;
import com.microsoft.azure.management.monitor.MetricCollection;
import com.microsoft.azure.management.monitor.MetricDefinition;
import com.microsoft.azure.management.monitor.MetricDefinitions;
import com.microsoft.azure.management.monitor.ResultType;
import com.microsoft.azure.management.monitor.TimeSeriesElement;
import com.microsoft.rest.LogLevel;

public class CheckVirtualMachineMetrics {

	public static boolean runMetrics02(Azure azure) {

        //DateTime recordDateTime = DateTime.parse("2018-01-24T00:07:40.350Z");
		DateTime recordDateTime = DateTime.now();
		
		try {
			// =============================================================
			// List virtual machines in the resource group
			String resourceGroupName = "GWOSGROUP"; // My Ubuntu VM is created under this group

			System.out.println("----------------");
			PagedList<VirtualMachine> vms = azure.virtualMachines().list();
			for (VirtualMachine vm : vms) {
				List<MetricDefinition> listMetricDefinitionmds = azure.metricDefinitions().listByResource(vm.id());
				for (MetricDefinition metricDefinition : listMetricDefinitionmds) {					
			        // Query resource metrics
			        MetricCollection metricCollection = metricDefinition.defineQuery()
			                .startingFrom(recordDateTime.minusMinutes(10))	// last 10 minutes
			                .endsBefore(recordDateTime)
                            //.withAggregation("Average")
                            //.withInterval(Period.minutes(5))				// Commented to use 1 minutes interval by default
			                .withResultType(ResultType.DATA)
			                .execute();

			        VMMetricsUtils.printMetricCollection(metricCollection, vm.id());
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
	
	public static boolean runMetrics(Azure azure) {

        //DateTime recordDateTime = DateTime.parse("2018-01-24T00:07:40.350Z");
		DateTime recordDateTime = DateTime.now();
		
		try {
			// =============================================================
			// List virtual machines in the resource group
			String resourceGroupName = "GWOSGROUP"; // My Ubuntu VM is created under this group

			System.out.println("----------------");
			PagedList<VirtualMachine> vms = azure.virtualMachines().list();
			for (VirtualMachine vm : vms) {
				MetricDefinitions mds = azure.metricDefinitions();
				for (MetricDefinition m : mds.listByResource(vm.id())) {
					VMMetricsUtils.printMetricDefinition(m);
					//System.out.println(m.resourceId());
					
			        // Query resource metrics
			        MetricCollection metrics = m.defineQuery()
			                .startingFrom(recordDateTime.minusMinutes(10))
			                .endsBefore(recordDateTime)
			                .withResultType(ResultType.DATA)
			                .execute();
			        
			        for (Metric metric : metrics.metrics()) {
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
			// https://docs.microsoft.com/en-us/java/azure/java-sdk-azure-authenticate#authenticate-with-an-applicationtokencredentials-object
			// https://docs.microsoft.com/en-us/java/azure/java-sdk-azure-authenticate
			ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
					"{Client Id}", 	
			        "{Tenant Id}",	
			        "{Client Secret}", 	
			        AzureEnvironment.AZURE);

			Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(credentials).withDefaultSubscription();
			/*
			// =============================================================
			// Authenticate by a generated azure auth file configured in env. variable
			final File credFile = new File(System.getenv("AZURE_AUTH_LOCATION"));

			Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(credFile).withDefaultSubscription();
			*/
			// Print selected subscription
			System.out.println("Selected subscription: " + azure.subscriptionId());
			runMetrics02(azure);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
