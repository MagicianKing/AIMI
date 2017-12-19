/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yeying.aimi.huanxin;

import android.content.Context;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.yeying.aimi.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SmileUtils {
    public static final String face_1 = "[):]";
    public static final String face_2 = "[:D]";
    public static final String face_3 = "[;)]";
    public static final String face_4 = "[:-o]";
    public static final String face_5 = "[:p]";
    public static final String face_6 = "[(H)]";
    public static final String face_7 = "[:@]";
    public static final String face_8 = "[:s]";
    public static final String face_9 = "[:$]";
    public static final String face_10 = "[:(]";
    public static final String face_11 = "[:'(]";
    public static final String face_12 = "[:|]";
    public static final String face_13 = "[(a)]";
    public static final String face_14 = "[8o|]";
    public static final String face_15 = "[8-|]";
    public static final String face_16 = "[+o(]";
    public static final String face_17 = "[<o)]";
    public static final String face_18 = "[|-)]";
    public static final String face_19 = "[*-)]";
    public static final String face_20 = "[:-#]";
    public static final String face_21 = "[:-*]";
    public static final String face_22 = "[^o)]";
    public static final String face_23 = "[8-)]";
    public static final String face_24 = "[(|)]";
    public static final String face_25 = "[(u)]";
    public static final String face_26 = "[(S)]";
    public static final String face_27 = "[(*)]";
    public static final String face_28 = "[(#)]";
    public static final String face_29 = "[(R)]";
    public static final String face_30 = "[({)]";
    public static final String face_31 = "[(})]";
    public static final String face_32 = "[(k)]";
    public static final String face_33 = "[(F)]";
    public static final String face_34 = "[(W)]";
    public static final String face_35 = "[(D)]";

    private static final Factory spannableFactory = Factory
            .getInstance();

    private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

    static {

        addPattern(emoticons, face_1, R.drawable.face_1);
        addPattern(emoticons, face_2, R.drawable.face_2);
        addPattern(emoticons, face_3, R.drawable.face_3);
        addPattern(emoticons, face_4, R.drawable.face_4);
        addPattern(emoticons, face_5, R.drawable.face_5);
        addPattern(emoticons, face_6, R.drawable.face_6);
        addPattern(emoticons, face_7, R.drawable.face_7);
        addPattern(emoticons, face_8, R.drawable.face_8);
        addPattern(emoticons, face_9, R.drawable.face_9);
        addPattern(emoticons, face_10, R.drawable.face_10);
        addPattern(emoticons, face_11, R.drawable.face_11);
        addPattern(emoticons, face_12, R.drawable.face_12);
        addPattern(emoticons, face_13, R.drawable.face_13);
        addPattern(emoticons, face_14, R.drawable.face_14);
        addPattern(emoticons, face_15, R.drawable.face_15);
        addPattern(emoticons, face_16, R.drawable.face_16);
        addPattern(emoticons, face_17, R.drawable.face_17);
        addPattern(emoticons, face_18, R.drawable.face_18);
        addPattern(emoticons, face_19, R.drawable.face_19);
        addPattern(emoticons, face_20, R.drawable.face_20);
        addPattern(emoticons, face_21, R.drawable.face_21);
        addPattern(emoticons, face_22, R.drawable.face_22);
        addPattern(emoticons, face_23, R.drawable.face_23);
        addPattern(emoticons, face_24, R.drawable.face_24);
        addPattern(emoticons, face_25, R.drawable.face_25);
        addPattern(emoticons, face_26, R.drawable.face_26);
        addPattern(emoticons, face_27, R.drawable.face_27);
        addPattern(emoticons, face_28, R.drawable.face_28);
        addPattern(emoticons, face_29, R.drawable.face_29);
        addPattern(emoticons, face_30, R.drawable.face_30);
        addPattern(emoticons, face_31, R.drawable.face_31);
        addPattern(emoticons, face_32, R.drawable.face_32);
        addPattern(emoticons, face_33, R.drawable.face_33);
        addPattern(emoticons, face_34, R.drawable.face_34);
        addPattern(emoticons, face_35, R.drawable.face_35);
    }

    private static void addPattern(Map<Pattern, Integer> map, String smile,
                                   int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    /**
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    spannable.setSpan(new ImageSpan(context, entry.getValue()),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }


}
