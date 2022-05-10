package com.models;

import com.Backupable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Order implements Backupable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "from_address")
    private String fromAddress;

    @Column(name = "to_address")
    private String toAddress;

    @Column(name = "cost")
    private Long cost;

    @Column(name = "start_time")
    private Long startTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "status")
    private String status;

    @Override
    public HashMap toHashMap() {
        HashMap order = new HashMap();
        order.put("id",this.id);
        order.put("clientId",this.clientId);
        order.put("driverId",this.driverId);
        order.put("fromAddress",this.fromAddress);
        order.put("toAddress",this.toAddress);
        order.put("cost",this.cost);
        order.put("startTime",this.startTime);
        order.put("endTime",this.endTime);
        order.put("status",this.status);
        return order;
    }
}
