package com.example.emos.wx.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.emos.wx.config.SystemConstants;
import com.example.emos.wx.db.dao.TbCheckinDao;
import com.example.emos.wx.db.dao.TbHolidaysDao;
import com.example.emos.wx.db.dao.TbWorkdayDao;
import com.example.emos.wx.service.CheckinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 描述:  考勤签到实现类
 */
@Service
@Scope("prototype")//为了以后的邮件发送，异步
@Slf4j
public class CheckinServiceImpl implements CheckinService {

    @Autowired
    private SystemConstants systemConstants;

    @Autowired
    private TbHolidaysDao tbHolidaysDao;

    @Autowired
    private TbWorkdayDao tbWorkdayDao;

    @Autowired
    private TbCheckinDao tbCheckinDao;
    @Override
    public String validCanCheckIn(int userId, String date) {
        boolean bool_1 = tbHolidaysDao.searchTodayIsHolidays() != null ? true : false;
        boolean bool_2 = tbWorkdayDao.searchTodayIsWorkday() != null ? true : false;
        String type = "工作日";
        if (DateUtil.date().isWeekend()){
            type = "节假日";
        }
        if (bool_1){
            type = "节假日";
        }else if (bool_2){
            type = "工作日";
        }
        if (type.equals("节假日")){
            return "节假日不需要考勤";
        }
        else {
            DateTime now = DateUtil.date();//详细日期
            String start = DateUtil.today() + " " + systemConstants.attendanceStartTime;//天数+时间
            String end = DateUtil.today() + " " + systemConstants.attendanceEndTime;
            DateTime attendanceStart = DateUtil.parse(start);//拼接时间
            DateTime attendanceEnd = DateUtil.parse(end);
            if (now.isBefore(attendanceStart)){
                return "没有到达上班考勤开始时间";
            }else if (now.isAfter(attendanceEnd)){
                return "已超过上班考勤结束时间";
            } else {
                HashMap map = new HashMap();
                map.put("userId",userId);
                map.put("date",date);
                map.put("start",start);
                map.put("end",end);
                boolean bool = tbCheckinDao.haveCheckin(map) != null ? true : false;
                return bool ? "今日已考勤，不用重复考勤" : "可以考勤";
            }
        }
    }
}
