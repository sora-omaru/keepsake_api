package com.omaru.keepsake_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class EntryTagId implements Serializable {

    @Column(name = "entry_id")
    private Long entryId;

    @Column(name = "tag_id")
    private Long tagId;
}
