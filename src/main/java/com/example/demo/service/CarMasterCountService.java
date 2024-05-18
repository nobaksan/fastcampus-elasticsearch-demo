package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.CountRequest;
import com.example.demo.construct.FilterQueryEnum;
import com.example.demo.controller.dto.CarMaster;
import com.example.demo.indexer.IndexerHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarMasterCountService {

    private final IndexerHelper indexerHelper;

    public Long count(CarMaster.Request request) throws IOException {

        CountRequest countRequest = CountRequest.of(s -> s
                .index(request.getIndexName())
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

        );

        return indexerHelper.count(countRequest);
    }
}
