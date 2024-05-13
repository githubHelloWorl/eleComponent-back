package cn.zwz.ele.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;

/**
 * @author 郑为中
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_admin_assets")
@TableName("a_admin_assets")
@ApiModel(value = "电子元器件库存")
public class AdminAssets extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "元器件名称")
    private String name;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "品类ID")
    private String assetId;

    @ApiModelProperty(value = "具体元器件编码")
    private String code;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "采购人")
    private String buyUser;

    @ApiModelProperty(value = "采购单ID")
    private String buyNumber;

    @ApiModelProperty(value = "采购日期")
    private String buyDate;

    @ApiModelProperty(value = "仓库")
    private String warehouse;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "领用类型",notes = "0 按人出库 | 1 按部门出库 | 2 销毁出库")
    private Integer giveType;

    @ApiModelProperty(value = "领用人ID")
    private String giveId;

    @ApiModelProperty(value = "领用人")
    private String giveName;

    @ApiModelProperty(value = "领用工号")
    private String giveJob;

    @ApiModelProperty(value = "是否出库",notes = "0 未领用 | 1 已领用")
    private Integer outFlag;

    @ApiModelProperty(value = "出库操作人")
    private String outWork;

    @ApiModelProperty(value = "出库操作时间")
    private String outTime;

    @ApiModelProperty(value = "销毁状态")
    private Integer status;

    @ApiModelProperty(value = "销毁人")
    private String destroyName;

    @ApiModelProperty(value = "销毁时间")
    private String destroyTime;

    @ApiModelProperty(value = "是否申请",notes = "0 未申请 | 1 已申请")
    private Integer applyFlag;

    @ApiModelProperty(value = "申请人")
    private String applyUser;

    @ApiModelProperty(value = "申请时间")
    private String applyTime;
}