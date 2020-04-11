package com.imti.response;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonFactory;
import com.imti.util.FileReader;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;

/**
 * @author imteyaz.ahmad
 */
public class SearchResponseReader implements ResponseReader {

  private SearchResponseReader() {
    throw new UnsupportedOperationException("You are not permitted to do this.");
  }

  /**
   * Constructs search response from content json file expected to appear from ElasticSearch.
   *
   * @param fullFileName the full file name
   * @return the search response
   * @throws IOException the io exception
   */
  public static SearchResponse getSearchResponseFromFile(String fullFileName) throws IOException {
    return getSearchResponse(FileReader.getContent(fullFileName));
  }

  /**
   * Gets search response.
   *
   * @param content the content
   * @return the search response
   * @throws IOException the io exception
   */
  public static SearchResponse getSearchResponse(String content) throws IOException {
    NamedXContentRegistry namedXContentRegistry = new NamedXContentRegistry(
        Stream.of(ResponseReader.getDefaultNamedXContents().stream(), ResponseReader
            .getProvidedNamedXContents().stream())
            .flatMap(Function.identity()).collect(toList()));
    final JsonXContentParser jsonXContentParser = new JsonXContentParser(namedXContentRegistry,
        new EmptyHandler()
        , new JsonFactory().createParser(content));
    return SearchResponse.fromXContent(jsonXContentParser);
  }
}
