package com.puchen.publishrealtime.mapper;

import com.puchen.publishrealtime.bean.NameValue;

import java.util.List;
import java.util.Map;

public interface PulisherMapper {
    Map<String, Object> searchDau(String td);

    List<NameValue> searchStatsByItem(String itemName, String date, String field);

    Map<String, Object> searchDetailByItem(String date, String itemName, Integer from, Integer pageSize);
}
