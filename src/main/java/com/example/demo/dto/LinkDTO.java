package com.example.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * LINK
 * @author 
 */
@Data
public class LinkDTO implements Serializable {

    /**
     * 主键
     */
    private String rid;

    /**
     * 长链接
     */
    private String longUrl;

    /**
     * 短链接
     */
    private String shortUrl;

    private static final long serialVersionUID = 1L;
}