package cn.zwz.ele.controller;

import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.data.entity.User;
import cn.zwz.data.service.IUserService;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.ele.entity.AdminAssetWare;
import cn.zwz.ele.service.IAdminAssetWareService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author 郑为中
 */
@RestController
@Api(tags = "电子元器件仓库档案")
@RequestMapping("/zwz/adminAssetWare")
@Transactional
public class AdminAssetWareController {

    @Autowired
    private IAdminAssetWareService iAdminAssetWareService;

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有元器件仓库档案")
    public Result<List<AdminAssetWare>> getAll(){
        return new ResultUtil<List<AdminAssetWare>>().setData(iAdminAssetWareService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询元器件仓库档案")
    public Result<IPage<AdminAssetWare>> getByPage(@ModelAttribute AdminAssetWare ware, @ModelAttribute PageVo page){
        QueryWrapper<AdminAssetWare> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(ware.getName())) {
            qw.like("name",ware.getName());
        }
        if(!ZwzNullUtils.isNull(ware.getAddress())) {
            qw.like("address",ware.getAddress());
        }
        if(!ZwzNullUtils.isNull(ware.getAdminName())) {
            qw.and(wrapper -> wrapper.eq("admin_name", ware.getAdminName()).or().eq("department",ware.getAdminName()));
        }
        IPage<AdminAssetWare> data =  iAdminAssetWareService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<AdminAssetWare>>().setData(data);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增元器件仓库档案")
    public Result<AdminAssetWare> insert(AdminAssetWare adminAssetWare){
        if(!Objects.equals(1,adminAssetWare.getAttributionType())) {
            adminAssetWare.setAttributionType(0);
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("nickname",adminAssetWare.getAdminName());
            qw.eq("status","0");
            List<User> rosterUserList = iUserService.list(qw);
            if(rosterUserList.size() < 1) {
                return ResultUtil.error("仓管员不存在或离职");
            }
            adminAssetWare.setJobNumber(rosterUserList.get(0).getUsername());
        }
        // 判断名称重复
        QueryWrapper<AdminAssetWare> qw = new QueryWrapper<>();
        qw.eq("name",adminAssetWare.getName());
        if(iAdminAssetWareService.count(qw) > 0L) {
            return ResultUtil.error(adminAssetWare.getName() + "仓库已存在，不能重复添加");
        }
        iAdminAssetWareService.saveOrUpdate(adminAssetWare);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑元器件仓库档案")
    public Result<AdminAssetWare> update(AdminAssetWare adminAssetWare){
        if(!Objects.equals(1,adminAssetWare.getAttributionType())) {
            adminAssetWare.setAttributionType(0);
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("nickname",adminAssetWare.getAdminName());
            qw.eq("status","0");
            List<User> rosterUserList = iUserService.list(qw);
            if(rosterUserList.size() < 1) {
                return ResultUtil.error("仓管员不存在或离职");
            }
            adminAssetWare.setJobNumber(rosterUserList.get(0).getUsername());
        }
        // 判断名称重复
        QueryWrapper<AdminAssetWare> qw = new QueryWrapper<>();
        qw.eq("name",adminAssetWare.getName());
        qw.ne("id",adminAssetWare.getId());
        if(iAdminAssetWareService.count(qw) > 0L) {
            return ResultUtil.error(adminAssetWare.getName() + "仓库已存在，不能重复添加");
        }
        iAdminAssetWareService.saveOrUpdate(adminAssetWare);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除元器件仓库档案")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iAdminAssetWareService.removeById(id);
        }
        return ResultUtil.success();
    }
}
