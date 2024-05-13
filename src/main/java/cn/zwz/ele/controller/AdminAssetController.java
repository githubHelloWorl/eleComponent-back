package cn.zwz.ele.controller;

import cn.zwz.basics.baseVo.PageVo;
import cn.zwz.basics.baseVo.Result;
import cn.zwz.basics.utils.PageUtil;
import cn.zwz.basics.utils.ResultUtil;
import cn.zwz.data.utils.ZwzNullUtils;
import cn.zwz.ele.entity.AdminAsset;
import cn.zwz.ele.entity.AdminAssetType;
import cn.zwz.ele.entity.AdminAssets;
import cn.zwz.ele.service.IAdminAssetService;
import cn.zwz.ele.service.IAdminAssetTypeService;
import cn.zwz.ele.service.IAdminAssetsService;
import cn.zwz.ele.vo.AdminAssetImportVo;
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
@Api(tags = "电子元器件品类")
@RequestMapping("/zwz/adminAsset")
@Transactional
public class AdminAssetController {

    @Autowired
    private IAdminAssetService iAdminAssetService;

    @Autowired
    private IAdminAssetsService iAdminAssetsService;

    @Autowired
    private IAdminAssetTypeService iAdminAssetTypeService;

    @RequestMapping(value = "/importAssetType", method = RequestMethod.POST)
    @ApiOperation(value = "导入元器件品类")
    public Result<Object> importAssetType(@RequestBody List<AdminAssetImportVo> voList){
        for (AdminAssetImportVo vo : voList) {
            if(Objects.equals("办公用品",vo.get大类())) {
                vo.set大类("办公用品类");
            }
            if(Objects.equals("劳保用品",vo.get大类())) {
                vo.set大类("劳保用品类");
            }
            if(Objects.equals("固定元器件",vo.get大类())) {
                vo.set大类("固定元器件类");
            }
            if(Objects.equals("加工设备",vo.get分类())) {
                vo.set大类("加工检验");
            }
            AdminAsset a = new AdminAsset();
            a.setName(vo.get名称());
            a.setCode("");
            a.setType(vo.get分类());
            a.setType2(vo.get大类());
            a.setGg(vo.get规格());
            a.setXh(vo.get型号());
            a.setColor(vo.get色号());
            a.setPurpose(vo.get用途());
            a.setJldw(vo.get单位());
            a.setXsj(BigDecimal.ZERO);
            a.setImageUrl("");
            a.setStatus1(vo.get采购状态());
            a.setStatus2(vo.get物料状态());
            a.setRemark("");
            iAdminAssetService.saveOrUpdate(a);
        }
        List<AdminAssetType> typeList = iAdminAssetTypeService.list();
        for (AdminAssetType type : typeList) {
            QueryWrapper<AdminAsset> qw = new QueryWrapper<>();
            qw.eq("type",type.getTitle());
            qw.eq("type2",type.getType());
            List<AdminAsset> assetList = iAdminAssetService.list(qw);
            for(int i = 0; i < assetList.size(); i ++) {
                AdminAsset as = assetList.get(i);
                as.setCode("ZC" + type.getCode() + String.format("%04d", i + 1));
                iAdminAssetService.saveOrUpdate(as);
            }
        }
        return ResultUtil.success();
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "查询元器件品类")
    public Result<IPage<AdminAsset>> getByPage(@ModelAttribute AdminAsset asset, @ModelAttribute PageVo page) {
        QueryWrapper<AdminAsset> qw = new QueryWrapper<>();
        if(!ZwzNullUtils.isNull(asset.getType())) {
            qw.eq("type",asset.getType());
        }
        if(!ZwzNullUtils.isNull(asset.getType2())) {
            qw.eq("type2",asset.getType2());
        }
        if(!ZwzNullUtils.isNull(asset.getCode())) {
            qw.like("code",asset.getCode());
        }
        if(!ZwzNullUtils.isNull(asset.getName())) {
            qw.like("name",asset.getName());
        }
        if(!ZwzNullUtils.isNull(asset.getGg())) {
            qw.like("gg",asset.getGg());
        }
        IPage<AdminAsset> data = iAdminAssetService.page(PageUtil.initMpPage(page),qw);
        for (AdminAsset vo : data.getRecords()) {
            QueryWrapper<AdminAssets> itemQw = new QueryWrapper<>();
            itemQw.eq("asset_id",vo.getId());
            itemQw.eq("status",0);
            vo.setCount(iAdminAssetsService.count(itemQw));
        }
        return new ResultUtil<IPage<AdminAsset>>().setData(data);
    }

    @RequestMapping(value = "/fastInsert", method = RequestMethod.POST)
    @ApiOperation(value = "快速新增元器件品类")
    public Result<AdminAsset> fastInsert(@RequestParam String name,@RequestParam String type,@RequestParam String type2,@RequestParam String modal){
        QueryWrapper<AdminAsset> qw = new QueryWrapper<>();
        qw.eq("name",name);
        qw.eq("xh",modal);
        if(iAdminAssetService.count(qw) > 0) {
            return ResultUtil.error("元器件品类已存在，无需重复添加");
        }
        AdminAsset as = new AdminAsset();
        as.setName(name);
        as.setType(type);
        as.setType2(type2);
        as.setGg("");
        as.setXh(modal);
        as.setJldw("个");
        as.setXsj(BigDecimal.ZERO);
        as.setImageUrl("");
        as.setRemark("");
        iAdminAssetService.saveOrUpdate(as);
        return new ResultUtil<AdminAsset>().setData(as);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation(value = "新增元器件品类")
    public Result<AdminAsset> insert(AdminAsset adminAsset){
        // 分类
        QueryWrapper<AdminAssetType> typeQw = new QueryWrapper<>();
        typeQw.eq("title",adminAsset.getType());
        typeQw.eq("type",adminAsset.getType2());
        List<AdminAssetType> typeList = iAdminAssetTypeService.list(typeQw);
        if(typeList.size() < 1) {
            return ResultUtil.error("元器件分类不存在");
        }
        // 编码
        AdminAssetType type = typeList.get(0);
        QueryWrapper<AdminAsset> qw = new QueryWrapper<>();
        qw.eq("type",adminAsset.getType());
        qw.eq("type2",adminAsset.getType2());
        adminAsset.setCode("ZC" + type.getCode() + String.format("%04d", iAdminAssetService.count(qw) + 1));
        // 为空补全
        if(ZwzNullUtils.isNull(adminAsset.getJldw())) {
            adminAsset.setJldw("个");
        }
        if(ZwzNullUtils.isNull(adminAsset.getStatus1())) {
            adminAsset.setStatus1("正常");
        }
        if(ZwzNullUtils.isNull(adminAsset.getStatus2())) {
            adminAsset.setStatus2("正常");
        }
        iAdminAssetService.saveOrUpdate(adminAsset);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改元器件品类")
    public Result<AdminAsset> update(AdminAsset adminAsset){
        // 分类
        QueryWrapper<AdminAssetType> typeQw = new QueryWrapper<>();
        typeQw.eq("title",adminAsset.getType());
        typeQw.eq("type",adminAsset.getType2());
        List<AdminAssetType> typeList = iAdminAssetTypeService.list(typeQw);
        if(typeList.size() < 1) {
            return ResultUtil.error("元器件分类不存在");
        }
        iAdminAssetService.saveOrUpdate(adminAsset);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除元器件品类")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String id : ids){
            iAdminAssetService.removeById(id);
        }
        return ResultUtil.success();
    }
}
