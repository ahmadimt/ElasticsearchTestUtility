package com.imti.util;

import java.io.IOException;
import java.net.URL;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author imteyaz.ahmad
 */
public class FileReaderTest {

  @Test
  public void getContentValidDataFile() throws IOException {

    final URL resource = ClassLoader.getSystemClassLoader().getResource(
        "query/valid_json_query.json");
    assert resource != null;
    final String content = FileReader.getContent(resource.getFile());
    Assertions.assertThat(content).isNotEmpty();
  }

  @Test
  public void getContentEmptyFile() throws IOException {
    final URL resource = ClassLoader.getSystemClassLoader().getResource(
        "query/empty_json_query.json");
    assert resource != null;
    final String content = FileReader.getContent(resource.getFile());
    Assertions.assertThat(content).isEmpty();
  }
}