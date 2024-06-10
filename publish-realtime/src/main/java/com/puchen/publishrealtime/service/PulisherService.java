package com.puchen.publishrealtime.service;

import com.puchen.publishrealtime.bean.NameValue;

import java.util.List;
import java.util.Map;

public interface PulisherService {
    Map<String, Object> doDauRealtime(String td);

    List<NameValue> doStatsByItem(String itemName, String date, String t);

    Map<String, Object> doDetailByItem(String date, String itemName, Integer from, Integer pageSize);
}
