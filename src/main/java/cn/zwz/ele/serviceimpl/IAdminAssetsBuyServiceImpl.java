package cn.zwz.ele.serviceimpl;

import cn.zwz.ele.entity.AdminAssetsBuy;
import cn.zwz.ele.mapper.AdminAssetsBuyMapper;
import cn.zwz.ele.service.IAdminAssetsBuyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电子元器件采购接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IAdminAssetsBuyServiceImpl extends ServiceImpl<AdminAssetsBuyMapper, AdminAssetsBuy> implements IAdminAssetsBuyService {

}