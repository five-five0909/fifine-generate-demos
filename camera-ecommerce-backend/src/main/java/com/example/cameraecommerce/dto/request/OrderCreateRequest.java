package com.example.cameraecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotEmpty;
// import java.util.List;
import lombok.Data;

@Data
public class OrderCreateRequest {
    // 通常从购物车选中商品生成订单，这里简化为可能需要传递收货地址等信息
    // 如果购物车ID或购物车项ID列表是必须的，可以在这里添加
    // @NotEmpty
    // private List<Long> cartItemIds;

    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;

    @NotBlank(message = "收货人电话不能为空")
    private String receiverPhone;

    @NotBlank(message = "收货地址不能为空")
    private String receiverAddress;

    private String remarks; // 订单备注

    // 支付方式等其他信息可以在后端处理或由前端传递
}
