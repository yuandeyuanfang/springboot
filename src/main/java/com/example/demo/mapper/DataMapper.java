package com.example.demo.mapper;

import com.example.demo.entity.ParseLog;
import com.example.demo.vo.ParamInfoVO;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataMapper {
    int testData();

    List<Object> testSql(String sql);

    List<ParamInfoVO> selectParamInfo(ParamInfoVO paramInfo);

    void insertParamInfo(ParamInfoVO paramInfo);

    void updateParamInfo(ParamInfoVO paramInfo);

    void deleteParamInfo(ParamInfoVO paramInfo);

    List<ParamInfoVO> selectParamInfoPage(ParamInfoVO paramInfo, RowBounds rowBounds);

    void insertParseLog(ParseLog parseLog);
}