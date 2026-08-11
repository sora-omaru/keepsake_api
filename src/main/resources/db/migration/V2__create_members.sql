CREATE TABLE member
(
    id           BIGSERIAL PRIMARY KEY,
    workspace_id BIGINT       NOT NULL,
    name         VARCHAR(100) NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_member_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspace (id)
            ON DELETE RESTRICT,

    CONSTRAINT uq_member_workspace_id
        UNIQUE (workspace_id, id),

    CONSTRAINT uq_member_workspace_name
        UNIQUE (workspace_id, name)
);
