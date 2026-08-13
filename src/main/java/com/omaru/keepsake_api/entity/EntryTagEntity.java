package com.omaru.keepsake_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "entry_tag")
public class EntryTagEntity {
    @EmbeddedId
    private EntryTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    @MapsId("entryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(
                    name = "workspace_id",
                    referencedColumnName = "workspace_id",
                    insertable = false,
                    updatable = false
            ),
            @JoinColumn(
                    name = "entry_id",
                    referencedColumnName = "id"
            )
    })
    private EntryEntity entry;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(
                    name = "workspace_id",
                    referencedColumnName = "workspace_id",
                    insertable = false,
                    updatable = false
            ),
            @JoinColumn(
                    name = "tag_id",
                    referencedColumnName = "id"
            )
    })
    private TagEntity tag;

}
