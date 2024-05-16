package com.example.demo.service;

import java.io.IOException;
import java.util.Map;

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

}
