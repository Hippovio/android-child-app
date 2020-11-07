package com.hippovio.child.services.messageRead;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.hippovio.child.enums.PackageName;
import com.hippovio.child.whatsapp.service.WhatsAppReadService;

public class ScreenReadService extends AccessibilityService {

    AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    WhatsAppReadService whatsAppService = new WhatsAppReadService();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        AccessibilityNodeInfo source = accessibilityEvent.getSource();
        if (source == null)
            return;

        AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow ());

        if (rootInActiveWindow == null) {
            return;
        }

        MessageReadService messageReadService = null;
        switch (PackageName.getByValue(accessibilityEvent.getPackageName().toString())) {
            case WHATSAPP: messageReadService = whatsAppService; break;
            default: break;
        }

        if (messageReadService != null)
            messageReadService.appEvent(accessibilityEvent, rootInActiveWindow, this);
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

        // If you only want this service to work with specific applications, set their
        // package names here. Otherwise, when the service is activated, it will listen
        // to events from all applications.
        info.packageNames = new String[]{PackageName.WHATSAPP.value(), PackageName.FACEBOOK.value()};


        // Set the type of feedback your service will provide.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        // Default services are invoked only if no package-specific ones are present
        // for the type of AccessibilityEvent generated. This service *is*
        // application-specific, so the flag isn't necessary. If this was a
        // general-purpose service, it would be worth considering setting the
        // DEFAULT flag.
        info.flags = AccessibilityServiceInfo.DEFAULT;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }
}
