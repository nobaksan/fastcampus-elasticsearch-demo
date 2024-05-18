package com.example.demo.construct;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.example.demo.controller.dto.CarMaster;
import lombok.Getter;

@Getter
public enum FilterQueryEnum {
    PRICE("price", "price 가 0 이 아닌 것") {
        @Override
        public void getQuery(BoolQuery.Builder musts, CarMaster.Request request) {
            musts.mustNot(f -> f.term(t -> t.field(getFieldName()).value(0)));
        }
    };

    private final String fieldName;
    private final String description;

    public abstract void getQuery(BoolQuery.Builder musts, CarMaster.Request request);

    FilterQueryEnum(String fieldName, String description) {
        this.fieldName = fieldName;
        this.description = description;
    }
}
