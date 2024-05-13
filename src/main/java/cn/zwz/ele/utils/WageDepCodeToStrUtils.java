package cn.zwz.ele.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 工资条部门
 * 编号转文字工具类
 * @author 郑为中
 */
public class WageDepCodeToStrUtils {

    private static Map<Integer, Consumer<String>> FUNC_MAP = new ConcurrentHashMap<>();
    private static String ans = "";

    static {
        FUNC_MAP.put(1,title ->{ans = WageParameterValue.OA_DEP_1;});
        FUNC_MAP.put(2,title ->{ans = WageParameterValue.OA_DEP_2;});
        FUNC_MAP.put(3,title ->{ans = WageParameterValue.OA_DEP_3;});
        FUNC_MAP.put(4,title ->{ans = WageParameterValue.OA_DEP_4;});
        FUNC_MAP.put(5,title ->{ans = WageParameterValue.OA_DEP_5;});
        FUNC_MAP.put(6,title ->{ans = WageParameterValue.OA_DEP_6;});
        FUNC_MAP.put(7,title ->{ans = WageParameterValue.OA_DEP_7;});
        FUNC_MAP.put(8,title ->{ans = WageParameterValue.OA_DEP_8;});
        FUNC_MAP.put(9,title ->{ans = WageParameterValue.OA_DEP_9;});
        FUNC_MAP.put(10,title ->{ans = WageParameterValue.OA_DEP_10;});
        FUNC_MAP.put(11,title ->{ans = WageParameterValue.OA_DEP_11;});
        FUNC_MAP.put(12,title ->{ans = WageParameterValue.OA_DEP_12;});
    }

    /**
     * 编号转文字
     * @param code 编号
     * @return 文字
     */
    public static String wageDepCodeToStr(int code) {
        try {
            FUNC_MAP.get(code).accept("默认部门");
        } catch (NullPointerException e) {
            ans = WageParameterValue.OA_DEP_NULL;
        }
        return ans;
    }
}
