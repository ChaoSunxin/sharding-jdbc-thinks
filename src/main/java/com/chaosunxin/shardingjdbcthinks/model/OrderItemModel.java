package com.chaosunxin.shardingjdbcthinks.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "t_order_item")
public class OrderItemModel {

    @Column(name = "item_id")
    @Id
    private String itemId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "item_nums")
    private Integer itemNums;

    @Column(name = "gmt_create")
    @CreatedDate
    private Date gmtCreate;

    @Column(name = "gmt_modified")
    @LastModifiedDate
    private Date gmtModified;
}
