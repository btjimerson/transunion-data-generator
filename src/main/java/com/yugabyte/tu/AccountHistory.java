package com.yugabyte.tu;

import java.sql.Timestamp;

import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "account_history")
public class AccountHistory {

    @Id
    @Column(name = "pk", nullable = false)
    String pk;

    @Column(name = "entity_group", nullable = false)
    String entityGroup;

    @Column(name = "entity", nullable = false)
    String entity;

    @Column(name = "sk", nullable = false)
    String sk;

    @Column(name = "next_sk")
    String nextSk;

    @Type(JsonType.class)
    @Column(name = "record", columnDefinition = "jsonb")
    Record record;

    @Column(name = "records")
    byte[] records;

    @Column(name = "upd_version_id", nullable = false)
    Long updVersionId;

    @Column(name = "upd_tsp")
    Timestamp updTsp;

    @Column(name = "user_src_id")
    String userSrcId;

    @Column(name = "index_bitmap")
    Long indexBitmap;

}
