package com.chaosunxin.shardingjdbcthinks.repository;

import com.chaosunxin.shardingjdbcthinks.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, String> {

    List<OrderModel> queryByUserId(String userId);

    OrderModel queryByOrderId(String orderId);
}
