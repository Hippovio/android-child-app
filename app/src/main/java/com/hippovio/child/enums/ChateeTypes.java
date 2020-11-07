package com.hippovio.child.enums;

/**
 * Author: Raghav Agarwal
 * Enum values for sources and package names.
 */
public enum ChateeTypes {
    INDIVIDUAL("Individual"),
    GROUP("Group");

    private String type;

    ChateeTypes(String type) {
        this.type = type;
    }

    public String value() {
        return type;
    }

    /**
     * Reverse mapping by packageName
     * @param sourceName
     * @return Source Enum
     */
    public static ChateeTypes getByValue(String sourceName)
    {
        for(ChateeTypes source : ChateeTypes.values())
        {
            if (source.value().equals(sourceName)) {
                return source;
            }
        }

        return null;
    }
}
