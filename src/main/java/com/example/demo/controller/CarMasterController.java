package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.demo.controller.dto.CarMaster;
import com.example.demo.service.CarMasterAggsService;
import com.example.demo.service.CarMasterCountService;
import com.example.demo.service.CarMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CarMasterController {

    private final CarMasterService carMasterService;
    private final CarMasterCountService carMasterCountService;
    private final CarMasterAggsService carMasterAggsService;

    @GetMapping("/create-index")
    public void create(@RequestParam String indexName) throws IOException {
        carMasterService.createIndex(indexName);
    }

    @GetMapping("/delete-index")
    public void delete(@RequestParam String indexName) throws IOException {
        carMasterService.deleteIndex(indexName);
    }

    @GetMapping("/count-index")
    public Long count(@RequestParam String indexName) throws IOException {
        return carMasterService.countIndex(indexName);
    }

    @GetMapping("/search")
    public List<CarMaster.Response> search(@ModelAttribute CarMaster.Request request) throws IOException {
        return carMasterService.search(request);
    }

    @GetMapping("/search/count")
    public Long searchCount(@ModelAttribute CarMaster.Request request) throws IOException {
        return carMasterCountService.count(request);
    }

    @GetMapping("/search/filter")
    public @ResponseBody Map<String, Object> searchFilter(@ModelAttribute CarMaster.Request request) throws IOException {
        return carMasterAggsService.filter(request);
    }

    @GetMapping("/completion")
    public List<CarMaster.CompletionResponse> completion(@ModelAttribute CarMaster.CompletionRequest request) throws IOException {
        return carMasterService.completion(request);
    }

}
