package com.hippovio.entities.enums;

/**
 * Author: Raghav Agarwal
 * Enum values for sources and package names.
 */
public enum PackageName {
    WHATSAPP("com.whatsapp");

    private String packageName;

    PackageName(String packageName) {
        this.packageName = packageName;
    }

    public String value() {
        return packageName;
    }

    /**
     * Reverse mapping by packageName
     * @param packageName
     * @return PackageName Enum
     */
    public static PackageName getByValue(String packageName)
    {
        for(PackageName source : PackageName.values())
        {
            if (source.value().equals(packageName)) {
                return source;
            }
        }

        return null;
    }
}
