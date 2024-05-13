package cn.zwz.ele.serviceimpl;

import cn.zwz.ele.entity.AdminAssetWare;
import cn.zwz.ele.mapper.AdminAssetWareMapper;
import cn.zwz.ele.service.IAdminAssetWareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电子元器件仓库档案接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IAdminAssetWareServiceImpl extends ServiceImpl<AdminAssetWareMapper, AdminAssetWare> implements IAdminAssetWareService {

}