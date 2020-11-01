package com.hippovio.child.services.messageRead.helpers;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class AccessibilityHelper {

    public static AccessibilityNodeInfoCompat getNodeInfo(AccessibilityNodeInfoCompat rootInActiveWindow, String viewId) {
        AccessibilityNodeInfoCompat node = null;

        List<AccessibilityNodeInfoCompat> nodes = rootInActiveWindow
                .findAccessibilityNodeInfosByViewId(viewId);

        if (nodes.size() > 0)
            node = nodes.get(0);

        return node;
    }
}
