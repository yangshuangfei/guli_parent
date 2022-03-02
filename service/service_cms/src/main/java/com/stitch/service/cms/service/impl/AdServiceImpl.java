package com.stitch.service.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stitch.common.base.result.R;
import com.stitch.service.cms.entity.Ad;
import com.stitch.service.cms.entity.vo.AdVo;
import com.stitch.service.cms.feign.OSSFileService;
import com.stitch.service.cms.mapper.AdMapper;
import com.stitch.service.cms.service.IAdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author ysf
 * @since 2021-03-03
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements IAdService {

    @Autowired
    private OSSFileService ossFileService;


    @Override
    public IPage<AdVo> selectPage(Long page, Long limit) {
        QueryWrapper<AdVo> adVoQueryWrapper = new QueryWrapper<>();
        adVoQueryWrapper.orderByAsc("a.type_id","a.sort");

        Page<AdVo> adVoPage = new Page<>(page,limit);
        List<AdVo> adVos = baseMapper.selectPageByQueryWrapper(adVoPage,adVoQueryWrapper);
        adVoPage.setRecords(adVos);
        return adVoPage;
    }

    @Override
    public boolean  removeAdImageById(String id) {
        Ad ad = baseMapper.selectById(id);
        if(ad != null) {
            String imagesUrl = ad.getImageUrl();
            if(!StringUtils.isEmpty(imagesUrl)){
                //删除图片
                R r = ossFileService.removeFile(imagesUrl);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Cacheable(value = "index",key = "'selectByAdTypeID'")
    @Override
    public List<Ad> selectByAdTypeID(String adTypeId) {
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort","id");
        queryWrapper.eq("type_id",adTypeId);

        return baseMapper.selectList(queryWrapper);
    }
}
