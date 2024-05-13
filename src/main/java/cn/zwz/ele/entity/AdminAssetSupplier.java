package cn.zwz.ele.entity;

import cn.zwz.basics.baseClass.ZwzBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author 郑为中
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_admin_asset_supplier")
@TableName("a_admin_asset_supplier")
@ApiModel(value = "电子元器件供应商")
public class AdminAssetSupplier extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编码")
    private String bm;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "开户行")
    private String open;

    @ApiModelProperty(value = "银行账号")
    private String code;

    @ApiModelProperty(value = "付款抬头")
    private String rise;

    @ApiModelProperty(value = "收款人姓名")
    private String userName;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "电话")
    private String mobile;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态")
    private boolean status;
}