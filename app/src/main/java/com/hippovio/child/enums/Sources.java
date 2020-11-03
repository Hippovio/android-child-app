package com.hippovio.child.enums;

/**
 * Author: Raghav Agarwal
 * Enum values for sources and package names.
 */
public enum Sources {
    WHATSAPP("WHATSAPP"),
    FACEBOOK("FACEBOOK"),
    INSTAGRAM("INSTAGRAM");

    private String source;

    Sources(String source) {
        this.source = source;
    }

    public String value() {
        return source;
    }

    /**
     * Reverse mapping by packageName
     * @param sourceName
     * @return Source Enum
     */
    public static Sources getByValue(String sourceName)
    {
        for(Sources source : Sources.values())
        {
            if (source.value().equals(sourceName)) {
                return source;
            }
        }

        return null;
    }
}
