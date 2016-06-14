package org.csnpod.sensor.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.MoreObjects;

public class PhysicalSensorInformation {
	private String name;
	private String type;
	private String SensorType;
	private int port;
	private List<LogicalSensorMetadata> sensors;
	private String parsingRegex;
	private Map<String, Object> targetInfo;
	private int period;

	public PhysicalSensorInformation(String name, String type,
			String SensorType,int port,
			List<LogicalSensorMetadata> sensors, String parsingRegex,
			Map<String, Object> targetInfo, int period) {
		super();
		this.name = name;
		this.type = type;
		this.SensorType = SensorType;
		this.port = port;
		this.sensors = sensors;
		this.parsingRegex = parsingRegex;
		this.targetInfo = targetInfo;
		this.period = period;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSensorType() {
		return SensorType;
	}

	public void setSensorType(String sensorType) {
		SensorType = sensorType;
	}


	public List<LogicalSensorMetadata> getSensors() {
		return sensors;
	}

	public void setSensors(List<LogicalSensorMetadata> sensors) {
		this.sensors = sensors;
	}

	public String getParsingRegex() {
		return parsingRegex;
	}

	public void setParsingRegex(String parsingRegex) {
		this.parsingRegex = parsingRegex;
	}

	public Map<String, Object> getTargetInfo() {
		return targetInfo;
	}

	public void setTargetInfo(Map<String, Object> targetInfo) {
		this.targetInfo = targetInfo;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("Name", name).add("SensorType", SensorType).add("type", type)
				.add("port", port).add("Sensors", sensors)
				.add("Parsing Regular Expression", parsingRegex)
				.add("Parsing Target Information", targetInfo)
				.add("Sampling Period", period)
				.toString();
	}
}
