package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.example.demo.controller.dto.CarMaster;
import com.example.demo.util.FileUtil;
import com.example.demo.indexer.IndexerHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarMasterService {

    private final IndexerHelper indexerHelper;

    public void createIndex(String indexName) throws IOException {
        Map<String, Object> indexTemplate = new FileUtil().getFileContent("/index/car-master.json");
        indexerHelper.createIndex(indexName, indexTemplate);
    }


    public void deleteIndex(String indexName) {
        indexerHelper.deleteIndex(indexName);
    }

    public Long countIndex(String indexName) {
        return indexerHelper.countIndex(indexName);
    }

    public List<CarMaster.Response> search(CarMaster.Request request) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(request.getIndexName())
                .query(q -> q.matchAll(m -> m))
                .size(100)
        );

        return indexerHelper.search(searchRequest);
    }
}
