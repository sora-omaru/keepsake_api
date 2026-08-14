package com.omaru.keepsake_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

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
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(
                    formula = @JoinFormula(
                            value = "workspace_id",
                            referencedColumnName = "workspace_id"
                    )
            ),
            @JoinColumnOrFormula(
                    column = @JoinColumn(
                            name = "topic_id",
                            referencedColumnName = "id"
                    )
            )
    })
    private TopicEntity topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(
                    formula = @JoinFormula(
                            value = "workspace_id",
                            referencedColumnName = "workspace_id"
                    )
            ),
            @JoinColumnOrFormula(
                    column = @JoinColumn(
                            name = "member_id",
                            referencedColumnName = "id"
                    )
            )
    })
    private MemberEntity member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String content;


}
