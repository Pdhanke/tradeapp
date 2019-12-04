package com.prashant.strategy;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


import com.prashant.util.XMLWrite;
import com.zerodhatech.kiteconnect.utils.Constants;

public class StrategyManager {

	final static Logger logger = Logger.getLogger(StrategyManager.class);

	public static String DIRECTORYLOCATION = "C:\\Users\\Prashant\\eclipse-workspace\\project2\\resources\\";

	private HashMap<String, AbstractStrategy> strategyMap;

	private File strategyFileDir = new File(DIRECTORYLOCATION);
	

	public void execute() {

		while (true) {

			try {

				// 1. Read Files from Directory
				File newFiles[] = getnewStrategyFiles();
				int numberOfNewFiles = newFiles.length;
				logger.debug("Found new files :  " + numberOfNewFiles);

				// 2. Start a strategy for each new file
				if (numberOfNewFiles > 0) {
					startStrategyForNewFiles(newFiles);
				}

				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * Query pnl and store in pnlMap
	 */
	private void queryPnL() {

	}

	private File[] getnewStrategyFiles() {
		FileFilter fileFilter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.canWrite()) {
					return true;
				}
				return false;
			}
		};
		File strategyfiles[] = strategyFileDir.listFiles(fileFilter);

		return strategyfiles;
	}

	private void startStrategyForNewFiles(File strategyfiles[]) {

		for (int i = 0; i < strategyfiles.length; i++) {
			StrategyData strategyData = getStrategyDataFromFile(strategyfiles[i]);

			if (strategyData.getState() > 0) {
				logger.debug("Not running this Strategy as state is not 0 " + strategyData.getStrategyName());
				return;
			}
			if (strategyMap.containsKey(strategyData.getId())) {
				logger.debug("Not running this Strategy as already deployed with Id " + strategyData.getId());
				return;
			}
			AbstractStrategy strategy = getStrategyFromData(strategyData);

			Thread t = new Thread(strategy);
			t.start();

			strategyMap.put(strategyData.getId(), strategy);
		}

	}

	private StrategyData getStrategyDataFromFile(File strategyfile) {
		return XMLWrite.getStrategyDatefromXMLFile(strategyfile);

	}

	private AbstractStrategy getStrategyFromData(StrategyData strategyData) {

		String strategyName = strategyData.getStrategyName();
		AbstractStrategy strategy = getStrategyForName(strategyName);

		strategy.setStrategyData(strategyData);

		return strategy;

	}

	private AbstractStrategy getStrategyForName(String name) {

			return new FutureEntryAtPrice();
		
	}

	private void closeStrategy(AbstractStrategy strategy) {

		strategyMap.remove(strategy.getStrategyData().getId());

	}

	private void closeAllPositionsAndStrategies() {

	}

}
