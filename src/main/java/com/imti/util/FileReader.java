package com.imti.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author imteyaz.ahmad
 */
public class FileReader {

  private FileReader() {
    throw new UnsupportedOperationException("You are not permitted to do this.");
  }
  /**
   * Gets content of the file.
   *
   * @param jsonFile the json file
   * @return the content
   * @throws IOException the io exception
   */
  public static String getContent(final String jsonFile) throws IOException {
    return String.join("", Files.readAllLines(Paths.get(jsonFile)));
  }

}
