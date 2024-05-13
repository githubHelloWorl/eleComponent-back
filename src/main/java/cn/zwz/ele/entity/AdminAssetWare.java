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

/**
 * @author 郑为中
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_admin_asset_ware")
@TableName("a_admin_asset_ware")
@ApiModel(value = "电子元器件仓库档案")
public class AdminAssetWare extends ZwzBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "仓库名称")
    private String name;

    @ApiModelProperty(value = "仓库地址")
    private String address;

    @ApiModelProperty(value = "归属类型",notes = "0 归属个人 | 1 归属部门")
    private Integer attributionType;

    @ApiModelProperty(value = "管理员")
    private String adminName;

    @ApiModelProperty(value = "管理员工号")
    private String jobNumber;

    @ApiModelProperty(value = "归属部门")
    private String department;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态")
    private boolean status;
}