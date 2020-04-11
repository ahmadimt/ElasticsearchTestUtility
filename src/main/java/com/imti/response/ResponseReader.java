package com.imti.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.ContextParser;
import org.elasticsearch.common.xcontent.DeprecationHandler;
import org.elasticsearch.common.xcontent.NamedXContentRegistry.Entry;
import org.elasticsearch.plugins.spi.NamedXContentProvider;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.adjacency.AdjacencyMatrixAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.adjacency.ParsedAdjacencyMatrix;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.ParsedComposite;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilters;
import org.elasticsearch.search.aggregations.bucket.geogrid.GeoGridAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.geogrid.ParsedGeoHashGrid;
import org.elasticsearch.search.aggregations.bucket.global.GlobalAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.global.ParsedGlobal;
import org.elasticsearch.search.aggregations.bucket.histogram.AutoDateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedAutoDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedHistogram;
import org.elasticsearch.search.aggregations.bucket.missing.MissingAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.missing.ParsedMissing;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedReverseNested;
import org.elasticsearch.search.aggregations.bucket.nested.ReverseNestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.GeoDistanceAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.IpRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.ParsedBinaryRange;
import org.elasticsearch.search.aggregations.bucket.range.ParsedDateRange;
import org.elasticsearch.search.aggregations.bucket.range.ParsedGeoDistance;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.sampler.InternalSampler;
import org.elasticsearch.search.aggregations.bucket.sampler.ParsedSampler;
import org.elasticsearch.search.aggregations.bucket.significant.ParsedSignificantLongTerms;
import org.elasticsearch.search.aggregations.bucket.significant.ParsedSignificantStringTerms;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantLongTerms;
import org.elasticsearch.search.aggregations.bucket.significant.SignificantStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.DoubleTerms;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedDoubleTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.ParsedCardinality;
import org.elasticsearch.search.aggregations.metrics.geobounds.GeoBoundsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.geobounds.ParsedGeoBounds;
import org.elasticsearch.search.aggregations.metrics.geocentroid.GeoCentroidAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.geocentroid.ParsedGeoCentroid;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.percentiles.hdr.InternalHDRPercentileRanks;
import org.elasticsearch.search.aggregations.metrics.percentiles.hdr.InternalHDRPercentiles;
import org.elasticsearch.search.aggregations.metrics.percentiles.hdr.ParsedHDRPercentileRanks;
import org.elasticsearch.search.aggregations.metrics.percentiles.hdr.ParsedHDRPercentiles;
import org.elasticsearch.search.aggregations.metrics.percentiles.tdigest.InternalTDigestPercentileRanks;
import org.elasticsearch.search.aggregations.metrics.percentiles.tdigest.InternalTDigestPercentiles;
import org.elasticsearch.search.aggregations.metrics.percentiles.tdigest.ParsedTDigestPercentileRanks;
import org.elasticsearch.search.aggregations.metrics.percentiles.tdigest.ParsedTDigestPercentiles;
import org.elasticsearch.search.aggregations.metrics.scripted.ParsedScriptedMetric;
import org.elasticsearch.search.aggregations.metrics.scripted.ScriptedMetricAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.ParsedStats;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ParsedExtendedStats;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.ParsedTopHits;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ParsedValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.InternalSimpleValue;
import org.elasticsearch.search.aggregations.pipeline.ParsedSimpleValue;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.InternalBucketMetricValue;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.ParsedBucketMetricValue;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.percentile.ParsedPercentilesBucket;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.percentile.PercentilesBucketPipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.stats.ParsedStatsBucket;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.stats.StatsBucketPipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.stats.extended.ExtendedStatsBucketPipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.stats.extended.ParsedExtendedStatsBucket;
import org.elasticsearch.search.aggregations.pipeline.derivative.DerivativePipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.derivative.ParsedDerivative;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestion;
import org.elasticsearch.search.suggest.term.TermSuggestion;

/**
 * @author imteyaz.ahmad
 */
public interface ResponseReader {

  /**
   * Due to private and final methods within ElasticSearch code below is made by duplicating
   * mappings into our code.
   *
   * @return the default named x contents.
   */
  static List<Entry> getDefaultNamedXContents() {
    Map<String, ContextParser<Object, ? extends Aggregation>> map = new HashMap<>();
    map.put(CardinalityAggregationBuilder.NAME,
        (p, c) -> ParsedCardinality.fromXContent(p, (String) c));
    map.put(InternalHDRPercentiles.NAME,
        (p, c) -> ParsedHDRPercentiles.fromXContent(p, (String) c));
    map.put(
        InternalHDRPercentileRanks.NAME,
        (p, c) -> ParsedHDRPercentileRanks.fromXContent(p, (String) c));
    map.put(
        InternalTDigestPercentiles.NAME,
        (p, c) -> ParsedTDigestPercentiles.fromXContent(p, (String) c));
    map.put(InternalTDigestPercentileRanks.NAME, (p, c) -> ParsedTDigestPercentileRanks
        .fromXContent(p, (String) c));
    map.put(PercentilesBucketPipelineAggregationBuilder.NAME, (p, c) -> ParsedPercentilesBucket
        .fromXContent(p, (String) c));
    map.put(MinAggregationBuilder.NAME, (p, c) -> ParsedMin.fromXContent(p, (String) c));
    map.put(MaxAggregationBuilder.NAME, (p, c) -> ParsedMax.fromXContent(p, (String) c));
    map.put(SumAggregationBuilder.NAME, (p, c) -> ParsedSum.fromXContent(p, (String) c));
    map.put(AvgAggregationBuilder.NAME, (p, c) -> ParsedAvg.fromXContent(p, (String) c));
    map.put(ValueCountAggregationBuilder.NAME,
        (p, c) -> ParsedValueCount.fromXContent(p, (String) c));
    map.put(InternalSimpleValue.NAME, (p, c) -> ParsedSimpleValue.fromXContent(p, (String) c));
    map.put(
        DerivativePipelineAggregationBuilder.NAME,
        (p, c) -> ParsedDerivative.fromXContent(p, (String) c));
    map.put(InternalBucketMetricValue.NAME,
        (p, c) -> ParsedBucketMetricValue.fromXContent(p, (String) c));
    map.put(StatsAggregationBuilder.NAME, (p, c) -> ParsedStats.fromXContent(p, (String) c));
    map.put(StatsBucketPipelineAggregationBuilder.NAME, (p, c) -> ParsedStatsBucket
        .fromXContent(p, (String) c));
    map.put(
        ExtendedStatsAggregationBuilder.NAME,
        (p, c) -> ParsedExtendedStats.fromXContent(p, (String) c));
    map.put(ExtendedStatsBucketPipelineAggregationBuilder.NAME,
        (p, c) -> ParsedExtendedStatsBucket.fromXContent(p, (String) c));
    map.put(GeoBoundsAggregationBuilder.NAME,
        (p, c) -> ParsedGeoBounds.fromXContent(p, (String) c));
    map.put(GeoCentroidAggregationBuilder.NAME,
        (p, c) -> ParsedGeoCentroid.fromXContent(p, (String) c));
    map.put(HistogramAggregationBuilder.NAME,
        (p, c) -> ParsedHistogram.fromXContent(p, (String) c));
    map.put(
        DateHistogramAggregationBuilder.NAME,
        (p, c) -> ParsedDateHistogram.fromXContent(p, (String) c));
    map.put(AutoDateHistogramAggregationBuilder.NAME, (p, c) -> ParsedAutoDateHistogram
        .fromXContent(p, (String) c));
    map.put(StringTerms.NAME, (p, c) -> ParsedStringTerms.fromXContent(p, (String) c));
    map.put(LongTerms.NAME, (p, c) -> ParsedLongTerms.fromXContent(p, (String) c));
    map.put(DoubleTerms.NAME, (p, c) -> ParsedDoubleTerms.fromXContent(p, (String) c));
    map.put(MissingAggregationBuilder.NAME, (p, c) -> ParsedMissing.fromXContent(p, (String) c));
    map.put(NestedAggregationBuilder.NAME, (p, c) -> ParsedNested.fromXContent(p, (String) c));
    map.put(
        ReverseNestedAggregationBuilder.NAME,
        (p, c) -> ParsedReverseNested.fromXContent(p, (String) c));
    map.put(GlobalAggregationBuilder.NAME, (p, c) -> ParsedGlobal.fromXContent(p, (String) c));
    map.put(FilterAggregationBuilder.NAME, (p, c) -> ParsedFilter.fromXContent(p, (String) c));
    map.put(InternalSampler.PARSER_NAME, (p, c) -> ParsedSampler.fromXContent(p, (String) c));
    map.put(GeoGridAggregationBuilder.NAME,
        (p, c) -> ParsedGeoHashGrid.fromXContent(p, (String) c));
    map.put(RangeAggregationBuilder.NAME, (p, c) -> ParsedRange.fromXContent(p, (String) c));
    map.put(DateRangeAggregationBuilder.NAME,
        (p, c) -> ParsedDateRange.fromXContent(p, (String) c));
    map.put(GeoDistanceAggregationBuilder.NAME,
        (p, c) -> ParsedGeoDistance.fromXContent(p, (String) c));
    map.put(FiltersAggregationBuilder.NAME, (p, c) -> ParsedFilters.fromXContent(p, (String) c));
    map.put(AdjacencyMatrixAggregationBuilder.NAME, (p, c) -> ParsedAdjacencyMatrix
        .fromXContent(p, (String) c));
    map.put(SignificantLongTerms.NAME,
        (p, c) -> ParsedSignificantLongTerms.fromXContent(p, (String) c));
    map.put(
        SignificantStringTerms.NAME,
        (p, c) -> ParsedSignificantStringTerms.fromXContent(p, (String) c));
    map.put(
        ScriptedMetricAggregationBuilder.NAME,
        (p, c) -> ParsedScriptedMetric.fromXContent(p, (String) c));
    map.put(IpRangeAggregationBuilder.NAME,
        (p, c) -> ParsedBinaryRange.fromXContent(p, (String) c));
    map.put(TopHitsAggregationBuilder.NAME, (p, c) -> ParsedTopHits.fromXContent(p, (String) c));
    map.put(CompositeAggregationBuilder.NAME,
        (p, c) -> ParsedComposite.fromXContent(p, (String) c));
    List<Entry> entries = map.entrySet().stream()
        .map(entry -> new Entry(Aggregation.class,
            new ParseField(entry.getKey()), entry.getValue()))
        .collect(Collectors.toList());
    entries.add(new Entry(
        Suggest.Suggestion.class, new ParseField(TermSuggestion.NAME),
        (parser, context) -> TermSuggestion.fromXContent(parser, (String) context)));
    entries.add(new Entry(Suggest.Suggestion.class, new ParseField(
        PhraseSuggestion.NAME),
        (parser, context) -> PhraseSuggestion.fromXContent(parser, (String) context)));
    entries.add(new Entry(Suggest.Suggestion.class, new ParseField(
        CompletionSuggestion.NAME),
        (parser, context) -> CompletionSuggestion.fromXContent(parser, (String) context)));
    return entries;
  }

  /**
   * Loads and returns the {@link Entry} parsers provided by plugins.
   */
  static List<Entry> getProvidedNamedXContents() {
    List<Entry> entries = new ArrayList<>();
    for (NamedXContentProvider service : ServiceLoader.load(NamedXContentProvider.class)) {
      entries.addAll(service.getNamedXContentParsers());
    }
    return entries;
  }

  /**
   * The type Empty handler.
   */
  @Log4j2
  class EmptyHandler implements DeprecationHandler {

    @Override
    public void usedDeprecatedName(final String usedName, final String modernName) {
      log.info("UsedName {} and ModernName {}", usedName, modernName);
    }

    @Override
    public void usedDeprecatedField(final String usedName, final String replacedWith) {
      log.info("UsedName {} and replacedWith {}", usedName, replacedWith);
    }

    @Override
    public void deprecated(final String message, final Object... params) {
      log.info("message {} and params {}", message, params);
    }
  }
}
