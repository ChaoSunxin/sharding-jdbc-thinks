package com.chaosunxin.shardingjdbcthinks.repository;

import com.chaosunxin.shardingjdbcthinks.model.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemModel, String> {

    @Query(value = "select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item i left join t_order o on o.order_id = i.order_id where o.order_id = ?1 and o.user_id = ?2", nativeQuery = true)
    List<OrderItemModel> queryByOrderId(String orderId, String userId);
}
