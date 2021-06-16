package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 2021/4/30
 */
@Controller
public class ActivityController {


    @Autowired
    private UserService userService;


    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(Model model){

        List<User> userList=userService.queryAllUers();

        model.addAttribute("userList", userList);

        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session){
        User user=(User)session.getAttribute(Contants.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject=new ReturnObject();

        try{
            int ret=activityService.saveCreateActivity(activity);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存失败");
            }
        }catch(Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败");
        }
        return returnObject;
    }


    @RequestMapping("/workbench/activity/queryActivityForPageByCondition.do")
    public @ResponseBody Object queryActivityForPageByCondition(int pageNo,int pageSize,String name,String owner,String startDate,String endDate) {
        Map<String,Object> map=new HashMap<>();
        map.put("beginNo", (pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        //获取所有的市场活动
        List<Activity> activityList=activityService.queryActivityForPageByCondition(map);
        //获取总记录数
        long totalRows=activityService.queryCountOfActivityByCondition(map);

        Map<String,Object> retMap=new HashMap<>();
        retMap.put("activityList", activityList);
        retMap.put("totalRows",totalRows);

        return retMap;

    }


    //跳转编辑页面
    @RequestMapping("/workbench/activity/editActivity.do")
    public @ResponseBody Object editActivity(String id){
        Activity activity=activityService.queryActivityById(id);
        return activity;
    }


    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody Object saveEditActivity(Activity activity, HttpSession session){
        User user=(User)session.getAttribute(Contants.SESSION_USER);
       // activity.setId(UUIDUtils.getUUID());
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        activity.setEditBy(user.getId());

        ReturnObject returnObject=new ReturnObject();

        try{
            int ret=activityService.saveEditActivity(activity);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("更新失败");
            }
        }catch(Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败");
        }
        return returnObject;
    }

    //删除
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    public @ResponseBody Object deleteActivityByIds(String[] id){
        ReturnObject returnObject=new ReturnObject();

        try{
            int ret=activityService.deleteActivityByIds(id);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败");
            }
        }catch(Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }
        return returnObject;
    }

    //批量导出
        @RequestMapping("/workbench/activity/exportAllActivity.do")
        public void exportAllActivity(HttpServletRequest request, HttpServletResponse response) throws Exception{
            //获取数据
            List<Activity> activityList=activityService.queryAllActivityForDetail();
            //创建一个excel文件来放市场活动对象
            HSSFWorkbook wb=new HSSFWorkbook();
            //2.工作表
            HSSFSheet sheet=wb.createSheet("市场活动列表");
            //3.行,从0开始
            HSSFRow row=sheet.createRow(0);
            //4.单元格
            HSSFCell cell=row.createCell(0);
            cell.setCellValue("ID");
            cell=row.createCell(1);
            cell.setCellValue("名称");
            cell=row.createCell(2);
            cell.setCellValue("所有者");
            cell=row.createCell(3);
            cell.setCellValue("开始时间");
            cell=row.createCell(4);
            cell.setCellValue("结束时间");
            cell=row.createCell(5);
            cell.setCellValue("成本");
            cell=row.createCell(6);
            cell.setCellValue("描述");

            //style样式对象
            HSSFCellStyle style=wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);

            //遍历市场活动集合
            if(activityList!=null){
                Activity activity=null;
                for(int i=0;i<activityList.size();i++){
                    activity=activityList.get(i);
                    //创建行,第一行是标题,数据从第2行开始
                    row=sheet.createRow(i+1);
                    cell=row.createCell(0); //第2行第1列
                    cell.setCellValue(activity.getId());

                    cell=row.createCell(1);
                    cell.setCellValue(activity.getName());

                    cell=row.createCell(2);
                    cell.setCellValue(activity.getOwner());

                    cell=row.createCell(3);
                    cell.setCellValue(activity.getStartDate());

                    cell=row.createCell(4);
                    cell.setCellValue(activity.getEndDate());

                    cell=row.createCell(5);
                    cell.setCellValue(activity.getCost());

                    cell=row.createCell(6);
                    cell.setCellValue(activity.getDescription());
                }
            }

            //下载
            //设置响应类型为字节流
            response.setContentType("application/octet-stream;charset=UTF-8");

            String fileName= URLEncoder.encode("市场活动列表", "UTF-8") ;


            //设置响应头信息,让浏览器接收到响应信息之后,在下载窗口中打开,以附件来下载文件
            response.addHeader("Content-Disposition","attachment;filename="+fileName+".xls");

            OutputStream os=response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
            wb.close();

        }


        //处理上传文件
        @RequestMapping("/a/fileUpload.do")
        public @ResponseBody Object fileUpload(String username, MultipartFile myfile) throws Exception{
            System.out.println(username);
            String filename1=myfile.getName();
            String filename2=myfile.getOriginalFilename();
            //文件已经上传,将文件放到服务器某个位置
            File file=new File("d:\\testDir",filename2);
            //上传的文件在服务器temp目录,将这个文件转到指定位置.
            myfile.transferTo(file);

            ReturnObject returnObject=new ReturnObject();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            return returnObject;
        }

        @RequestMapping("/workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(String username,MultipartFile activityFile,HttpSession session){
            User user=(User)session.getAttribute(Contants.SESSION_USER);
            Map<String,Object> retMap=new HashMap<>();

            try {
                //定义存放市场活动对象的集合,每次循环获取一个市场活动对象放在集合中,然后做一个批量插入,参数市场活动集合
                List<Activity> activityList=new ArrayList<>();
                //输入流
                InputStream is=activityFile.getInputStream();
                //工作簿
                HSSFWorkbook wb=new HSSFWorkbook(is);
                //工作表
                HSSFSheet sheet=wb.getSheetAt(0);

                HSSFRow row=null;
                HSSFCell cell=null;

                Activity activity=null;
                //行
                for(int i=1;i<=sheet.getLastRowNum();i++){
                    row=sheet.getRow(i); //从第2行开始,第1行是标题
                    activity=new Activity();
                    //补充市场活动的信息
                    activity.setId(UUIDUtils.getUUID());
                    activity.setOwner(user.getId());
                    activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                    activity.setCreateBy(user.getId());

                    //列
                    for(int j=0;j<row.getLastCellNum();j++){
                        cell=row.getCell(j);
                        String cellValue=getCellValue(cell);
                        if(j==0){
                            activity.setName(cellValue);
                        }else if(j==1){
                            activity.setStartDate(cellValue);
                        }else if(j==2){
                            activity.setEndDate(cellValue);
                        }else if(j==3){
                            activity.setCost(cellValue);
                        }else if(j==4){
                            activity.setDescription(cellValue);
                        }
                    }
                    activityList.add(activity);
                }
                //批量
                int ret=activityService.saveCreateActivityByList(activityList);
                retMap.put("code",Contants.RETURN_OBJECT_CODE_SUCCESS);
                retMap.put("count",ret);
            } catch (IOException e) {
                e.printStackTrace();
                retMap.put("code",Contants.RETURN_OBJECT_CODE_FAIL);
                retMap.put("message","导入失败");
            }

            return retMap;
        }

    //读取单元格中的内容,根据不同的类型转成string
    public static String getCellValue(HSSFCell cell){
        String ret="";
        //获取到单元格的类型
        switch (cell.getCellType()){
            case HSSFCell.CELL_TYPE_STRING:
                ret=cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret=cell.getBooleanCellValue()+"";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret=cell.getNumericCellValue()+"";
                break;
            default:
                ret="";
        }
        return ret;
    }




}
