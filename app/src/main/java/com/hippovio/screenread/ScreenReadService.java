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
//        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("YOUR PACKAGE NAME:id/RESOURCE ID FROM WHERE YOU WANT DATA");
//        if (findAccessibilityNodeInfosByViewId.size() > 0) {
//            AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
//            // You can also traverse the list if required data is deep in view hierarchy.
//            String requiredText = parent.getText().toString();
//            Log.i("Required Text", requiredText);
//        }

        Log.i(TAG + "Event", accessibilityEvent.toString() + "");
        Log.i(TAG + "Source", source.toString() + "");

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
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED;

        // If you only want this service to work with specific applications, set their
        // package names here. Otherwise, when the service is activated, it will listen
        // to events from all applications.
        info.packageNames = new String[]
                {"com.example.android.myFirstApp", "com.example.android.mySecondApp"};

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


//    public String getEventNameFromId(Integer eventID){
//        switch (eventID){
//            case 4:
//                return "CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION";
//            case  6:
//                return "CONTENT_CHANGE_TYPE_PANE_APPEARED";
//            case  2:
//                return "CONTENT_CHANGE_TYPE_PANE_DISAPPEARED";
//            case 8:
//                return "CONTENT_CHANGE_TYPE_PANE_TITLE";
//            case 1:
//                return "CONTENT_CHANGE_TYPE_SUBTREE";
//            case 2:
//                return "CONTENT_CHANGE_TYPE_TEXT";
//            case 0:
//                return "CONTENT_CHANGE_TYPE_UNDEFINED";
//            case  1:
//                return "INVALID_POSITION";
//            case  50:
//                return "MAX_TEXT_LENGTH";
//            case  1:
//                return "TYPES_ALL_MASK";
//            case  1634:
//                return "TYPE_ANNOUNCEMENT";
//            case  1677726:
//                return "TYPE_ASSIST_READING_CONTEXT";
//            case  52428:
//                return "TYPE_GESTURE_DETECTION_END";
//            case  26214:
//                return "TYPE_GESTURE_DETECTION_START";
//            case  4:
//                return "TYPE_NOTIFICATION_STATE_CHANGED";
//            case  104:
//                return "TYPE_TOUCH_EXPLORATION_GESTURE_END";
//            case  52:
//                return "TYPE_TOUCH_EXPLORATION_GESTURE_START";
//            case  209712:
//                return "TYPE_TOUCH_INTERACTION_END";
//            case  104856:
//                return "TYPE_TOUCH_INTERACTION_START";
//            case  3278:
//                return "TYPE_VIEW_ACCESSIBILITY_FOCUSED";
//            case 6556:
//                return "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED";
//            case 1:
//                return "TYPE_VIEW_CLICKED";
//            case  838868:
//                return "TYPE_VIEW_CONTEXT_CLICKED";
//            case 8:
//                return "TYPE_VIEW_FOCUSED";
//            case  18:
//                return "TYPE_VIEW_HOVER_ENTER";
//            case  26:
//                return "TYPE_VIEW_HOVER_EXIT";
//            case 2:
//                return "TYPE_VIEW_LONG_CLICKED";
//            case  406:
//                return "TYPE_VIEW_SCROLLED";
//            case 4:
//                return "TYPE_VIEW_SELECTED";
//            case  6:
//                return "TYPE_VIEW_TEXT_CHANGED";
//            case  812:
//                return "TYPE_VIEW_TEXT_SELECTION_CHANGED";
//            case  13102:
//                return "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY";
//            case  419434:
//                return "TYPE_WINDOWS_CHANGED";
//            case  208:
//                return "TYPE_WINDOW_CONTENT_CHANGED";
//            case  2:
//                return "TYPE_WINDOW_STATE_CHANGED";
//            case  18:
//                return "WINDOWS_CHANGE_ACCESSIBILITY_FOCUSED";
//            case  2:
//                return "WINDOWS_CHANGE_ACTIVE";
//            case 1:
//                return "WINDOWS_CHANGE_ADDED";
//            case 8:
//                return "WINDOWS_CHANGE_BOUNDS";
//            case  52:
//                return "WINDOWS_CHANGE_CHILDREN";
//            case  4:
//                return "WINDOWS_CHANGE_FOCUSED";
//            case  6:
//                return "WINDOWS_CHANGE_LAYER";
//            case  26:
//                return "WINDOWS_CHANGE_PARENT";
//            case  104:
//                return "WINDOWS_CHANGE_PIP";
//            case 2:
//                return "WINDOWS_CHANGE_REMOVED";
//            case 4:
//                return "WINDOWS_CHANGE_TITLE";
//            default:
//        }
//    }
}
