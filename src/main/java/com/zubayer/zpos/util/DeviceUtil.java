package com.zubayer.zpos.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Zubayer Ahamed
 * @since May 29, 2024
 */
public class DeviceUtil {

	public static String getDeviceId() {
		try {
			// Execute the command
			@SuppressWarnings("deprecation")
			Process process = Runtime.getRuntime().exec("wmic diskdrive get serialnumber");

			// Get the input stream of the process and read from it
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			String serialNumber = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() > 0 && !line.contains("SerialNumber")) {
					serialNumber = line;
					break;
				}
			}

			// Close the reader
			reader.close();
			process.destroy();

			if (serialNumber != null) {
				return serialNumber;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
