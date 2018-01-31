/*
 * @(#) XzsCookieGetServiceImpl.java ,2017年12月01日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.service.impl;

import com.gad.common.Constant;
import com.gad.common.HttpHelper;
import com.gad.service.XzsCookieGetService;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/12/1
 */
@Service
public class XzsCookieGetServiceImpl implements XzsCookieGetService {

    private String cookie = "__cfduid=d7ad3ab194a9a21d560df40adeb5a3e9b1512052635; cf_clearance=54fd5193678707c2a43894cb0b42904f38449517-1512052639-604800; yjs_id=TW96aWxsYS81LjAgKE1hY2ludG9zaDsgSW50ZWwgTWFjIE9TIFggMTBfMTNfMCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzYyLjAuMzIwMi45NCBTYWZhcmkvNTM3LjM2fHd3dy5jcHh6cy5jb218MTUxMjA1MjY0MDE4OXxodHRwOi8vd3d3LmNweHpzLmNvbS8; ctrl_time=1; pina=C4AX3JADIASNI; pin=Z4ZBDZ23U72COFNTXRGNC2QWK4BGN2YJLE5SPWI; pinw=JBTT72D2XTO4UBF7UD2WMTQR4Q";

    Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 检查浏览器
     */
    @Override
    public void chk() throws Exception {
        this.cookie = "";
        String res = getChkPageContent();

        //为了更真实睡眠一段时间
        Thread.sleep(7000);

        if (res.contains("安全检查")) {
            String jschl_vc = getInput("jschl_vc", res);
            String pass = getInput("pass", res);
            String jschl_answer = getAnswer(res);

            HttpHelper helper = HttpHelper.create();
            String url = String.format(Constant.XZS_CHK, jschl_vc, pass, jschl_answer);

            logger.info("验证浏览器 url : {}", url);

            HttpResponse response = helper.get(url, Boolean.FALSE)
                    .addHeader("Cookie", this.cookie)
                    .execute().getResponse();
            Assert.assertTrue("小助手浏览器检查地址返回码不对 [" + response.getStatusLine().getStatusCode() + "]", response.getStatusLine().getStatusCode() == 302);
            Header[] headers = response.getHeaders("Set-Cookie");

            Assert.assertTrue("小助手浏览器检查地址返回 cookie 不能为空", headers.length > 0);

            if (headers.length != 0) {
                this.appendCookie(headers);
            }else {
                logger.warn("小助手浏览器检查失败，返回 header 长度为 0");
            }

        }

    }


    /**
     * 获取应答码
     * @param res
     * @return
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    private static String getAnswer(String res) throws ScriptException, NoSuchMethodException {
        String script = res.substring(res.indexOf("setTimeout(function(){") + 22, res.indexOf("f.submit();"));

        script = script
                .replace("t = document.createElement('div');", "t='www.cpxzs.com'")
                .replace("t.innerHTML=\"<a href='/'>x</a>\";", "")
                .replace("t = t.firstChild.href;r = t.match(/https?:\\/\\//)[0];", "")
                .replace("t = t.substr(r.length); t = t.substr(0,t.length-1);", "")
                .replace("a = document.getElementById('jschl-answer');", "")
                .replace("f = document.getElementById('challenge-form');", "")
                .replace("a.value", "a");

        script += "\n return a;";

        script = "function b(){ \n" + script + " \n}";

        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("JavaScript"); //得到脚本引擎
        engine.eval(script);
        Invocable inv = (Invocable)engine;
        Object a = inv.invokeFunction("b");

        return a.toString().replace(".0", "");
    }

    /**
     * 获取后台返回的参数
     * @param name
     * @param res
     * @return
     */
    private static String getInput(String name, String res) {
        int sIdx = 0;
        int eIdx = 0;
        String seq = "name=\"" + name + "\" value=\"";
        sIdx = res.indexOf(seq);
        if (sIdx != -1) {
            eIdx = res.indexOf("\"/>", sIdx);
            String str = res.substring(sIdx + seq.length(), eIdx);
            return str;
        }
        return null;
    }


    @Override
    public void appendCookie(String str) {
        if (str.trim().endsWith(";")) {
            str = str.substring(0, str.trim().length() - 1);
        }
        this.cookie += str+"; ";
    }

    @Override
    public void appendCookie(Header... headers) {
        for (Header header : headers) {
            this.appendCookie(header.getValue());
        }
    }

    private String getChkPageContent() throws IOException {
        HttpHelper homeReq = HttpHelper.create().get(Constant.XZS_HOME).execute();
        HttpResponse homeRes = homeReq.getResponse();
        String res = homeReq.getResponseContent();
        Header[] h = homeRes.getHeaders("Set-Cookie");
        String cookie = h[0].getValue();
        cookie = cookie.substring(0, cookie.indexOf(";"));
        this.appendCookie(cookie);
        return res;
    }


    @Override
    public String getCookie() {
        return this.cookie;
    }

}
