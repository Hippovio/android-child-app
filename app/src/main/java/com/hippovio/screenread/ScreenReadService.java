package com.hippovio.screenread;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import java.util.List;

public class ScreenReadService extends AccessibilityService {

    AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    private String TAG = "Hippovio";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.i(TAG, "test");

        AccessibilityNodeInfo source = accessibilityEvent.getSource();
        if (source == null) {
            return;
        }

        Log.i(TAG ,"source");
        Log.i(TAG , source.getClassName().toString());
        Log.i(TAG , source.getText() == null ? "false" : "true");
        if(source.getText() != null){
            Log.i(TAG , source.getText().toString());
        }
        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("com.whatsapp");
        Log.i(TAG ,"source child count " + findAccessibilityNodeInfosByViewId.size());

        if (findAccessibilityNodeInfosByViewId.size() > 0) {
            for(AccessibilityNodeInfo nodeInfo : findAccessibilityNodeInfosByViewId){
                //AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                // You can also traverse the list if required data is deep in view hierarchy.
                Log.i(TAG ,"source child");
                Log.i(TAG , nodeInfo.getClassName().toString());
                Log.i(TAG , nodeInfo.getText() == null ? "false" : "true");
                if(nodeInfo.getText() != null){
                    Log.i(TAG , nodeInfo.getText().toString());
                }
            }
        }

        for(int k = 0; k < source.getChildCount(); k++){
            AccessibilityNodeInfo viewParentNode = source.getChild(k);
            if(viewParentNode != null) {
                if (viewParentNode.getClassName().equals("android.widget.ListView")) {
                    for (int i = 0; i < viewParentNode.getChildCount(); i++) {
                        AccessibilityNodeInfo listViewItemNode = viewParentNode.getChild(i);
                        if (listViewItemNode != null) {
                            for (int j = 0; j < listViewItemNode.getChildCount(); j++) {
                                try {
                                    AccessibilityNodeInfo listItemViewGroupChildNode = listViewItemNode.getChild(j);
                                    if (listItemViewGroupChildNode.getClassName().equals("android.widget.TextView")) {
                                        Log.i(TAG + " data", listItemViewGroupChildNode.getText().toString());
                                    }
                                } catch (Exception e) {

                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "onServiceConnected: ");
        // Set the type of events that this service wants to listen to. Others
        // won't be passed to this service.
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK | AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED | AccessibilityEvent.TYPE_WINDOWS_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;

        info.notificationTimeout = 0;
        info.flags = AccessibilityServiceInfo.DEFAULT;


        // If you only want this service to work with specific applications, set their
        // package names here. Otherwise, when the service is activated, it will listen
        // to events from all applications.
        info.packageNames = new String[]
                {"com.whatsapp", "com.facebook.orca"};

        // Set the type of feedback your service will provide.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        // Default services are invoked only if no package-specific ones are present
        // for the type of AccessibilityEvent generated. This service *is*
        // application-specific, so the flag isn't necessary. If this was a
        // general-purpose service, it would be worth considering setting the
        // DEFAULT flag.

        // info.flags = AccessibilityServiceInfo.DEFAULT;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }
}
