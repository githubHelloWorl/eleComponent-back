package cn.zwz.ele.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 工资条部门
 * 文字转编号工具类
 * @author 郑为中
 */
public class WageDepStrToCodeUtils {

    private static Map<String, Consumer<Integer>> FUNC_MAP = new ConcurrentHashMap<>();
    private static Integer ans = 0;

    static {
        FUNC_MAP.put("总经办",title ->{ans = WageParameterValue.OA_DEP_ZJB;});
        FUNC_MAP.put("行政综合部",title ->{ans = WageParameterValue.OA_DEP_XZZHB;});
        FUNC_MAP.put("人力资源部",title ->{ans = WageParameterValue.OA_DEP_RLZYB;});
        FUNC_MAP.put("财务部",title ->{ans = WageParameterValue.OA_DEP_CWB;});
        FUNC_MAP.put("设计研发",title ->{ans = WageParameterValue.OA_DEP_SJYF;});
        FUNC_MAP.put("产品应用",title ->{ans = WageParameterValue.OA_DEP_CPYY;});
        FUNC_MAP.put("业务部",title ->{ans = WageParameterValue.OA_DEP_YWB;});
        FUNC_MAP.put("品牌发展部",title ->{ans = WageParameterValue.OA_DEP_PPFZB;});
        FUNC_MAP.put("市场部",title ->{ans = WageParameterValue.OA_DEP_SCB;});
        FUNC_MAP.put("综合发展部",title ->{ans = WageParameterValue.OA_DEP_ZHFZB;});
        FUNC_MAP.put("客服部",title ->{ans = WageParameterValue.OA_DEP_KFB;});
        FUNC_MAP.put("培训部",title ->{ans = WageParameterValue.OA_DEP_PXB;});
    }

    /**
     * 文字转编号
     * @param str 文字
     * @return 编号
     */
    public static int wageDepCodeToStr(String str) {
        try {
            FUNC_MAP.get(str).accept(0);
        } catch (NullPointerException e) {
            ans = WageParameterValue.OA_DEP_NULL_NUMBER;
        }
        return ans;
    }
}
