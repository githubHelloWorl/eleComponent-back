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
import cn.zwz.ele.entity.AdminAsset;
import cn.zwz.ele.entity.AdminAssetType;
import cn.zwz.ele.entity.AdminAssetWare;
import cn.zwz.ele.entity.AdminAssets;
import cn.zwz.ele.service.IAdminAssetService;
import cn.zwz.ele.service.IAdminAssetTypeService;
import cn.zwz.ele.service.IAdminAssetWareService;
import cn.zwz.ele.service.IAdminAssetsService;
import cn.zwz.ele.utils.WageDepCodeToStrUtils;
import cn.zwz.ele.vo.AdminAssetsImportVo;
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
@Api(tags = "电子元器件库存")
@RequestMapping("/zwz/adminAssets")
@Transactional
public class AdminAssetsController {

    @Autowired
    private IAdminAssetsService iAdminAssetsService;

    @Autowired
    private IAdminAssetService iAdminAssetService;

    @Autowired
    private IAdminAssetTypeService iAdminAssetTypeService;

    @Autowired
    private IAdminAssetWareService iAdminAssetWareService;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService iUserService;

    @ApiOperation(value = "导入元器件")
    @RequestMapping(value = "/importData", method = RequestMethod.POST)
    public Result<Object> importData(@RequestBody List<AdminAssetsImportVo> voList) {
        String message = "";
        for (AdminAssetsImportVo vo : voList) {
            QueryWrapper<AdminAssetType> typeQw = new QueryWrapper<>();
            typeQw.eq("type",vo.get元器件大类());
            typeQw.eq("title",vo.get元器件分类());
            typeQw.last("limit 1");
            AdminAssetType type = iAdminAssetTypeService.getOne(typeQw);
            if(type == null) {
                message += vo.get元器件大类() + "-" + vo.get元器件分类() + " 不存在;";
                continue;
            }
            // 找仓库
            String wareTitle = vo.get仓库名称();
            QueryWrapper<AdminAssetWare> wareQw = new QueryWrapper<>();
            wareQw.eq("name",wareTitle);
            wareQw.last("limit 1");
            AdminAssetWare wareObj = iAdminAssetWareService.getOne(wareQw);
            if(wareObj == null) {
                message += vo.get仓库名称() + " 不存在;";
                continue;
            }
            // 找领用人
            QueryWrapper<User> userQw = new QueryWrapper<>();
            userQw.eq("nickname",vo.get管理人员());
            userQw.last("limit 1");
            User wareAdminRosterUser = iUserService.getOne(userQw);
            if(wareAdminRosterUser == null) {
                message += vo.get管理人员() + " 不存在;";
                continue;
            }
            // 持久化
            QueryWrapper<AdminAsset> aaQw = new QueryWrapper<>();
            aaQw.eq("type",vo.get元器件分类());
            aaQw.eq("type2",vo.get元器件大类());
            AdminAsset aa = new AdminAsset();
            aa.setName(vo.get元器件名称());
            aa.setCode("ZC" + type.getCode() + String.format("%04d", iAdminAssetService.count(aaQw) + 1));
            aa.setType(vo.get元器件分类());
            aa.setType2(vo.get元器件大类());
            aa.setGg("");
            aa.setXh(ZwzNullUtils.isNull(vo.get型号()) ? "" : vo.get型号());
            aa.setColor("");
            aa.setPurpose("");
            aa.setJldw("个");
            aa.setXsj(BigDecimal.ZERO);
            aa.setImageUrl("");
            aa.setStatus1("正常");
            aa.setStatus2("正常");
            aa.setRemark(vo.get仓库名称());
            iAdminAssetService.saveOrUpdate(aa);
            // 设计具体元器件
            Integer assetNumber = vo.get数量();
            for(int i = 1; i <= assetNumber; i ++) {
                AdminAssets aas = new AdminAssets();
                aas.setName(aa.getName());
                aas.setModel(aa.getXh());
                aas.setAssetId(aa.getId());
                aas.setCode("ZC" + aa.getCode() + String.format("%04d", i));
                aas.setUnit(aa.getJldw());
                aas.setBuyUser("初期导入");
                aas.setBuyNumber("");
                aas.setBuyDate(DateUtil.today());
                aas.setWarehouse(wareObj.getId());
                aas.setWarehouseName(wareObj.getName());
                aas.setGiveType(0);
                aas.setGiveId(wareAdminRosterUser.getId());
                aas.setGiveName(wareAdminRosterUser.getNickname());
                aas.setGiveJob(wareAdminRosterUser.getUsername());
                aas.setOutFlag(1);
                aas.setOutWork("初期导入");
                aas.setOutTime(DateUtil.now());
                aas.setStatus(0);
                aas.setDestroyName("");
                aas.setDestroyTime("");
                aas.setApplyFlag(0);
                aas.setApplyUser("");
                aas.setApplyTime("");
                iAdminAssetsService.saveOrUpdate(aas);
            }
        }
        return ResultUtil.success(message);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询元器件库存")
    public Result<IPage<AdminAssets>> getByPage(@ModelAttribute AdminAssets asset,@ModelAttribute PageVo page){
        System.out.println("asset =");
        System.out.println(asset);
        // 都为空
        QueryWrapper<AdminAssets> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(asset.getName())) {
            qw.like("name",asset.getName());
        }
        if(!ZwzNullUtils.isNull(asset.getBuyUser())) {
            qw.like("buy_user",asset.getBuyUser());
        }
        if(!ZwzNullUtils.isNull(asset.getWarehouse())) {
            qw.eq("warehouse",asset.getWarehouse());
        }
        if(!ZwzNullUtils.isNull(asset.getGiveName())) {
            qw.eq("give_name",asset.getGiveName());
        }
        if(asset.getStatus() != null) {
            qw.eq("status",asset.getStatus());
        }
        if(asset.getOutFlag() != null) {
            qw.eq("out_flag",asset.getOutFlag());
        }
        if(asset.getApplyFlag() != null) {
            qw.eq("apply_flag",asset.getApplyFlag());
        }
        if(!ZwzNullUtils.isNull(asset.getBuyDate())) {
            String[] split = asset.getBuyDate().split("ZWZ@ZWZ");
            if(split.length > 1) {
                qw.ge("buy_date",split[0]);
                qw.le("buy_date",split[1]);
            }
        }
        IPage<AdminAssets> data = iAdminAssetsService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<AdminAssets>>().setData(data);
    }

    @RequestMapping(value = "/getByMyApplyPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询我申请的元器件")
    public Result<IPage<AdminAssets>> getByMyApplyPage(@ModelAttribute AdminAssets asset,@ModelAttribute PageVo page){
        User currUser = securityUtil.getCurrUser();
        QueryWrapper<AdminAssets> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(asset.getName())) {
            qw.like("name",asset.getName());
        }
        qw.eq("apply_user",currUser.getNickname());
        if(!ZwzNullUtils.isNull(asset.getApplyTime())) {
            String[] split = asset.getApplyTime().split("ZWZ@ZWZ");
            if(split.length > 1) {
                qw.ge("apply_time",split[0]);
                qw.le("apply_time",split[1]);
            }
        }
        IPage<AdminAssets> data = iAdminAssetsService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<AdminAssets>>().setData(data);
    }

    @RequestMapping(value = "/getByNotAuditPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询待审核的元器件")
    public Result<IPage<AdminAssets>> getByNotAuditPage(@ModelAttribute AdminAssets asset,@ModelAttribute PageVo page){
        QueryWrapper<AdminAssets> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(asset.getName())) {
            qw.like("name",asset.getName());
        }
        if(!ZwzNullUtils.isNull(asset.getApplyUser())) {
            qw.like("apply_user",asset.getApplyUser());
        }
        qw.eq("apply_flag",1);
        if(!ZwzNullUtils.isNull(asset.getApplyTime())) {
            String[] split = asset.getApplyTime().split("ZWZ@ZWZ");
            if(split.length > 1) {
                qw.ge("apply_time",split[0]);
                qw.le("apply_time",split[1]);
            }
        }
        IPage<AdminAssets> data = iAdminAssetsService.page(PageUtil.initMpPage(page),qw);
        return new ResultUtil<IPage<AdminAssets>>().setData(data);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "销毁元器件")
    public Result<Object> delByIds(@RequestParam String[] ids){
        User user = securityUtil.getCurrUser();
        for(String id : ids){
            AdminAssets assets = iAdminAssetsService.getById(id);
            if(assets == null) {
                continue;
            }
            assets.setStatus(1);
            assets.setDestroyName(user.getNickname());
            assets.setDestroyTime(DateUtil.now());
            iAdminAssetsService.saveOrUpdate(assets);
        }
        return ResultUtil.success();
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ApiOperation(value = "申请元器件")
    public Result<Object> apply(@RequestParam String id){
        User user = securityUtil.getCurrUser();
        AdminAssets asset = iAdminAssetsService.getById(id);
        if(asset == null) {
            return ResultUtil.error("元器件不存在");
        }
        if(!Objects.equals(0,asset.getOutFlag())) {
            return ResultUtil.error("手慢啦!元器件已被" + asset.getGiveName() + "领用,请刷新");
        }
        asset.setApplyFlag(1);
        asset.setApplyUser(user.getNickname());
        asset.setApplyTime(DateUtil.now());
        iAdminAssetsService.saveOrUpdate(asset);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/disApply", method = RequestMethod.POST)
    @ApiOperation(value = "取消申请元器件")
    public Result<Object> disApply(@RequestParam String id){
        AdminAssets asset = iAdminAssetsService.getById(id);
        if(asset == null) {
            return ResultUtil.error("元器件不存在");
        }
        if(!Objects.equals(1,asset.getApplyFlag())) {
            return ResultUtil.error("手慢啦!元器件不在被申请的状态");
        }
        asset.setApplyFlag(0);
        asset.setApplyUser("");
        asset.setApplyTime("");
        iAdminAssetsService.saveOrUpdate(asset);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/applySuccess", method = RequestMethod.POST)
    @ApiOperation(value = "通过元器件申请")
    public Result<Object> applySuccess(@RequestParam String id){
        AdminAssets asset = iAdminAssetsService.getById(id);
        if(asset == null) {
            return ResultUtil.error("元器件不存在");
        }
        if(!Objects.equals(1,asset.getApplyFlag())) {
            return ResultUtil.error("该元器件不处于被申请状态");
        }
        User currUser = securityUtil.getCurrUser();
        asset.setApplyFlag(0);

        asset.setGiveId(currUser.getId());
        asset.setGiveName(currUser.getNickname());
        asset.setGiveJob(currUser.getUsername());
        asset.setOutFlag(1);
        asset.setOutWork(currUser.getUsername());
        asset.setOutTime(DateUtil.now());
        iAdminAssetsService.saveOrUpdate(asset);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/applyFail", method = RequestMethod.POST)
    @ApiOperation(value = "驳回元器件申请")
    public Result<Object> applyFail(@RequestParam String id){
        AdminAssets asset = iAdminAssetsService.getById(id);
        if(asset == null) {
            return ResultUtil.error("元器件不存在");
        }
        if(!Objects.equals(1,asset.getApplyFlag())) {
            return ResultUtil.error("该元器件不处于被申请状态");
        }
        asset.setApplyFlag(0);
        asset.setApplyUser("");
        asset.setApplyTime("");
        iAdminAssetsService.saveOrUpdate(asset);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/outWage", method = RequestMethod.POST)
    @ApiOperation(value = "元器件出库")
    public Result<Object> outWage(@RequestParam String[] ids,@RequestParam Integer type,@RequestParam String nickName,@RequestParam String userId){
        System.out.println("nickName =");
        System.out.println(nickName);
        User currUser = securityUtil.getCurrUser();
        for(String id : ids){
            AdminAssets assets = iAdminAssetsService.getById(id);
            if(assets == null) {
                continue;
            }
            assets.setGiveType(type);
            if(Objects.equals(1,type)) {
                assets.setGiveId(currUser.getId());
                // TODO 修改姓名
                assets.setGiveName(nickName);
                assets.setGiveJob(currUser.getUsername());
            } else if(Objects.equals(2,type)) {
                Integer userInt = Integer.parseInt(userId);
                assets.setGiveId(userId);
                String wageDepTitle = WageDepCodeToStrUtils.wageDepCodeToStr(userInt);
                if(Objects.equals("默认部门",wageDepTitle)) {
                    return ResultUtil.error(assets.getName() + "出库失败,部门不存在");
                }
                assets.setGiveName(wageDepTitle);
                assets.setGiveJob("");
            } else if(Objects.equals(3,type)) {
                assets.setGiveId("");
                assets.setGiveName("");
                assets.setGiveJob("");
                assets.setStatus(1);
                assets.setDestroyName(currUser.getNickname());
                assets.setDestroyTime(DateUtil.now());
            }
            // 直接分配的取消申请标记
            assets.setApplyFlag(0);
            assets.setApplyUser("");
            assets.setApplyTime("");
            // TODO 根据是否有nickname 来设置 outFlag
            // 持久化
//            assets.setOutFlag(1);
//            assets.setOutFlag(0);
            if(nickName.equals("") || nickName==null){
                assets.setOutFlag(0); // 不可修改
            }else{
                assets.setOutFlag(1); // 可以修改
            }
            assets.setOutWork(currUser.getUsername());
            assets.setOutTime(DateUtil.now());
            iAdminAssetsService.saveOrUpdate(assets);
        }
        return ResultUtil.success();
    }
}
