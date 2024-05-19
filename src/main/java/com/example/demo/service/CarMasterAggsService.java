package com.example.demo.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.example.demo.construct.FilterQueryEnum;
import com.example.demo.controller.dto.CarMaster;
import com.example.demo.indexer.IndexerHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CarMasterAggsService {

    private final IndexerHelper indexerHelper;

    public Map<String, Object> filter(CarMaster.Request request) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(request.getIndexName())
                .size(0)
                .query(q -> q
                        .bool(b -> b
                                .should(sh -> sh
                                        .nested(shn -> shn
                                                .path("area")
                                                .query(shnq -> shnq
                                                        .multiMatch(shnqm -> shnqm
                                                                .fields(List.of("area.country.standard",
                                                                        "area.country.english",
                                                                        "area.country.korean",
                                                                        "area.country.combine",
                                                                        "area.region.standard",
                                                                        "area.region.english",
                                                                        "area.region.korean",
                                                                        "area.region.combine",
                                                                        "area.state.standard",
                                                                        "area.state.english",
                                                                        "area.state.korean",
                                                                        "area.state.combine"))
                                                                .query(request.getKeyword())
                                                                .type(TextQueryType.CrossFields)
                                                                .operator(Operator.And)
                                                        )
                                                )
                                        )
                                )
                                .should(sh -> sh
                                        .multiMatch(shnqm -> shnqm
                                                .fields(List.of(
                                                        "brand.standard",
                                                        "model.standard",
                                                        "color.standard",
                                                        "brand.english",
                                                        "model.english",
                                                        "color.english",
                                                        "model.korean",
                                                        "color.korean",
                                                        "brand.combine",
                                                        "model.combine",
                                                        "color.combine"))
                                                .query(request.getKeyword())
                                                .type(TextQueryType.CrossFields)
                                                .operator(Operator.And)
                                        )
                                )
                                .minimumShouldMatch("1")
                                .filter(sf -> {
                                    BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
                                    for (FilterQueryEnum filterQueryEnum : FilterQueryEnum.values()) {
                                        filterQueryEnum.getQuery(boolQueryBuilder, request);
                                    }
                                    sf.bool(boolQueryBuilder.build());
                                    return sf;
                                })
                        )
                )
                .aggregations(getAggregation(request)));
        Map<String, Aggregate> response =  indexerHelper.searchAggregation(searchRequest);
        Map<String, Object> result = new HashMap<>();
        List<StringTermsBucket> model = response.get("model").sterms().buckets().array().stream().toList();
        result.put("model", model);
        return result;
    }

    private Map<String, Aggregation> getAggregation(CarMaster.Request request) {
        Map<String, Aggregation> result = new HashMap<>();
        result.put("brand", Aggregation.of(a -> a.terms(t -> t.field("brand").size(5))));
        result.put("model", Aggregation.of(a -> a.terms(t -> t.field("model").size(5))));
        result.put("priceMin", Aggregation.of(a -> a.min(t -> t.field("price"))));
        result.put("priceMax", Aggregation.of(a -> a.max(t -> t.field("price"))));
        result.put("color", Aggregation.of(a -> a.terms(t -> t.field("color").size(5))));

        return result;
    }
}
