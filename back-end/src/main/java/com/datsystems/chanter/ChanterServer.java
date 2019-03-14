package com.datsystems.chanter;

import java.util.Scanner;

import org.apache.meecrowave.Meecrowave;

public class ChanterServer {
	public static void main(String... args) throws Exception {
		final Meecrowave meecrowave = new Meecrowave();
		try {
			meecrowave.bake();
			new Scanner(System.in).nextLine();
		} finally {
			meecrowave.close();
		}
	}
}
