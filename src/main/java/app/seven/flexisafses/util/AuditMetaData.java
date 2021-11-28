package app.seven.flexisafses.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

public class AuditMetaData {
    @JsonIgnore
    Boolean deleted = false;

    @JsonIgnore
    Boolean updated = false;

    @Indexed
    @CreatedDate
    LocalDateTime creationDate = LocalDateTime.now();

    @CreatedBy
    String createdByUser;

    @LastModifiedDate
    LocalDateTime lastModifiedDate;

    @LastModifiedBy
    String lastModifiedUserId;
}
