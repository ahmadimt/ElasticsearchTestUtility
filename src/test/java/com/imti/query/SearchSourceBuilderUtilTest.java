package com.imti.query;


import com.imti.exception.InvalidDataException;
import com.imti.util.FileReader;
import java.io.IOException;
import java.net.URL;
import org.assertj.core.api.Assertions;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

/**
 * The type Search source builder util test.
 *
 * @author imteyaz.ahmad
 */
public class SearchSourceBuilderUtilTest {

  /**
   * Search source builder with valid data.
   *
   * @throws IOException the io exception
   */
  @Test
  public void searchSourceBuilderWithValidData() throws IOException {
    final URL resource = ClassLoader.getSystemClassLoader().getResource(
        "query/valid_json_query.json");
    assert resource != null;
    final SearchSourceBuilder searchSourceBuilder = SearchSourceBuilderUtil
        .searchSourceBuilder(resource.getFile());
    Assertions.assertThat(searchSourceBuilder).isNotNull();
  }

  /**
   * Search source builder with blank file.
   *
   * @throws IOException the io exception
   */
  @Test
  public void searchSourceBuilderWithBlankFile() throws IOException {
    final URL resource = ClassLoader.getSystemClassLoader().getResource(
        "query/empty_json_query.json");
    assert resource != null;
    final SearchSourceBuilder searchSourceBuilder = SearchSourceBuilderUtil
        .searchSourceBuilder(resource.getFile());
    Assertions.assertThat(searchSourceBuilder.toString()).isEqualTo("{}");
  }


  @Test
  public void searchSourceBuilderWithInvalidData() throws IOException {
    final URL resource = ClassLoader.getSystemClassLoader().getResource(
        "query/invalid_json_file.json");
    assert resource != null;

    Assertions.assertThatExceptionOfType(InvalidDataException.class)
        .isThrownBy(() -> SearchSourceBuilderUtil
            .searchSourceBuilder(resource.getFile()));
  }
}