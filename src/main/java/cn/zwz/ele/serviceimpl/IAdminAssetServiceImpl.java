package cn.zwz.ele.serviceimpl;

import cn.zwz.ele.entity.AdminAsset;
import cn.zwz.ele.mapper.AdminAssetMapper;
import cn.zwz.ele.service.IAdminAssetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电子元器件类型接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IAdminAssetServiceImpl extends ServiceImpl<AdminAssetMapper, AdminAsset> implements IAdminAssetService {

}