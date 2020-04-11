package com.imti.response;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonFactory;
import com.imti.util.FileReader;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;

/**
 * @author imteyaz.ahmad
 */
public class MultiSearchResponseReader implements ResponseReader {

  /**
   * Gets multi search response from file.
   *
   * @param fullFileName the full file name
   * @return the multi search response from file
   * @throws IOException the io exception
   */
  public static MultiSearchResponse getMultiSearchResponseFromFile(String fullFileName)
      throws IOException {
    NamedXContentRegistry namedXContentRegistry = new NamedXContentRegistry(
        Stream.of(ResponseReader.getDefaultNamedXContents().stream(), ResponseReader
            .getProvidedNamedXContents().stream())
            .flatMap(Function.identity()).collect(toList()));
    final JsonXContentParser jsonXContentParser = new JsonXContentParser(namedXContentRegistry,
        new EmptyHandler()
        , new JsonFactory().createParser(FileReader.getContent(fullFileName)));
    return MultiSearchResponse.fromXContext(jsonXContentParser);
  }

}
