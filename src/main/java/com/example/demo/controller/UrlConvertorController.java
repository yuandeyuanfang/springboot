package com.example.demo.controller;

import com.example.demo.service.UrlConvertorService;
import com.example.demo.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@Api(value = "短链服务")
@RequestMapping("/link")
public class UrlConvertorController {

    @Autowired
    private UrlConvertorService urlConvertorService;

    @PostMapping("/toshorturl")
    @ApiOperation("接收长域名,转换为短域名")
    public ResultVO<String> longUrlToShortUrl(@RequestParam("longUrl") String longUrl) {
        String uuid = UUID.randomUUID().toString();
        ResultVO<String> resultVo = new ResultVO<>();
        try {
            if (StringUtils.isBlank(longUrl)) {
                resultVo.setResultDes("入参不能为空");
                resultVo.setSuccess(false);
                return resultVo;
            }

            String shortUrl = urlConvertorService.longUrlToShortUrl(longUrl);
            resultVo.setResult(shortUrl);
            resultVo.setSuccess(true);
            return resultVo;
        } catch (Exception e) {
            resultVo.setResultDes(e.getMessage());
            resultVo.setSuccess(false);
        }
        return resultVo;
    }

    @GetMapping("/{shortUrl}")
    @ApiOperation("根据短域名,返回长域名")
    public void  getLongUrlByShortUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        if (StringUtils.isNotBlank(shortUrl)) {
            String longUrl = urlConvertorService.getLongUrlByShortUrl(shortUrl);
            response.sendRedirect(longUrl);
        }

    }

}
