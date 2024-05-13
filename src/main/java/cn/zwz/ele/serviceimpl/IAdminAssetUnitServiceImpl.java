package cn.zwz.ele.serviceimpl;

import cn.zwz.ele.entity.AdminAssetUnit;
import cn.zwz.ele.mapper.AdminAssetUnitMapper;
import cn.zwz.ele.service.IAdminAssetUnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电子元器件计量单位接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IAdminAssetUnitServiceImpl extends ServiceImpl<AdminAssetUnitMapper, AdminAssetUnit> implements IAdminAssetUnitService {

}