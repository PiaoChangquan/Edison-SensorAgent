package org.csnpod.mgnt;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.csnpod.comm.CommControllerUnit;
import org.csnpod.comm.atcmd.ConfigurationAtCmd;
import org.csnpod.comm.data.CommConfig;
import org.csnpod.comm.socket.SocketConnFactory;
import org.csnpod.datastream.DataStreamControllerUnit;
import org.csnpod.datastream.data.DataStreamConfig;
import org.csnpod.mgnt.data.CsnPodConfig;
import org.csnpod.sensor.SensorControllerUnit;
import org.csnpod.sensor.data.SensorData;
import org.csnpod.sensor.data.SensorUnitConfig;
import org.csnpod.util.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.Uninterruptibles;

public class CSNPodManager {
	private static Logger logger = LoggerFactory.getLogger(CSNPodManager.class);
	private static final String CONF_PATH = "./EdisonPod_conf.json";

	private SensorControllerUnit sensorCtrlUnit;
	private DataStreamControllerUnit dsCtrlUnit;
	private CommControllerUnit commCtrlUnit;

	public static void StartMain() {
		logger.info("Start CSN Pod ...");

		CSNPodManager manager = new CSNPodManager();

		ConfigLoader.loadAllConfig(CONF_PATH);

		logger.info("Init CSN Pod ...");
		int ret = manager.initSystem();
		if (ret != 0) {
			logger.error("System Can't initialize");
			return;
		}

		logger.info("Run CSN Pod ...");
		manager.startSystem();

		Uninterruptibles.sleepUninterruptibly(180, TimeUnit.DAYS);
		logger.info("Stop CSN Pod ...");
		manager.stopUnits();

		logger.info("Closed CSN Pod System");
	}

	public int initSystem() {
		logger.info("Initialzie CSN Pod Units...");
		BlockingQueue<SensorData> queue = new ArrayBlockingQueue<SensorData>(
				DataStreamConfig.bufferSize);
		sensorCtrlUnit = new SensorControllerUnit(queue);
		
	    if (CommConfig.commType.toString().equals("CELLULAR")){
	    	commCtrlUnit = new CommControllerUnit();
	    	dsCtrlUnit = new DataStreamControllerUnit(queue, commCtrlUnit.getSockAtCmd());
	    	int cmdRet = 0;
	    	
	    	cmdRet = commCtrlUnit.getConfAtCmd().setResponseMode(0);
			if (cmdRet != 0)
				cmdRet = commCtrlUnit.getConfAtCmd().setResponseMode(0);

			logger.debug("Set Response Mode Return: {}", cmdRet);

			cmdRet = commCtrlUnit.getConfAtCmd().setEchoMode(0);
			if (cmdRet != 0)
				cmdRet = commCtrlUnit.getConfAtCmd().setEchoMode(0);

			logger.debug("Set Echo Mode Return: {}", cmdRet);

			cmdRet = commCtrlUnit.getConfAtCmd().setCMEEMode(1);
			if (cmdRet != 0)
				cmdRet = commCtrlUnit.getConfAtCmd().setCMEEMode(1);
			logger.debug("Set CMEE Error Mode Return: {}", cmdRet);
	    }else if (CommConfig.commType.toString().equals("ETHERNET")){
	    	dsCtrlUnit = new DataStreamControllerUnit(queue);
	    }
		logger.info("Modem Initialization");
		return 0;
	}

	public void startSystem() {
		logger.trace("Start startSystem method");
		// commCtrlUnit.start();

		dsCtrlUnit.start();

		sensorCtrlUnit.start();
		logger.trace("End startSystem method");
	}

	private void stopUnits() {
		logger.trace("Start stopUnits method");

		logger.trace("Waiting for the stop of Sensor Controller Unit thread");
		sensorCtrlUnit.abort();
		try {
			sensorCtrlUnit.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.trace("Finish to stop of Sensor Controller Unit thread");

		logger.trace("Waiting for the stop of Data Stream Controller Unit thread");
		dsCtrlUnit.abort();
		try {
			dsCtrlUnit.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.trace("Finish to stop of Sensor Data Stream Unit thread");
		if (CommConfig.commType.toString().equals("CELLULAR")){
			commCtrlUnit.closeIO();
		}
		
		
		// logger.trace("Waiting for the stop of Communication Controller Unit thread");
		// commCtrlUnit.abort();
		// commCtrlUnit.join();
		// logger.trace("Finish to stop of Communication Controller Unit thread");

		logger.trace("End stopUnits method");
	}
}
