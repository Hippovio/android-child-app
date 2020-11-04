package com.hippovio.child.services.messageRead.helpers;

import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class AccessibilityHelper {

    public static AccessibilityNodeInfo getNodeInfo(AccessibilityNodeInfo nodeInfo, String viewId) {
        AccessibilityNodeInfo node = null;

        List<AccessibilityNodeInfo> nodes = nodeInfo
                .findAccessibilityNodeInfosByViewId(viewId);

        if (nodes.size() > 0)
            node = nodes.get(0);

        return node;
    }
}
