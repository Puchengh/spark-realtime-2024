package com.puchen.publishrealtime.service.impl;

import com.puchen.publishrealtime.bean.NameValue;
import com.puchen.publishrealtime.mapper.PulisherMapper;
import com.puchen.publishrealtime.service.PulisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PulisherServiceImpl implements PulisherService {

    @Autowired
    PulisherMapper pulisherMapper;

    /**
     * 日活分析的统计
     *
     * @param td
     * @return
     */
    @Override
    public Map<String, Object> doDauRealtime(String td) {
        //业务处理
        Map<String, Object> result = pulisherMapper.searchDau(td);
        return result;
    }

    /**
     * 交易分析的业务处理
     *
     * @param itemName
     * @param date
     * @param t
     * @return
     */
    @Override
    public List<NameValue> doStatsByItem(String itemName, String date, String t) {
        return transformResult(pulisherMapper.searchStatsByItem(itemName, date, typeToField(t)), t);
    }

    @Override
    public Map<String, Object> doDetailByItem(String date, String itemName, Integer pageNo, Integer pageSize) {
        //计算分页开始位置
        int from = (pageNo - 1) * pageSize;
        return pulisherMapper.searchDetailByItem(date, itemName, from, pageSize);
    }

    public List<NameValue> transformResult(List<NameValue> searchResult, String t) {
        if ("age".equals(t)) {

            double totalAmount20 = 0;
            double totalAmount20to29 = 0;
            double totalAmount30 = 0;
            if (searchResult.size() > 0) {
                for (NameValue nameValue : searchResult) {
                    Integer age = Integer.parseInt(nameValue.getName());
                    Double value = Double.parseDouble(nameValue.getValue().toString());
                    if (age < 20) {
                        totalAmount20 += value;
                    } else if (age <= 29) {
                        totalAmount20to29 += value;
                    } else {
                        totalAmount30 += value;
                    }
                }
                searchResult.clear();
                searchResult.add(new NameValue("20岁以下", totalAmount20));
                searchResult.add(new NameValue("20岁至29岁", totalAmount20to29));
                searchResult.add(new NameValue("30岁以上", totalAmount30));

            }
            return searchResult;

        } else if ("gender".equals(t)) {
            if (searchResult.size() > 0) {
                for (NameValue nameValue : searchResult) {
                    String name = nameValue.getName();
                    if (name.equals("F")) {
                        nameValue.setName("女");
                    } else if (name.equals("M")) {
                        nameValue.setName("男");
                    }
                }
            }
            return searchResult;
        } else {
            return null;
        }
    }

    public String typeToField(String t) {
        if ("age".equals(t)) {
            return "user_age";
        } else if ("gender".equals(t)) {
            return "user_gender";
        } else {
            return null;

        }
    }

}
