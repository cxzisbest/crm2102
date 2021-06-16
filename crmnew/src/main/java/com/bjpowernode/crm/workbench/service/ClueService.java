package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

/**
 * 2021/5/8
 */
public interface ClueService {

    //创建线索
    int saveCreateClue(Clue clue);

    //按id查询线索详情
    Clue queryClueForDetailById(String id);
}
