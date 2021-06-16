package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicType;

import java.util.List;

/**
 * 2021/4/26
 */
public interface DicTypeService {

    //查询所有的字典类型(过度注释)
    List<DicType> queryAllDicTypes();
    DicType queryDicTypeByCode(String code);
    int saveCreateDicType(DicType dicType);
    int deleteDicTypeByCodes(String[] codes);
    int saveEditDicType(DicType dicType);

}
