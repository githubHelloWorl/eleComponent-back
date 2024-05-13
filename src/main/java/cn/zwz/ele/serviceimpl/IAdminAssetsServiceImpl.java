package cn.zwz.ele.serviceimpl;

import cn.zwz.ele.entity.AdminAssets;
import cn.zwz.ele.mapper.AdminAssetsMapper;
import cn.zwz.ele.service.IAdminAssetsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电子元器件库存接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IAdminAssetsServiceImpl extends ServiceImpl<AdminAssetsMapper, AdminAssets> implements IAdminAssetsService {

}