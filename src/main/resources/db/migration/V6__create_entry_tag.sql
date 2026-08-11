CREATE TABLE entry_tag
(
    workspace_id BIGINT NOT NULL,
    entry_id     BIGINT NOT NULL,
    tag_id       BIGINT NOT NULL,

    CONSTRAINT pk_entry_tag
        PRIMARY KEY (entry_id, tag_id),

    CONSTRAINT fk_entry_tag_entry
        FOREIGN KEY (workspace_id, entry_id)
            REFERENCES entry (workspace_id, id)
            ON DELETE CASCADE,

    CONSTRAINT fk_entry_tag_tag
        FOREIGN KEY (workspace_id, tag_id)
            REFERENCES tag (workspace_id, id)
            ON DELETE CASCADE
);

CREATE INDEX idx_entry_tag_tag_id
    ON entry_tag (tag_id);
