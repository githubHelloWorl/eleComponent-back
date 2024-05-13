package cn.zwz.ele.controller;

import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.ele.entity.AdminAssetSupplier;
import cn.zwz.ele.service.IAdminAssetSupplierService;
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
@Api(tags = "电子元器件供应商")
@RequestMapping("/zwz/adminAssetSupplier")
@Transactional
public class AdminAssetSupplierController {

    @Autowired
    private IAdminAssetSupplierService iAdminAssetSupplierService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有元器件供应商")
    public Result<List<AdminAssetSupplier>> getAll(){
        return new ResultUtil<List<AdminAssetSupplier>>().setData(iAdminAssetSupplierService.list());
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询元器件供应商")
    public Result<IPage<AdminAssetSupplier>> getByPage(@ModelAttribute AdminAssetSupplier supplier, @ModelAttribute PageVo page){
        QueryWrapper<AdminAssetSupplier> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(supplier.getName())) {
            qw.like("name",supplier.getName());
        }
        if(!ZwzNullUtils.isNull(supplier.getType())) {
            qw.like("type",supplier.getType());
        }
        if(!ZwzNullUtils.isNull(supplier.getAddress())) {
            qw.like("address",supplier.getAddress());
        }
        return new ResultUtil<IPage<AdminAssetSupplier>>().setData(iAdminAssetSupplierService.page(PageUtil.initMpPage(page),qw));
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增元器件供应商")
    public Result<AdminAssetSupplier> insert(AdminAssetSupplier adminAssetSupplier){
        iAdminAssetSupplierService.saveOrUpdate(adminAssetSupplier);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑元器件供应商")
    public Result<AdminAssetSupplier> update(AdminAssetSupplier adminAssetSupplier){
        iAdminAssetSupplierService.saveOrUpdate(adminAssetSupplier);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除元器件库存")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iAdminAssetSupplierService.removeById(id);
        }
        return ResultUtil.success();
    }
}
