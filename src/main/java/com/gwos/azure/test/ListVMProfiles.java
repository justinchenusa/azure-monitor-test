package com.gwos.azure.test;

import java.io.File;

import com.gwos.azure.utils.Utils;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.compute.VirtualMachine;
import com.microsoft.rest.LogLevel;

public class ListVMProfiles {
	public static boolean runSample(Azure azure) {

		try {
			// =============================================================
			// List virtual machines in the resource group
			String resourceGroupName = "GWOSGROUP"; // My Ubuntu VM is created under this group
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
	
	public static void main(String[] args) {
		try {
			// =============================================================
			// Authenticate by a generated azure auth file configured in env. variable
			final File credFile = new File(System.getenv("AZURE_AUTH_LOCATION"));

			Azure azure = Azure.configure().withLogLevel(LogLevel.BASIC).authenticate(credFile).withDefaultSubscription();

			// Print selected subscription
			System.out.println("Selected subscription: " + azure.subscriptionId());
			runSample(azure);
						
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
