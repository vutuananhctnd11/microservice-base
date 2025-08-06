package com.example.product_service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableResponse<T> {

    Integer page;
    Integer limit;
    Long totalPage;
    List<T> listResults;
}
