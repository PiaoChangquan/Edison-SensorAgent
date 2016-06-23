package org.csnpod.sensor.sampling;

import org.csnpod.exception.SerialReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensorStream {
	private Logger logger = LoggerFactory.getLogger(SensorStream.class);


	private upm_grove.GroveTemp temp;
	private upm_grove.GroveLight light;
	private upm_ttp223.TTP223 touch;
	private upm_grove.GroveRotary rotary;
	private upm_grove.GroveButton button;
	public SensorStream(String SensorType, int port) {
		super();

		if (SensorType.equals("upm_grove.GroveLight")) {
			light = new upm_grove.GroveLight(port);
		} else if (SensorType.equals("upm_grove.GroveTemp")) {
			temp = new upm_grove.GroveTemp(port);
		} else if (SensorType.equals("upm_ttp223.TTP223")) {
			touch = new upm_ttp223.TTP223(port);
		} else if (SensorType.equals("upm_grove.GroveRotary")){
			rotary = new upm_grove.GroveRotary(port);
		}else if (SensorType.equals("upm_grove.GroveButton")){
			button = new upm_grove.GroveButton(port);
		}else{
			logger.error("Do not hava " + SensorType + " sensor");
		}
		
	}

	public String readLine(String SensorType, int period)
			throws SerialReadException {
		logger.trace("Start readLine Method");

		String rawdata = null;
		if (SensorType.equals("upm_grove.GroveTemp")) {

			rawdata = "temp: " + temp.value();
		} else if (SensorType.equals("upm_grove.GroveLight")) {

			rawdata = "light: " + light.value();
		} else if (SensorType.equals("upm_ttp223.TTP223")) {
			if (touch.isPressed())
				rawdata = "touch: " + 1;
			else
				rawdata = "touch: " + 0;
		}else if (SensorType.equals("upm_grove.GroveRotary")){
			rawdata = "Rotary: " + rotary.abs_deg(); // Absolute degrees
		}else if (SensorType.equals("upm_grove.GroveButton")){
			rawdata = "Button: "+button.value();
		} else {
			logger.error("Do not hava " + SensorType + " sensor");
		}

		return rawdata;
	}

	public void closeSensorStream() {
		logger.trace("Start closeSensorStream Method");
		// serial.close();
		logger.trace("End closeSensorStream Method");
	}
}
