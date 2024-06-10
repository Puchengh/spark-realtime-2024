package com.puchen.publishrealtime.controller;

import com.puchen.publishrealtime.bean.NameValue;
import com.puchen.publishrealtime.service.PulisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PulisherController {

    @Autowired
    PulisherService pulisherService;

    //日活分析统计
    @GetMapping("dauRealtime")
    public Map<String, Object> dauRealtime(@RequestParam("td") String td) {
        Map<String, Object> result = pulisherService.doDauRealtime(td);
        return result;
    }

    /**
     * 交易分析 按照类别，年龄统计
     * http://bigdata.gmall.com/statsByItem?itemName=小米手机&date=2021-02-02&t=age
     * http://bigdata.gmall.com/statsByItem?itemName=小米手机&date=2021-02-02&t=gender
     */
    @GetMapping("statsByItem")
    public List<NameValue> statsByItem(@RequestParam("itemName") String itemName,
                                       @RequestParam("date") String date,
                                       @RequestParam("t") String t
    ) {

        return pulisherService.doStatsByItem(itemName, date, t);
    }

    /**
     * http://bigdata.gmall.com/detailByItem?date=2021-02-02&itemName=小米手机&pageNo=1&pageSize=20
     */

    @GetMapping("detailByItem")
    public Map<String, Object> detailByItem(@RequestParam("date") String date,
                                            @RequestParam("itemName") String itemName,
                                            @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {

        return pulisherService.doDetailByItem(date, itemName, pageNo, pageSize);
    }
}
