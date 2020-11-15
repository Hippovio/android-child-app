package com.hippovio.child.hashing;

import com.hippovio.child.pojos.Message;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class HashingService {

    public static String computeHash(Message message){

        List<String> hashEntries = new ArrayList<>();
        //hashEntries.add(message.getChatee().getChateeId().toString());
        hashEntries.add(message.getTimeInDayMillies().toString());
        hashEntries.add(message.getMsg());
        hashEntries.add(message.getIsReceived().toString());
        return StringUtils.join(hashEntries, "-");
    }

    public static String findChateeIdFromHash(String hash){
        return hash.split("-")[0];
    }

    public static String findMessageTimeFromHash(String hash){
        return hash.split("-")[1];
    }

    public static String findMessageTextFromHash(String hash){
        return hash.split("-")[2];
    }

    public static boolean checkHashMatches(Message message, String hash){
        return computeHash(message).equals(hash);
    }

}
