package com.gwos.azure.test;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.appservice.SiteAvailabilityState;
import com.microsoft.azure.management.appservice.WebApp;
import com.microsoft.azure.management.compute.Disk;
import com.microsoft.azure.management.compute.VirtualMachine;
import com.microsoft.azure.management.cosmosdb.CosmosDBAccount;
import com.microsoft.azure.management.monitor.*;
import com.microsoft.azure.management.sql.ServerMetric;
import com.microsoft.azure.management.sql.SqlDatabase;
import com.microsoft.azure.management.sql.SqlServer;
import com.microsoft.azure.management.sql.SqlServer.Databases;
import com.microsoft.rest.LogLevel;
import org.joda.time.DateTime;

import java.io.File;
import java.util.List;

public class HealthTest {

    public static void runSample(Azure azure) {

        MetricDefinitions definitions = azure.metricDefinitions();

        PagedList<CosmosDBAccount> mongos = azure.cosmosDBAccounts().list();
        for (CosmosDBAccount mongo : mongos) {
            System.out.println(mongo.name());
            if (mongo.readableReplications().size() > 0) {
            	String state = mongo.readableReplications().get(0).provisioningState();
            }
            listMetrics(definitions, mongo.id());
            listEvents(azure, mongo.id());
        }

        PagedList<VirtualMachine> vms = azure.virtualMachines().list();
        for (VirtualMachine vm: vms) {
            System.out.println("-- vm = " + vm.computerName());
            System.out.println("state: " + vm.powerState());
            System.out.println("prov state: " + vm.provisioningState());
            listMetrics(definitions, vm.id());
            listEvents(azure, vm.id());
        }

        PagedList<WebApp> webapps = azure.webApps().list();
        for (WebApp webApp : webapps) {
            System.out.println(webApp.name());
            String webAppState = webApp.state();
            SiteAvailabilityState state = webApp.availabilityState();
            listMetrics(definitions, webApp.id());
        }

        PagedList<Disk> disks = azure.disks().list();
        for (Disk disk : disks) {
            System.out.println(disk.name());
            listMetrics(definitions, disk.id());
        }

        PagedList<SqlServer> sqlServers = azure.sqlServers().list();
        
        for (SqlServer sqlServer : sqlServers) {
            System.out.println(sqlServer.name());
            //sqlServer.databases().get(sqlServer.name()).status();
            List<ServerMetric> sqlServerMetric = sqlServer.listUsages();
            //List<SqlDatabase> sqlDbs = sqlServer.databases().list();
            for (SqlDatabase sqlDatabase : sqlServer.databases().list()) {
            	//String status = sqlServer.databases().list().get(i).status();
            	//String status = sqlDatabase.status();
            	System.out.println(sqlDatabase.name() + " - Status = " + sqlDatabase.status());
            	listMetrics(definitions, sqlDatabase.id());
            }
            //listMetrics(definitions, sqlServer.id());
        }

    }



    public static void main(String[] args) {
        try {

			// Authenticate by a generated azure auth file configured in env. variable
			final File credFile = new File(System.getenv("AZURE_AUTH_LOCATION"));

			Azure azure = Azure.configure().withLogLevel(LogLevel.NONE).authenticate(credFile).withDefaultSubscription();
            // Print selected subscription
            System.out.println("Selected subscription: " + azure.subscriptionId());

            runSample(azure);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listEvents(Azure azure, String id) {
        DateTime recordDateTime = DateTime.now();

        PagedList<EventData> logs = azure.activityLogs().defineQuery()
                .startingFrom(recordDateTime.minusDays(7))
                .endsBefore(recordDateTime)
                .withAllPropertiesInResponse()
                .filterByResource(id)
                .execute();

        System.out.println("Activity logs for the Storage Account:");

        for (EventData event : logs) {
            if (event.eventName() != null) {
                System.out.println("\tEvent: " + event.eventName().localizedValue());
            }
            if (event.operationName() != null) {
                System.out.println("\tOperation: " + event.operationName().localizedValue());
            }
            System.out.println("\tCaller: " + event.caller());
            System.out.println("\tCorrelationId: " + event.correlationId());
            System.out.println("\tSubscriptionId: " + event.subscriptionId());
        }


    }

    private static void listMetrics(MetricDefinitions definitions, String id) {
        DateTime recordDateTime = DateTime.now();
        List<MetricDefinition> md = definitions.listByResource(id);
        if (md != null) {
            for (MetricDefinition definition : md) {
                System.out.printf("metric: %s %s\n", definition.name().value(), definition.resourceId() );
                MetricCollection metrics = definition.defineQuery()
                        .startingFrom(recordDateTime.minusMinutes(5))
                        .endsBefore(recordDateTime)
                        .withResultType(ResultType.DATA)
                        .execute();
                if (metrics.inner() != null) {
                    List<Metric> ms = metrics.metrics();
                    for (Metric m : ms) {
                        //switch (m.unit())
                        System.out.println("metric = " + m.name() + ", " + m.timeseries().get(0).data().get(0).timeStamp());
                    }
                }
            }
        }
    }

}
