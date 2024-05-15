package com.example.demo.indexer;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import co.elastic.clients.util.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndexerHelper {
    private final ElasticsearchClientManager elasticsearchClientManager;
    private final ObjectMapper objectMapper;

    public Boolean createIndex(String indexName, Map<String, Object> indexTemplate) throws IOException {

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
            HttpEntity entity = new NStringEntity(requestBody, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
        }
        try {
            restClient.performRequest(request);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String jsonMapToString(Map<String, Object> jsonMap) {
        try {
            return objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



}
