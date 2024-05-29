package com.example.demo.service;

import com.example.demo.dto.LinkDTO;
import com.example.demo.mapper.LinkMapper;
import com.example.demo.utils.Base62;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.hutool.core.lang.hash.MurmurHash;

import java.util.UUID;

/**
 * @author liuxh
 * @version 1.0
 * @description: TODO
 * @date 2022/1/17 16:06
 */
@Slf4j
@Service
public class UrlConvertorService {

    @Autowired
    private LinkMapper linkMapper;

    public String longUrlToShortUrl(String longUrl) {
        String shortUrl ="";
        //查询长链接是否存在
        LinkDTO linkDTO = linkMapper.selectByLongUrl(longUrl);
        // 生成短链
        if (null == linkDTO){
            shortUrl = generate(longUrl);
            LinkDTO modul  = new LinkDTO();
            modul.setLongUrl(longUrl);
            modul.setShortUrl(shortUrl);
            modul.setRid(UUID.randomUUID().toString());
            linkMapper.insert(modul);
        }else {
            shortUrl = linkDTO.getShortUrl();
        }

        return shortUrl;
    }

    public String getLongUrlByShortUrl(String shortUrl) {
        LinkDTO linkDTO = linkMapper.selectByShortUrl(shortUrl);
        if (null == linkDTO){
            return "";
        }
        return linkDTO.getLongUrl();
    }

    /**
     * 生成短url
     *
     * @param longUrl 源 长url
     * @return shortUrl
     */
    public static String generate(String longUrl) {
        String hash32 = Integer.toUnsignedString(MurmurHash.hash32(longUrl));
        int i = longUrl.hashCode() % (1 << 3);
        return Base62.encode(Long.parseUnsignedLong(hash32));
    }

}
