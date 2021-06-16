package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * 2021/4/30
 */
public interface ActivityService {

    //添加
    int saveCreateActivity(Activity activity);
    //分页和条件查询
    List<Activity> queryActivityForPageByCondition(Map<String, Object> map);
    //统计记录数
    long queryCountOfActivityByCondition(Map<String, Object> map);
    //按id查询，简单
    Activity queryActivityById(String id);
    //更新
    int saveEditActivity(Activity activity);
    //删除
    int deleteActivityByIds(String[] ids);
    //导出
    List<Activity> queryAllActivityForDetail();
    //ids
    List<Activity>  queryActivityForDetailByIds(String[] ids);
    //批量插入excel批量
    int saveCreateActivityByList(List<Activity> activityList);
    //按id查询，详细
    Activity queryActivityForDetailById(String id);
    //在线索模块，根据市场活动名查询市场活动
    List<Activity> queryActivityForDetailByName(String name);

    //按线索id查询相关的市场活动
    List<Activity> queryActivityForDeteailByClueId(String clueId);



}
