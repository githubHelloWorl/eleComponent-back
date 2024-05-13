package cn.zwz.ele.serviceimpl;

import cn.zwz.ele.entity.AdminAssetSupplier;
import cn.zwz.ele.mapper.AdminAssetSupplierMapper;
import cn.zwz.ele.service.IAdminAssetSupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电子元器件供应商接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class IAdminAssetSupplierServiceImpl extends ServiceImpl<AdminAssetSupplierMapper, AdminAssetSupplier> implements IAdminAssetSupplierService {

}