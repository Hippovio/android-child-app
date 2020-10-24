package com.hippovio.screenread;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.hippovio.whatsapp.service.WhatsAppReadService;

public class ScreenReadService extends AccessibilityService {

    AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    private WhatsAppReadService whatsAppService = new WhatsAppReadService();
    private String whatsappPackageName = "com.whatsapp";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        AccessibilityNodeInfo source = accessibilityEvent.getSource();
        if (source == null)
            return;

        AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap (getRootInActiveWindow ());

        if (rootInActiveWindow == null) {
            return;
        }

        if (whatsappPackageName.equals(accessibilityEvent.getPackageName())) {
            whatsAppService.whatsAppEvent(accessibilityEvent, rootInActiveWindow);
        }

//        Log.i(TAG + "Source:", sourceClass);
//
//        Log.i(TAG ,"Child count " + source.getChildCount());
//        Log.i(TAG + "Window Id:", source.getWindowId() + "");
//        if (source.getChildCount() > 0) {
//            for(int j = 0; j < source.getChildCount(); j++){
//                AccessibilityNodeInfo nodeInfo = source.getChild(j);
//                if(nodeInfo != null) {
//                    String msg = "";
//                    for (int i = 0; i < nodeInfo.getChildCount(); i++) {
//                        AccessibilityNodeInfo node = nodeInfo.getChild(i);
//                        if (node != null && node.getText() != null) {
//                            msg += node.getText() + "\t";
//                        }
//                    }
//                    Log.i(TAG + "Message", msg);
//                }
//            }
//        }

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i("Accessibility", "onServiceConnected: ");
        // Set the type of events that this service wants to listen to. Others
        // won't be passed to this service.
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK | AccessibilityEvent.TYPE_VIEW_SCROLLED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

        info.notificationTimeout = 0;
        info.flags = AccessibilityServiceInfo.DEFAULT;


        // If you only want this service to work with specific applications, set their
        // package names here. Otherwise, when the service is activated, it will listen
        // to events from all applications.
        info.packageNames = new String[]
                {whatsappPackageName, "com.facebook.orca"};

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
