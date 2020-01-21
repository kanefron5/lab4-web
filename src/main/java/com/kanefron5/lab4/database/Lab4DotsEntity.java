package com.kanefron5.lab4.database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lab4_dots", schema = "s264425", catalog = "studs")
//@Table(name = "lab4_dots")

public class Lab4DotsEntity {
    private Integer id;
    private String owner;
    private Double x;
    private Double y;
    private Double r;
    private Integer popadanie;

    public Lab4DotsEntity() {
    }

    public Lab4DotsEntity(String owner, Double x, Double y, Double r) {
        this.owner = owner;
        this.x = x;
        this.y = y;
        this.r = r;
        this.popadanie = checkPopadanie() ? 1 : 0;
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
    @Column(name = "owner", nullable = true)
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Basic
    @Column(name = "x", nullable = true, precision = 0)
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @Basic
    @Column(name = "y", nullable = true, precision = 0)
    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Basic
    @Column(name = "r", nullable = true, precision = 0)
    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    @Basic
    @Column(name = "popadanie", nullable = true)
    public Integer getPopadanie() {
        return popadanie;
    }

    public void setPopadanie(Integer popadanie) {
        this.popadanie = popadanie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lab4DotsEntity that = (Lab4DotsEntity) o;
        return id.equals(that.id) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(x, that.x) &&
                Objects.equals(y, that.y) &&
                Objects.equals(r, that.r) &&
                Objects.equals(popadanie, that.popadanie);
    }

    private boolean checkPopadanie() {
        if (x < 0 && y < 0) {
            return false;
        } else if (x > 0 && y <= 0) {
            return y > -r && x < r / 2;
        } else if (x <= 0 && y > 0) {
            return x * x + y * y < r * r;
        } else if (x >= 0 && y >= 0) {
            return y < (-x / 2 + r / 2);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, x, y, r, popadanie);
    }
}
