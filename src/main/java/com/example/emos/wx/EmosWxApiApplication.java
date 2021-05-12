package com.example.emos.wx;

import cn.hutool.core.util.StrUtil;
import com.example.emos.wx.config.SystemConstants;
import com.example.emos.wx.db.dao.SysConfigDao;
import com.example.emos.wx.db.pojo.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;

@SpringBootApplication
@ServletComponentScan//使创建的filter生效
@Slf4j

public class EmosWxApiApplication {

    @Autowired
    private SysConfigDao sysConfigDao;

    @Autowired
    private SystemConstants systemConstants;

    public static void main(String[] args) {
        SpringApplication.run(EmosWxApiApplication.class, args);
    }

    @PostConstruct
    public void init(){//在项目初始化时完成常量的声明
        List<SysConfig> list = sysConfigDao.selectAllParam();
        for (SysConfig one : list){
            String key = one.getParamKey();
            key = StrUtil.toCamelCase(key);//转换成驼峰命名法
            String value = one.getParamValue();
            try {
                Field field = systemConstants.getClass().getDeclaredField(key);//反射
                field.set(systemConstants,value);
            }catch (Exception e){
                log.error("执行异常",e);
            }
        }
    }

}
