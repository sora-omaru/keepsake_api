package com.omaru.keepsake_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "entry")
public class EntryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(
                    name = "workspace_id",
                    referencedColumnName = "workspace_id",
                    insertable = false,
                    updatable = false
            ),
            @JoinColumn(
                    name = "topic_id",
                    referencedColumnName = "id",
                    insertable = false,
                    updatable = false
            )
    })
    private TopicEntity topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(
                    name = "workspace_id",
                    referencedColumnName = "workspace_id",
                    insertable = false,
                    updatable = false
            ),
            @JoinColumn(
                    name = "member_id",
                    referencedColumnName = "id",
                    insertable = false,
                    updatable = false
            )
    })
    private MemberEntity member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String content;


}
