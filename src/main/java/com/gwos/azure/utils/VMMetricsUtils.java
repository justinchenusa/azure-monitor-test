package com.gwos.azure.utils;

import java.util.List;

import com.microsoft.azure.management.monitor.Metric;
import com.microsoft.azure.management.monitor.MetricDefinition;
import com.microsoft.azure.management.monitor.MetricValue;

public class VMMetricsUtils {

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
	
}
