package com.stitch.service.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.stitch.service.cms.entity.Ad;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stitch.service.cms.entity.vo.AdVo;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author ysf
 * @since 2021-03-03
 */
public interface IAdService extends IService<Ad> {

    IPage<AdVo> selectPage(Long page, Long limit);

    boolean removeAdImageById(String id);

    List<Ad> selectByAdTypeID(String adTypeId);
}
