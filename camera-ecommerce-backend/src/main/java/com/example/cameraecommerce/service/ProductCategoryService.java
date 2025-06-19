package com.example.cameraecommerce.service;

import com.example.cameraecommerce.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ProductCategoryService extends IService<ProductCategory> {
    List<ProductCategory> listByParentId(Long parentId);
    // 可根据需要添加更多方法，如获取树形分类等
}
