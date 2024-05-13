package cn.zwz.ele.controller;

import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.ele.entity.AdminAssetUnit;
import cn.zwz.ele.service.IAdminAssetUnitService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 郑为中
 */
@RestController
@Api(tags = "电子元器件计量单位")
@RequestMapping("/zwz/adminAssetUnit")
@Transactional
public class AdminAssetUnitController {

    @Autowired
    private IAdminAssetUnitService iAdminAssetUnitService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有元器件计量单位")
    public Result<List<AdminAssetUnit>> getAll(){
        List<AdminAssetUnit> list = iAdminAssetUnitService.list();
        return new ResultUtil<List<AdminAssetUnit>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询元器件计量单位")
    public Result<IPage<AdminAssetUnit>> getByPage(@ModelAttribute AdminAssetUnit unit, @ModelAttribute PageVo page) {
        QueryWrapper<AdminAssetUnit> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(unit.getName())) {
            qw.like("name",unit.getName());
        }
        if(!ZwzNullUtils.isNull(unit.getBm())) {
            qw.like("bm",unit.getBm());
        }
        return new ResultUtil<IPage<AdminAssetUnit>>().setData(iAdminAssetUnitService.page(PageUtil.initMpPage(page),qw));
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增元器件计量单位")
    public Result<AdminAssetUnit> insert(AdminAssetUnit adminAssetUnit){
        int number = (int)iAdminAssetUnitService.count() + 1;
        adminAssetUnit.setBm("DW" + number);
        if(iAdminAssetUnitService.saveOrUpdate(adminAssetUnit)){
            return new ResultUtil<AdminAssetUnit>().setData(adminAssetUnit);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑元器件计量单位")
    public Result<AdminAssetUnit> update(AdminAssetUnit adminAssetUnit){
        if(iAdminAssetUnitService.saveOrUpdate(adminAssetUnit)){
            return new ResultUtil<AdminAssetUnit>().setData(adminAssetUnit);
        }
        return ResultUtil.error();
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除元器件计量单位")
    public Result<Object> delAllByIds(@RequestParam String[] ids){
        for(String id : ids){
            iAdminAssetUnitService.removeById(id);
        }
        return ResultUtil.success();
    }
}
