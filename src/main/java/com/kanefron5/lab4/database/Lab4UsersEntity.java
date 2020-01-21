package com.kanefron5.lab4.database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lab4_users", schema = "s264425", catalog = "studs")
//@Table(name = "lab4_users")

public class Lab4UsersEntity {
    private Integer id;
    private String passworduser;
    private String emailuser;


    public Lab4UsersEntity() {
    }

    public Lab4UsersEntity(String passworduser, String emailuser) {
        this.passworduser = passworduser;
        this.emailuser = emailuser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "passworduser", nullable = true)
    public String getPassworduser() {
        return passworduser;
    }

    public void setPassworduser(String passworduser) {
        this.passworduser = passworduser;
    }

    @Basic
    @Column(name = "emailuser", nullable = true)
    public String getEmailuser() {
        return emailuser;
    }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lab4UsersEntity that = (Lab4UsersEntity) o;
        return id == that.id &&
                Objects.equals(passworduser, that.passworduser) &&
                Objects.equals(emailuser, that.emailuser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passworduser, emailuser);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Lab4UsersEntity{");
        sb.append("id=").append(id);
        sb.append(", passworduser='").append(passworduser).append('\'');
        sb.append(", emailuser='").append(emailuser).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
