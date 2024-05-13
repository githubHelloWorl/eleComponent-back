package cn.zwz.ele.controller;

import cn.hutool.core.date.DateUtil;
import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.redis.RedisTemplateHelper;
import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.basics.utils.SecurityUtil;
import cn.zwz.data.entity.User;
import cn.zwz.data.service.IUserService;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.ele.entity.*;
import cn.zwz.ele.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 郑为中
 */
@Slf4j
@RestController
@Api(tags = "电子元器件采购")
@RequestMapping("/zwz/adminAssetsBuy")
@Transactional
public class AdminAssetsBuyController {

    @Autowired
    private IAdminAssetsBuyService iAdminAssetsBuyService;

    @Autowired
    private IAdminAssetsService iAdminAssetsService;

    @Autowired
    private IAdminAssetService iAdminAssetService;

    @Autowired
    private IAdminAssetSupplierService iAdminAssetSupplierService;

    @Autowired
    private IAdminAssetWareService iAdminAssetWareService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询元器件采购")
    public Result<IPage<AdminAssetsBuy>> getByPage(@ModelAttribute AdminAssetsBuy buy, @ModelAttribute PageVo page) {
        QueryWrapper<AdminAssetsBuy> qw = new QueryWrapper<>();
        qw.eq("user_id",securityUtil.getCurrUser().getId());
        if(!ZwzNullUtils.isNull(buy.getName())) {
            qw.like("name",buy.getName());
        }
        if(!ZwzNullUtils.isNull(buy.getReason())) {
            qw.like("reason",buy.getReason());
        }
        IPage<AdminAssetsBuy> data = iAdminAssetsBuyService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<AdminAssetsBuy>>().setData(data);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增元器件采购")
    public Result<AdminAssetsBuy> insert(AdminAssetsBuy adminAssetsBuy){
        if(adminAssetsBuy.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
            return ResultUtil.error("总费用必须为正数");
        }
        if(adminAssetsBuy.getMoney1().compareTo(BigDecimal.ZERO) <= 0) {
            return ResultUtil.error("首次费用必须为正数");
        }
        if(adminAssetsBuy.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            return ResultUtil.error("采购数量必须为正数");
        }
        AdminAsset adminAsset = iAdminAssetService.getById(adminAssetsBuy.getAssetId());
        if(adminAsset == null) {
            return ResultUtil.error("元器件品类不存在");
        }
        adminAssetsBuy.setName(adminAsset.getName());
        adminAssetsBuy.setModel(adminAsset.getXh());
        adminAssetsBuy.setUnit(adminAsset.getJldw());
        adminAssetsBuy.setAssetCode(adminAsset.getCode());
        // 填充发起人
        User currUser = securityUtil.getCurrUser();
        adminAssetsBuy.setUserId(currUser.getId());
        adminAssetsBuy.setUserName(currUser.getNickname());
        adminAssetsBuy.setUserCode(currUser.getUsername());
        // 空置
        adminAssetsBuy.setSupplierId("");
        adminAssetsBuy.setSupplierName("");
        adminAssetsBuy.setReason("");
        adminAssetsBuy.setQuantity2(BigDecimal.ZERO);
        adminAssetsBuy.setAuditFlag(0);
        adminAssetsBuy.setSubmitFlag(0);
        adminAssetsBuy.setWareFlag(0);
        adminAssetsBuy.setOpen("");
        adminAssetsBuy.setCode("");
        adminAssetsBuy.setRise("");
        adminAssetsBuy.setPushName("");
        iAdminAssetsBuyService.saveOrUpdate(adminAssetsBuy);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑元器件采购")
    public Result<AdminAssetsBuy> update(AdminAssetsBuy adminAssetsBuy){
        if(adminAssetsBuy.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
            return ResultUtil.error("预估总费用必须为正数");
        }
        if(adminAssetsBuy.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            return ResultUtil.error("采购数量必须为正数");
        }
        AdminAsset adminAsset = iAdminAssetService.getById(adminAssetsBuy.getAssetId());
        if(adminAsset == null) {
            return ResultUtil.error("元器件品类不存在");
        }
        adminAssetsBuy.setName(adminAsset.getName());
        if(!Objects.equals(0,adminAssetsBuy.getSubmitFlag())) {
            return ResultUtil.error("已提交的采购单不可修改");
        }
        iAdminAssetsBuyService.saveOrUpdate(adminAssetsBuy);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除元器件采购")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            AdminAssetsBuy buy = iAdminAssetsBuyService.getById(id);
            if(buy == null) {
                continue;
            }
            if(!Objects.equals(0,buy.getSubmitFlag())) {
                return ResultUtil.error("已提交的采购单不可删除");
            }
            iAdminAssetsBuyService.removeById(id);
        }
        return ResultUtil.success();
    }

    @RequestMapping(value = "/submitData", method = RequestMethod.POST)
    @ApiOperation(value = "提交元器件采购")
    public Result<Object> submitData(@RequestParam String[] ids,@RequestParam String reason,@RequestParam String supplierId){
        AdminAssetSupplier supplier = iAdminAssetSupplierService.getById(supplierId);
        if(supplier == null) {
            return ResultUtil.error("供应商不存在");
        }
        List<AdminAssetsBuy> voList = new ArrayList<>();
        for(String id : ids){
            AdminAssetsBuy buy = iAdminAssetsBuyService.getById(id);
            if(buy == null) {
                return ResultUtil.error("id = " + id + "不存在");
            }
            if(Objects.equals(0,buy.getSubmitFlag())) {
                voList.add(buy);
            }
        }
        for (AdminAssetsBuy vo : voList) {
            vo.setSubmitFlag(1);
            vo.setOpen(supplier.getOpen());
            vo.setPushName(supplier.getUserName());
            vo.setCode(supplier.getCode());
            vo.setRise(supplier.getRise());
            vo.setSupplierId(supplier.getId());
            vo.setSupplierName(supplier.getName());
            iAdminAssetsBuyService.saveOrUpdate(vo);
        }
        return ResultUtil.success();
        
    }


    @RequestMapping(value = "/auditSuccessFlowCallBack", method = RequestMethod.GET)
    @ApiOperation(value = "采购审批通过回调")
    public Result<Object> auditSuccess(@RequestParam String flowId){
        QueryWrapper<AdminAssetsBuy> qw = new QueryWrapper<>();
        qw.eq("flow_id",flowId);
        qw.eq("audit_flag",0);
        List<AdminAssetsBuy> buyList = iAdminAssetsBuyService.list(qw);
        if(buyList.size() < 1) {
            return ResultUtil.success();
        }
        AdminAssetSupplier supplier = iAdminAssetSupplierService.getById(buyList.get(0).getSupplierId());
        if(supplier == null) {
            return ResultUtil.error("供应商不存在");
        }
        for (AdminAssetsBuy vo : buyList) {
            vo.setAuditFlag(1);
            vo.setQuantity2(vo.getQuantity());
            iAdminAssetsBuyService.saveOrUpdate(vo);
        }
        return ResultUtil.success();
    }

    @RequestMapping(value = "/auditData", method = RequestMethod.POST)
    @ApiOperation(value = "审核元器件采购")
    public Result<Object> auditData(@RequestParam String[] ids,@RequestParam int status){
        for(String id : ids){
            AdminAssetsBuy buy = iAdminAssetsBuyService.getById(id);
            if(buy == null || !Objects.equals(0,buy.getAuditFlag())) {
                continue;
            }
            User user = iUserService.getById(buy.getUserId());
            if(user == null) {
                continue;
            }
            buy.setAuditFlag(status);
            // 通过
            if(Objects.equals(1,status)) {
                buy.setQuantity2(buy.getQuantity());
            }
            iAdminAssetsBuyService.saveOrUpdate(buy);
        }
        return ResultUtil.success();
    }

    @RequestMapping(value = "/inWare", method = RequestMethod.POST)
    @ApiOperation(value = "入库元器件采购")
    public Result<Object> inWare(@RequestParam String id,@RequestParam int quantity,@RequestParam String date,@RequestParam String warehouse){
        AdminAssetsBuy buy = iAdminAssetsBuyService.getById(id);
        if(buy == null) {
            return ResultUtil.error("采购单不存在");
        }
        AdminAssetWare ware = iAdminAssetWareService.getById(warehouse);
        if(ware == null) {
            return ResultUtil.error("仓库不存在");
        }
        if(ZwzNullUtils.isNull(date)) {
            date = DateUtil.today();
        }
        QueryWrapper<AdminAssets> qw = new QueryWrapper<>();
        qw.eq("asset_id",buy.getAssetId());
        Long assetsCount = iAdminAssetsService.count(qw);
        for(int i = 1 ; i <= quantity; i ++) {
            AdminAssets adminAssets = new AdminAssets();
            adminAssets.setName(buy.getName());
            adminAssets.setModel(buy.getModel());
            adminAssets.setAssetId(buy.getAssetId());
            adminAssets.setCode("ZC" + buy.getAssetCode() + String.format("%04d", assetsCount + i));
            adminAssets.setUnit(buy.getUnit());
            adminAssets.setBuyUser(buy.getUserName());
            adminAssets.setBuyNumber(buy.getId());
            adminAssets.setBuyDate(date);
            adminAssets.setWarehouse(ware.getId());
            adminAssets.setWarehouseName(ware.getName());
            adminAssets.setGiveType(0);
            adminAssets.setGiveId("");
            adminAssets.setGiveName("");
            adminAssets.setOutFlag(0);
            adminAssets.setOutWork("");
            adminAssets.setOutTime("");
            adminAssets.setStatus(0);
            adminAssets.setDestroyName("");
            adminAssets.setDestroyTime("");
            adminAssets.setApplyFlag(0);
            adminAssets.setApplyUser("");
            adminAssets.setApplyTime("");
            iAdminAssetsService.saveOrUpdate(adminAssets);
        }
        if(BigDecimal.valueOf(quantity).compareTo(buy.getQuantity())< 0) {
            buy.setQuantity2(buy.getQuantity2().subtract(BigDecimal.valueOf(quantity)));
            buy.setWareFlag(1);
        } else {
            buy.setQuantity2(BigDecimal.ZERO);
            buy.setWareFlag(2);
        }
        iAdminAssetsBuyService.saveOrUpdate(buy);
        return ResultUtil.success();
    }
}
