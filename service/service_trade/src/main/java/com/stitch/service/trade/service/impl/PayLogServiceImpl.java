package com.stitch.service.trade.service.impl;

import com.stitch.service.trade.entity.PayLog;
import com.stitch.service.trade.mapper.PayLogMapper;
import com.stitch.service.trade.service.IPayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2021-03-18
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements IPayLogService {

}
