package com.example.emos.wx.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 描述: 系统常量类
 */
@Component
@Data
public class SystemConstants {
    public String attendanceStartTime;
    public String attendanceTime;
    public String attendanceEndTime;
    public String closingStartTime;
    public String closingTime;
    public String closingEndTime;
}
