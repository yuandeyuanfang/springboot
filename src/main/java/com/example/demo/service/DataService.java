package com.example.demo.service;

import com.example.demo.mapper.DataMapper;
import com.example.demo.vo.ParamInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    @Autowired
    DataMapper dataMapper;

    public int testData() {
        return dataMapper.testData();
    }

    public List<Object> testSql(String sql) {
        return dataMapper.testSql(sql);
    }

    public List<ParamInfoVO> selectParamInfo(ParamInfoVO paramInfo) {
        return dataMapper.selectParamInfo(paramInfo);
    }

    public void insertParamInfo(ParamInfoVO paramInfo) {
        dataMapper.insertParamInfo(paramInfo);
    }

    public void updateParamInfo(ParamInfoVO paramInfo) {
        dataMapper.updateParamInfo(paramInfo);
    }

    public void deleteParamInfo(ParamInfoVO paramInfo) {
        dataMapper.deleteParamInfo(paramInfo);
    }
}
