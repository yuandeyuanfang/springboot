package com.example.demo.mapper;

import com.example.demo.dto.TradeLogDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeLogMapper {
    int insert(TradeLogDTO record);

    int update(TradeLogDTO record);
}