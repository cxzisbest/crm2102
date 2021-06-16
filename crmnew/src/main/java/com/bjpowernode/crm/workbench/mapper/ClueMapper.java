package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jun 03 15:45:09 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jun 03 15:45:09 CST 2020
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jun 03 15:45:09 CST 2020
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jun 03 15:45:09 CST 2020
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jun 03 15:45:09 CST 2020
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Wed Jun 03 15:45:09 CST 2020
     */
    int updateByPrimaryKey(Clue record);

    /**
    * 保存创建的线索
    */
    int insertClue(Clue clue);

    /**
    * 根据id查询线索明细信息
    */
    Clue selectClueForDetailById(String id);

    /**
    * 根据id查询线索信息
    */
    Clue selectClueById(String id);

    /**
    * 根据id删除线索
    */
    int deleteClueById(String id);

}