package com.models;

import com.Backupable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "users")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User implements Backupable {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "type")
    private String type;

    @Column(name = "active_order")
    private Long activeOrder;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Override
    public String toString() {
        return "";
    }
}
