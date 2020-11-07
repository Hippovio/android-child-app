package com.hippovio.child.enums;

public enum MessageSyncStates {
    NEW("NEW"),
    UPLOADED("UPLOADED"),
    COMPLETE("COMPLETE");

    MessageSyncStates(String syncState) {
        this.syncState = syncState;
    }

    private String syncState;

    public String getSyncState() {
        return syncState;
    }

    public void setSyncState(String syncState) {
        this.syncState = syncState;
    }

    public String value() {
        return syncState;
    }

    /**
     * Reverse mapping by syncState
     * @param syncState
     * @return SyncState Enum
     */
    public static MessageSyncStates getByValue(String syncState)
    {
        for(MessageSyncStates source : MessageSyncStates.values())
        {
            if (source.value().equals(syncState)) {
                return source;
            }
        }

        return null;
    }
}
