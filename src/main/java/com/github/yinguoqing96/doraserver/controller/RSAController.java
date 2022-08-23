package com.github.yinguoqing96.doraserver.controller;

import com.github.yinguoqing96.doraserver.common.AjaxResult;
import com.github.yinguoqing96.doraserver.rsa.RSAEncryptUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yinguoqing
 * @date 2022/8/22
 */
@RestController
@RequestMapping("/rsa")
public class RSAController {

    @GetMapping("/getKeys")
    public AjaxResult getKeys() {
        Map<String, String> key = RSAEncryptUtil.getKeys();
        return AjaxResult.success(key);
    }
}