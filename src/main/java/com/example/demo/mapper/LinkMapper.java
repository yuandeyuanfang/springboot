package com.example.demo.mapper;

import com.example.demo.dto.LinkDTO;

public interface LinkMapper {

    int deleteByPrimaryKey(String rid);

    int insert(LinkDTO record);

    LinkDTO selectByPrimaryKey(String rid);

    int updateByPrimaryKeySelective(LinkDTO record);

    int updateByPrimaryKey(LinkDTO record);

    LinkDTO selectByLongUrl(String longUrl);

    LinkDTO selectByShortUrl(String shortUrl);

}