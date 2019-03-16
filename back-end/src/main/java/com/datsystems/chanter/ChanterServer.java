package com.datsystems.chanter;

import java.util.Scanner;

import org.apache.meecrowave.Meecrowave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChanterServer {
	private static final Logger logger = LoggerFactory.getLogger(ChanterServer.class.getName());
	
	public static void main(String... args) {
		try (Meecrowave meecrowave = new Meecrowave()) {
			meecrowave.bake();
			new Scanner(System.in).nextLine();
		} catch(Exception ex) {
			logger.error(ex.getMessage());
		}
	}
}
