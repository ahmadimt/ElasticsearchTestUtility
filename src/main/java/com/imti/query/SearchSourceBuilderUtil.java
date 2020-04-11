package com.imti.query;

import com.imti.exception.InvalidDataException;
import com.imti.util.FileReader;
import java.io.IOException;
import java.util.Collections;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.DeprecationHandler;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * The type Search source builder util.
 *
 * @author imteyaz.ahmad
 */
@UtilityClass
public class SearchSourceBuilderUtil {

  /**
   * Search source builder search source builder.
   *
   * @param fullFileName the full file name
   * @return the search source builder
   * @throws IOException the io exception
   */
  public static SearchSourceBuilder searchSourceBuilder(String fullFileName) throws IOException {
    final String content = FileReader.getContent(fullFileName);
    return getSearchSourceBuilder(content);
  }


  /**
   * Gets search source builder.
   *
   * @param content the content
   * @return the search source builder
   */
  public static SearchSourceBuilder getSearchSourceBuilder(final String content) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    if (StringUtils.isBlank(content)) {
      return searchSourceBuilder;
    }
    SearchModule searchModule = new SearchModule(Settings.EMPTY, false, Collections.emptyList());
    try (XContentParser parser = XContentFactory.xContent(XContentType.JSON)
        .createParser(new NamedXContentRegistry(searchModule.getNamedXContents()),
            DeprecationHandler.THROW_UNSUPPORTED_OPERATION, content)) {
      searchSourceBuilder.parseXContent(parser);
    } catch (IOException ioe) {
      throw new InvalidDataException(ioe);
    }
    return searchSourceBuilder;
  }


}
