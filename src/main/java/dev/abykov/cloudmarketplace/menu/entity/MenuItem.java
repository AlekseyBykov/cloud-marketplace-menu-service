package dev.abykov.cloudmarketplace.menu.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(
        name = "menu_items",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_menu_items_name", columnNames = "name")
        }
)
public class MenuItem {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "preparation_time", nullable = false)
    private long preparationTime;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @CreationTimestamp
    @DateTimeFormat(pattern = DATE_FORMAT)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @DateTimeFormat(pattern = DATE_FORMAT)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Type(JsonBinaryType.class)
    @Column(name = "attributes", columnDefinition = "jsonb", nullable = false)
    private MenuItemAttributes attributes;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || resolvePersistentClass(this) != resolvePersistentClass(o)) {
            return false;
        }

        MenuItem that = (MenuItem) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public final int hashCode() {
        return resolvePersistentClass(this).hashCode();
    }

    private static Class<?> resolvePersistentClass(Object o) {
        return o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
    }
}
