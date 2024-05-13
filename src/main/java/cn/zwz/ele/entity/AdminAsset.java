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
@Table(name = "a_admin_asset")
@TableName("a_admin_asset")
@ApiModel(value = "电子元器件档案")
public class AdminAsset extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "分类")
    private String type;

    @ApiModelProperty(value = "大类")
    private String type2;

    @ApiModelProperty(value = "规格")
    private String gg;

    @ApiModelProperty(value = "型号")
    private String xh;

    @ApiModelProperty(value = "色号")
    private String color;

    @ApiModelProperty(value = "用途")
    private String purpose;

    @ApiModelProperty(value = "计量单位")
    private String jldw;

    @ApiModelProperty(value = "销售价")
    private BigDecimal xsj;

    @ApiModelProperty(value = "物料图片")
    private String imageUrl;

    @ApiModelProperty(value = "采购状态")
    private String status1;

    @ApiModelProperty(value = "物料状态")
    private String status2;

    @ApiModelProperty(value = "备注")
    private String remark;

    @Transient
    @TableField(exist=false)
    @ApiModelProperty(value = "元器件个数")
    private Long count;
}