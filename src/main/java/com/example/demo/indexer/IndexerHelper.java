package com.example.demo.indexer;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import com.example.demo.controller.dto.CarMaster;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndexerHelper {
    private final ElasticsearchClientManager elasticsearchClientManager;
    private final ObjectMapper objectMapper;

    public void createIndex(String indexName, Map<String, Object> indexTemplate) throws IOException {
        /*elasticsearchClientManager.getElasticsearchClient("indexer")
                .indices()
                .create(c -> c
                        .index("products")
                );
         */

        RestClient restClient = elasticsearchClientManager.getRestClient("indexer");
        Request request = new Request(HttpPut.METHOD_NAME, "/" + indexName);
        if (ObjectUtils.isNotEmpty(indexTemplate)) {
            String requestBody = jsonMapToString(indexTemplate);
            HttpEntity entity = new NStringEntity(requestBody, ContentType.create("application/json", StandardCharsets.UTF_8));
            request.setEntity(entity);
        }
        try {
            Response response = restClient.performRequest(request);
            System.out.printf(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String jsonMapToString(Map<String, Object> jsonMap) {
        try {
            return objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteIndex(String indexName) {
        DeleteIndexRequest deleteIndexRequest = DeleteIndexRequest.of(d -> d.index(indexName));
        try {
            elasticsearchClientManager.getElasticsearchClient("indexer")
                    .indices()
                    .delete(deleteIndexRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Long countIndex(String indexName) {
        CountRequest countRequest = CountRequest.of(c -> c.index(indexName));
        try {
            return elasticsearchClientManager.getElasticsearchClient("indexer")
                    .count(countRequest)
                    .count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CarMaster.Response> search(SearchRequest searchRequest) throws IOException {
        return elasticsearchClientManager.getElasticsearchClient("indexer")
                .search(searchRequest, CarMaster.Response.class)
                .hits().hits().stream().map(Hit::source)
                .toList();
    }
}
