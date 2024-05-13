package cn.zwz.ele.controller;

import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.ele.entity.AdminAsset;
import cn.zwz.ele.entity.AdminAssetType;
import cn.zwz.ele.service.IAdminAssetService;
import cn.zwz.ele.service.IAdminAssetTypeService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author 郑为中
 */
@RestController
@Api(tags = "电子元器件类型")
@RequestMapping("/zwz/adminAssetType")
@Transactional
public class AdminAssetTypeController {

    @Autowired
    private IAdminAssetTypeService iAdminAssetTypeService;

    @Autowired
    private IAdminAssetService iAdminAssetService;

    @RequestMapping(value = "/getCascader", method = RequestMethod.GET)
    @ApiOperation(value = "查询元器件类型级联数据")
    public Result<JSONArray> getCascader(){
        QueryWrapper<AdminAssetType> qw = new QueryWrapper<>();
        qw.orderByAsc("sort_order");
        List<AdminAssetType> typeList = iAdminAssetTypeService.list(qw);
        JSONArray ans = new JSONArray();
        for (AdminAssetType vo1 : typeList) {
            boolean flag = true;
            JSONObject jo = new JSONObject();
            jo.put("label",vo1.getTitle());
            jo.put("value",vo1.getTitle());
            for(int i = 0; i < ans.size(); i ++) {
                JSONObject jo2 = ans.getJSONObject(i);
                if(Objects.equals(vo1.getType(),jo2.getString("label"))) {
                    JSONArray itemList = jo2.getJSONArray("children");
                    itemList.add(jo);
                    jo2.put("children",itemList);
                    flag = false;
                    break;
                }
            }
            if(flag) {
                JSONObject jo2 = new JSONObject();
                jo2.put("value",vo1.getType());
                jo2.put("label",vo1.getType());
                JSONArray ja = new JSONArray();
                ja.add(jo);
                jo2.put("children",ja);
                ans.add(jo2);
            }
        }
        return new ResultUtil<JSONArray>().setData(ans);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询电子元器件类型")
    public Result<IPage<AdminAssetType>> getByPage(@ModelAttribute AdminAssetType type,@ModelAttribute PageVo page){
        QueryWrapper<AdminAssetType> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(type.getType())) {
            qw.eq("type",type.getType());
        }
        if(!ZwzNullUtils.isNull(type.getTitle())) {
            qw.like("title",type.getTitle());
        }
        IPage<AdminAssetType> data = iAdminAssetTypeService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<AdminAssetType>>().setData(data);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增电子元器件类型")
    public Result<AdminAssetType> insert(AdminAssetType adminAssetType){
        QueryWrapper<AdminAssetType> qw2 = new QueryWrapper<>();
        qw2.eq("type",adminAssetType.getType());
        qw2.eq("title",adminAssetType.getTitle());
        if(iAdminAssetTypeService.count(qw2) > 0L) {
            return ResultUtil.error("元器件类型已存在，请勿重复添加");
        }
        QueryWrapper<AdminAssetType> qw = new QueryWrapper<>();
        qw.eq("type",adminAssetType.getType());
        adminAssetType.setSortOrder(BigDecimal.valueOf(iAdminAssetTypeService.count(qw)));
        iAdminAssetTypeService.saveOrUpdate(adminAssetType);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑电子元器件类型")
    public Result<AdminAssetType> update(AdminAssetType adminAssetType){
        AdminAssetType old = iAdminAssetTypeService.getById(adminAssetType.getId());
        iAdminAssetTypeService.saveOrUpdate(adminAssetType);
        // 更新现有元器件的分类
        changeAsset(old,adminAssetType);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除电子元器件类型")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            AdminAssetType type = iAdminAssetTypeService.getById(id);
            // TODO 测试代码,没有用处
            QueryWrapper<AdminAsset> qw = new QueryWrapper<>();
            qw.eq("type",type.getTitle());
            qw.eq("type2",type.getType());
            if(iAdminAssetService.count(qw) > 0L) {
                return ResultUtil.error(type.getType() + "/" + type.getTitle() + " 下有元器件，不能删除!");
            }

            // TODO 新增删除操作
            iAdminAssetTypeService.removeById(id);
        }

        return ResultUtil.success();
    }

    @ApiOperation(value = "更新现有元器件的分类")
    private void changeAsset(AdminAssetType o,AdminAssetType n) {
        QueryWrapper<AdminAsset> qw = new QueryWrapper<>();
        qw.eq("type",o.getTitle());
        qw.eq("type2",o.getType());
        List<AdminAsset> assetList = iAdminAssetService.list(qw);
        for (AdminAsset as : assetList) {
            as.setType(n.getTitle());
            as.setType2(n.getType());
            iAdminAssetService.saveOrUpdate(as);
        }
    }
}
