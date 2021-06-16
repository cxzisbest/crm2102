package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 2021/5/8
 */
@Controller
public class ClueController {
    @Autowired
    private ClueService clueService;

    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;

    //跳到线索首页面
    @RequestMapping("/workbench/clue/index.do")
    public String index(Model model){
        //所有用户
        List<User> userList=userService.queryAllUers();
        //取所有称呼
        List<DicValue>  appellationList=dicValueService.queryDicValueByTypeCode("appellation");
        //来源
        List<DicValue>  sourceList=dicValueService.queryDicValueByTypeCode("source");
        //状态
        List<DicValue>  clueStateList=dicValueService.queryDicValueByTypeCode("clueState");

        model.addAttribute("userList",userList);
        model.addAttribute("appellationList",appellationList);
        model.addAttribute("sourceList",sourceList);
        model.addAttribute("clueStateList",clueStateList);

        return "workbench/clue/index";
    }

    //创建线索
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session){
        User user=(User)session.getAttribute(Contants.SESSION_USER);

        clue.setId(UUIDUtils.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));

        ReturnObject returnObject=new ReturnObject();

        try{
            int ret = clueService.saveCreateClue(clue);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存线索失败");
            }
        }catch(Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存线索失败");
        }

        return returnObject;
    }

    //线索详情
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,Model model){
        //线索详情
        Clue clue=clueService.queryClueForDetailById(id);
        //线索备注
        List<ClueRemark> remarkList=clueRemarkService.queryClueRemarkForDetailByClueId(id);

        //与该线索相关联的市场活动
        List<Activity> activityList=activityService.queryActivityForDeteailByClueId(id);

        model.addAttribute("clue",clue);
        model.addAttribute("remarkList",remarkList);
        model.addAttribute("activityList",activityList);


        return "workbench/clue/detail";
    }

}
