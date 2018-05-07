package com.datsystems.chanter;

import java.util.Scanner;

import org.apache.meecrowave.Meecrowave;

public class ChanterServer {
  public static void main(String... args) throws Exception {
    try (final Meecrowave meecrowave = new Meecrowave().bake()) {
      new Scanner(System.in).nextLine();
    } finally {

    }
  }
}
