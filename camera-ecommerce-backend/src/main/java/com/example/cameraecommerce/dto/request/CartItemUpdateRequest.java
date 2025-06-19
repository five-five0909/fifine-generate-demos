package com.example.cameraecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemUpdateRequest {
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量至少为1") // 如果要删除可以考虑允许0，但通常删除是另一个API
    private Integer quantity;

    private Boolean checkedStatus; // 可选，用于更新选中状态
}
