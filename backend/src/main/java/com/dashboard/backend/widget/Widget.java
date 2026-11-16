package com.dashboard.backend.widget;

import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "widgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Widget {

    @Id
    @Column(name = "id", length = 64, nullable = false)
    private String id;

    @Column(name = "type", length = 32, nullable = false)
    private String type;

    @Column(name = "x", nullable = false)
    @Builder.Default
    private Integer x = 0;

    @Column(name = "y", nullable = false)
    @Builder.Default
    private Integer y = 0;

    @Column(name = "w", nullable = false)
    @Builder.Default
    private Integer w = 1;

    @Column(name = "h", nullable = false)
    @Builder.Default
    private Integer h = 1;

    @Column(name = "view_mode", length = 32)
    private String viewMode;

    @Column(name = "mode", length = 30)
    private String mode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", columnDefinition = "json")
    private Map<String, Object> data;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.x == null) {
            this.x = 0;
        }
        if (this.y == null) {
            this.y = 0;
        }
        if (this.w == null) {
            this.w = 1;
        }
        if (this.h == null) {
            this.h = 1;
        }
        // if (this.viewMode == null || this.viewMode.isBlank()) {
        // this.viewMode = "normal";
        // }
        if ("memo".equals(this.type) && (this.viewMode == null || this.viewMode.isBlank())) {
            this.viewMode = "normal";
        }
    }

}
