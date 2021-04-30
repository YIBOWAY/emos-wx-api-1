package com.example.emos.wx.config.xss;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 描述:  抵御跨站脚本攻击
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (!StrUtil.hasEmpty(value)){
            HtmlUtil.filter(value);//借助hutool进行转义
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) { //对数组进行转义
        String[] values = super.getParameterValues(name);
        if (values != null){
            for (int i = 0; i < values.length; i ++){
                String value = values[i];
                if (!StrUtil.hasEmpty(value)){
                    HtmlUtil.filter(value);//借助hutool进行转义
                }
                values[i] = value;
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String,String[]> parameters = super.getParameterMap();
        LinkedHashMap map = new LinkedHashMap();//保证数据的顺序
        if (parameters != null){
            for (String key : parameters.keySet()){
                String[] values = parameters.get(key);
                for (int i = 0; i < values.length; i ++){
                    String value = values[i];
                    if (!StrUtil.hasEmpty(value)){
                        HtmlUtil.filter(value);//借助hutool进行转义
                    }
                    values[i] = value;
                }
                map.put(key,values);
            }
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (!StrUtil.hasEmpty(value)){
            HtmlUtil.filter(value);//借助hutool进行转义
        }
        return value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        InputStream in = super.getInputStream();
        InputStreamReader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
        BufferedReader buffer = new BufferedReader(reader);//创建缓冲流
        StringBuffer body = new StringBuffer();
        String line = buffer.readLine();//读取IO流中的内容
        while (line != null){
            body.append(line);//读取得到的是json格式
            line = buffer.readLine();
        }
        buffer.close();
        reader.close();
        in.close();
        Map<String,Object> map = JSONUtil.parseObj(body.toString());
        Map<String,Object> result = new LinkedHashMap<>();//用来存储转义后的数据
        for (String key : map.keySet()){
            Object val = map.get(key);
            if (val instanceof String){//是否是string类型的数据
                if (!StrUtil.hasEmpty(val.toString())){//返回字符串
                    result.put(key,HtmlUtil.filter(val.toString()));
                }
            }
            else {
                result.put(key,val);
            }
        }
        String json = JSONUtil.toJsonStr(result);//json格式的字符串
        ByteArrayInputStream bain = new ByteArrayInputStream(json.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bain.read();
            }
        };
    }
}
