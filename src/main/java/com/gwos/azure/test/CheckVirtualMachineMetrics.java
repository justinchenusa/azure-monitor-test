package com.gwos.azure.test;

import java.io.File;
import java.util.List;

import org.joda.time.DateTime;

import com.gwos.azure.utils.MetricsUtils;
import com.gwos.azure.utils.Utils;
import com.microsoft.azure.PagedList;
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

	public static boolean runSample(Azure azure) {

		try {
			// =============================================================
			// List virtual machines in the resource group
			String resourceGroupName = "GWOSGROUP"; // My Ubuntu VM is created
													// under this group
			System.out.println("Printing list of VMs =======");
			for (VirtualMachine virtualMachine : azure.virtualMachines().listByResourceGroup(resourceGroupName)) {
				Utils.print(virtualMachine);
			}

			System.out.println("----------------");
			PagedList<VirtualMachine> vms = azure.virtualMachines().list();
			for (VirtualMachine vm : vms) {
				System.out.println("-- vm = " + vm.computerName());
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
//				System.out.println("-- vm = " + vm.computerName());			
				MetricDefinitions mds = azure.metricDefinitions();
				for (MetricDefinition m : mds.listByResource(vm.id())) {
					MetricsUtils.printMetricDefinition(m);
					//System.out.println(m.resourceId());
					
			        // Query resource metrics
			        MetricCollection metrics = m.defineQuery()
			                .startingFrom(recordDateTime.minusMinutes(10))
			                .endsBefore(recordDateTime)
			                .withResultType(ResultType.DATA)
			                .execute();
			        
			        for (Metric metric : metrics.metrics()) {
			        	MetricsUtils.printVMMetricMetadata(metric);

			        	List<TimeSeriesElement> tses = metric.timeseries();
			        	for (TimeSeriesElement tse : tses) {
			        		MetricsUtils.printVMMetrics(tse.data());
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
			//runSample(azure);
			
			// Print selected subscription
			System.out.println("Selected subscription: " + azure.subscriptionId());
			runMetrics(azure);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
