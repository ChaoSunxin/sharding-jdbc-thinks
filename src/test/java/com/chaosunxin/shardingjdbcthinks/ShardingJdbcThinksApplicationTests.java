package com.chaosunxin.shardingjdbcthinks;

import com.chaosunxin.shardingjdbcthinks.model.OrderModel;
import com.chaosunxin.shardingjdbcthinks.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class ShardingJdbcThinksApplicationTests {


    @Autowired
    private OrderRepository orderRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void createOrder() {
        List<OrderModel> orderModelList = new LinkedList<>();

        OrderModel orderModel = new OrderModel();
        orderModel.setOrderId("20230001");
        orderModel.setUserId("20230001");


        OrderModel orderModel2 = new OrderModel();
        orderModel2.setOrderId("20230002");
        orderModel2.setUserId("20230002");


        OrderModel orderModel3 = new OrderModel();
        orderModel3.setOrderId("20230003");
        orderModel3.setUserId("20230003");

        orderModelList.add(orderModel);
        orderModelList.add(orderModel2);
        orderModelList.add(orderModel3);
        orderRepository.saveAll(orderModelList);
    }

}
