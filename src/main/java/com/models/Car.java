package com.models;

import com.Backupable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;

@Table(name = "cars")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Car implements Backupable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "model")
    private String model;

    @Column(name = "color")
    private String color;

    @Column(name = "driver_id")
    private Long driverId;

    @Override
    public String toString() {
        return "";
    }

    @Override
    public HashMap toHashMap(){
        HashMap car = new HashMap();
        car.put("id",this.id);
        car.put("number",this.number);
        car.put("model",this.model);
        car.put("color",this.color);

        return car;
    }
}
