package com.chaosunxin.shardingjdbcthinks;
import java.util.Date;

import com.chaosunxin.shardingjdbcthinks.model.OrderItemModel;
import com.chaosunxin.shardingjdbcthinks.model.OrderModel;
import com.chaosunxin.shardingjdbcthinks.repository.OrderItemRepository;
import com.chaosunxin.shardingjdbcthinks.repository.OrderRepository;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class ShardingJdbcThinksApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(ShardingJdbcThinksApplicationTests.class);
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

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

    @Test
    void addOrderItem() {
        OrderItemModel itemModel = new OrderItemModel();
        itemModel.setItemId("20230001");
        itemModel.setOrderId("20230001");
        itemModel.setProductId("pk_001");
        itemModel.setItemNums(1);

        OrderItemModel itemModel2 = new OrderItemModel();
        itemModel2.setItemId("20230002");
        itemModel2.setOrderId("20230001");
        itemModel2.setProductId("pk_002");
        itemModel2.setItemNums(2);

        orderItemRepository.saveAll(Lists.newArrayList(itemModel, itemModel2));
    }


    /**
     * 配置的分片规则和分片键不一致
     * <p>绑定表
     * 指分片规则一致的一组分片表。 使用绑定表进行多表关联查询时，必须使用分片键进行关联，否则会出现笛卡尔积关联或跨库关联，从而影响查询效率。
     * 例如：t_order 表和 t_order_item 表，均按照 order_id 分片，并且使用 order_id 进行关联，则此两张表互为绑定表关系。
     * 绑定表之间的多表关联查询不会出现笛卡尔积关联，关联查询效率将大大提升</p>
     * <pre>
     *  t_order:
     *      database-inline INLINE db$->{user_id.toInteger() % 2}
     *      table-inline INLINE t_order_$->{order_id.toInteger() % 3}
     *  t_order_item:
     *      database-inline-order-item INLINE db$->{order_id.toInteger() % 2}
     *      table-inline-order-item INLINE t_order_item_$->{order_id.toInteger() % 3}
     * </pre>
     * Logic SQL: Logic SQL: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item i left join t_order o on o.order_id = i.order_id where o.order_id = ?
     * [20230001] -> order_id条件定位到：分片t_order_2真实表，无法定位数据节点，所以db${0...1}和t_order_item${0...2}与t_order_2 出现卡笛尔集执行情况
     * Actual SQL:
     * <pre>
     * 2023-08-20 12:10:21.666  INFO 56797 --- [    Test worker] ShardingSphere-SQL                       : Actual SQL: db1 ::: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item_0 i left join t_order_2 o on o.order_id = i.order_id where o.order_id = ? ::: [20230001]
     * 2023-08-20 12:10:21.666  INFO 56797 --- [    Test worker] ShardingSphere-SQL                       : Actual SQL: db1 ::: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item_1 i left join t_order_2 o on o.order_id = i.order_id where o.order_id = ? ::: [20230001]
     * 2023-08-20 12:10:21.667  INFO 56797 --- [    Test worker] ShardingSphere-SQL                       : Actual SQL: db1 ::: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item_2 i left join t_order_2 o on o.order_id = i.order_id where o.order_id = ? ::: [20230001]
     * 2023-08-20 12:10:21.667  INFO 56797 --- [    Test worker] ShardingSphere-SQL                       : Actual SQL: db0 ::: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item_0 i left join t_order_2 o on o.order_id = i.order_id where o.order_id = ? ::: [20230001]
     * 2023-08-20 12:10:21.667  INFO 56797 --- [    Test worker] ShardingSphere-SQL                       : Actual SQL: db0 ::: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item_1 i left join t_order_2 o on o.order_id = i.order_id where o.order_id = ? ::: [20230001]
     * 2023-08-20 12:10:21.667  INFO 56797 --- [    Test worker] ShardingSphere-SQL                       : Actual SQL: db0 ::: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item_2 i left join t_order_2 o on o.order_id = i.order_id where o.order_id = ? ::: [20230001]
     * </pre>
     * Logic SQL: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item i left join t_order o on o.order_id = i.order_id where o.order_id = ? and o.user_id = ?
     * [order_id：20230001, user_id：20230001] -> 定位到node: db0, table: t_order_2
     * Actual SQL:
     * <pre>
     * 2023-08-20 12:40:53.344  INFO 57073 --- [    Test worker] ShardingSphere-SQL                       : Actual SQL: db1 ::: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item_0 i left join t_order_2 o on o.order_id = i.order_id where o.order_id = ? and o.user_id = ? ::: [20230001, 20230001]
     * 2023-08-20 12:40:53.345  INFO 57073 --- [    Test worker] ShardingSphere-SQL                       : Actual SQL: db1 ::: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item_1 i left join t_order_2 o on o.order_id = i.order_id where o.order_id = ? and o.user_id = ? ::: [20230001, 20230001]
     * 2023-08-20 12:40:53.345  INFO 57073 --- [    Test worker] ShardingSphere-SQL                       : Actual SQL: db1 ::: select i.item_id, i.order_id,i.product_id,i.item_nums,i.gmt_create,i.gmt_modified from t_order_item_2 i left join t_order_2 o on o.order_id = i.order_id where o.order_id = ? and o.user_id = ? ::: [20230001, 20230001]
     * </pre>
     */
    @Test
    void testShardingBindingTableRelationship() {
        List<OrderItemModel> itemModelList = orderItemRepository.queryByOrderId("20230001","20230001");
        logger.info(itemModelList.toString());
    }
}
