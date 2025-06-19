package com.example.cameraecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cameraecommerce.entity.ProductCategory;
import com.example.cameraecommerce.mapper.ProductCategoryMapper;
import com.example.cameraecommerce.service.ProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Override
    public List<ProductCategory> listByParentId(Long parentId) {
        LambdaQueryWrapper<ProductCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductCategory::getParentId, parentId)
                    .orderByAsc(ProductCategory::getSortOrder); // 按排序字段排序
        return list(queryWrapper);
    }
}
