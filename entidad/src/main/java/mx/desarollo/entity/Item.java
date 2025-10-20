package mx.desarollo.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "item")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Item implements Serializable {

    @Id
    @Column(name = "ID_Item", length = 45, nullable = false)
    private String idItem;

    public Item() {
    }

    public Item(String idItem) {
        this.idItem = idItem;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }
}
