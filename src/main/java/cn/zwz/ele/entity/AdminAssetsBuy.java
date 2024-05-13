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
@Table(name = "a_admin_assets_buy")
@TableName("a_admin_assets_buy")
@ApiModel(value = "电子元器件采购")
public class AdminAssetsBuy extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总付款金额")
    private BigDecimal money;

    @ApiModelProperty(value = "首次付款金额")
    private BigDecimal money1;

    @ApiModelProperty(value = "发起人ID")
    private String userId;

    @ApiModelProperty(value = "发起人流程ID")
    private String userCode;

    @ApiModelProperty(value = "发起人姓名")
    private String userName;

    @ApiModelProperty(value = "元器件ID")
    private String assetId;

    @ApiModelProperty(value = "元器件编码")
    private String assetCode;

    @ApiModelProperty(value = "元器件名称")
    private String name;

    @ApiModelProperty(value = "元器件型号")
    private String model;

    @ApiModelProperty(value = "元器件单位")
    private String unit;

    @ApiModelProperty(value = "供应商ID")
    private String supplierId;

    @ApiModelProperty(value = "供应商姓名")
    private String supplierName;

    @ApiModelProperty(value = "开户行")
    private String open;

    @ApiModelProperty(value = "银行账号")
    private String code;

    @ApiModelProperty(value = "付款抬头")
    private String rise;

    @ApiModelProperty(value = "收款人姓名")
    private String pushName;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "可入库数量")
    private BigDecimal quantity2;

    @ApiModelProperty(value = "采购事由")
    private String reason;

    @ApiModelProperty(value = "是否审核")
    private Integer auditFlag;

    @ApiModelProperty(value = "是否提交")
    private Integer submitFlag;

    @ApiModelProperty(value = "是否入库",notes = "0 未入库 | 1 部分入库 | 2 全部入库")
    private Integer wareFlag;
}