package com.omaru.keepsake_api.dto;

public interface EntryTagProjection {
    Long getEntryId();

    Long getTagId();

    String getTagName();

    Long getWorkspaceId();
}
