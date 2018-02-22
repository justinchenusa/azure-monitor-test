package com.gwos.azure.utils;

import java.util.List;

import com.microsoft.azure.management.compute.VirtualMachine;
import com.microsoft.azure.management.monitor.MetadataValue;
import com.microsoft.azure.management.monitor.Metric;
import com.microsoft.azure.management.monitor.MetricCollection;
import com.microsoft.azure.management.monitor.MetricDefinition;
import com.microsoft.azure.management.monitor.MetricValue;
import com.microsoft.azure.management.monitor.TimeSeriesElement;

public class VMMetricsUtils {

	public static void printMetricCollection(MetricCollection metricCollection, String resourceId) {

		if (resourceId.contains("JCUbuntuVMVNET")) {
			System.out.println("Break Point");
		}
		
        System.out.println("-- MetricCollection MetaData for Resource Id '" + resourceId + "':");
        //System.out.println("Query time: " + metricCollection.timespan());
        //System.out.println("Time Grain: " + metricCollection.interval());
        //System.out.println("Cost: " + metricCollection.cost());
		
        for (Metric metric : metricCollection.metrics()) {
            System.out.println("\t-- Metric MetaData");
            System.out.println("\tName: " + metric.name().value() + ", localized: " + metric.name().localizedValue());
            System.out.println("\tType: " + metric.type());
            System.out.println("\tUnit: " + metric.unit());
            System.out.println("\t# of TimeSeriesElement: " + metric.timeseries().size() + ", Time Series: ");
            for (TimeSeriesElement timeElement : metric.timeseries()) {
                System.out.println("\t\t-- TimeSeriesElement Metadata: ");
                for (MetadataValue metadata : timeElement.metadatavalues()) {
                    System.out.println("\t\t\t" + metadata.name().localizedValue() + ": " + metadata.value());
                }
                System.out.println("\t\t-- TimeSeriesElement Data: ");
                for (MetricValue data : timeElement.data()) {
                    System.out.println("\t\t\t" + data.timeStamp()
                            + " : (Min) " + data.minimum()
                            + " : (Max) " + data.maximum()
                            + " : (Avg) " + data.average()
                            + " : (Total) " + data.total()
                            + " : (Count) " + data.count());
                }
            }
        }
	}
	
	
	public static void printMetricDefinition(MetricDefinition metricDefinition) {
        StringBuilder stMetricDefinition = new StringBuilder().append("\nMetric Definition ");
	    stMetricDefinition.append("\nId = ").append(metricDefinition.id());
	    stMetricDefinition.append("\nName = ").append(metricDefinition.name());
	    stMetricDefinition.append("\nResource Id = ").append(metricDefinition.resourceId());
	    stMetricDefinition.append("\nUnit Name = ").append(metricDefinition.unit().name());
	    
        System.out.println(stMetricDefinition.toString());
	}
	
    public static void printMetricMetadata(Metric metric) {
    	
        StringBuilder stMetricMetadata = new StringBuilder().append("\n\tMetric Metadata ");
	    stMetricMetadata.append("\n\tId = ").append(metric.id());
	    stMetricMetadata.append("\n\tName = ").append(metric.name());
	    stMetricMetadata.append("\n\tType = ").append(metric.type());
	    stMetricMetadata.append("\n\tUnit Name = ").append(metric.unit().name());
	    
        System.out.println(stMetricMetadata.toString());
    }
	
    public static void printMetrics(List<MetricValue> metricValues) {
    	
        StringBuilder stMetricValues = new StringBuilder().append("\n\tMetric Values ");
        int idx = 0;
        for (MetricValue metricValue : metricValues) {
        	stMetricValues.append("\n\tMetric Value - " + idx++);
        	stMetricValues.append("\n\t\tAverage = ").append(metricValue.average());
        	stMetricValues.append("\n\t\tCount = ").append(metricValue.count());
        	stMetricValues.append("\n\t\tTotal = ").append(metricValue.total());
        	stMetricValues.append("\n\t\tTimeStamp = ").append(metricValue.timeStamp().toString());
        }
        
        System.out.println(stMetricValues.toString());
    }
	
    public static void printVirtualMachine(VirtualMachine vm) {
    	System.out.println("VirtualMachine MetaData");
    	System.out.println("ComputeName: " + vm.computerName());
    	System.out.println("Name: " + vm.name());
    	System.out.println("Public IP Address ID: " + vm.getPrimaryPublicIPAddressId());
    	System.out.println("Type: " + vm.type());
    }
    
}
