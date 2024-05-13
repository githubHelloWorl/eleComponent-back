package cn.zwz.ele.vo;

import lombok.Data;

/**
 * @author 郑为中
 * 电子元器件导入 VO 类
 */
@Data
public class AdminAssetsImportVo {
    private String 元器件大类;
    private String 元器件分类;
    private String 元器件名称;
    private String 型号;
    private Integer 数量;
    private String 仓库名称;
    private String 管理人员;
}
