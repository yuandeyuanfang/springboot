package com.example.demo.mapper;

import com.example.demo.entity.ParseLog;
import com.example.demo.vo.ParamInfoVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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

    @Select("select 1 name ,2 card from dual where akb999 = #{akb999}")
    List<HashMap> selectBysql(@Param("akb999") String akb999);

    @Update("update TALENT_POOL set source='第三方接口添加校验通过' where source='第三方接口添加' ")
    void updateBysql(String sql);

    /**
     * 获取下一个序列号
     * @return
     */
    Long getSequence();
}