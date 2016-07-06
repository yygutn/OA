package cn.edu.jumy.oa.pinyin;


import android.text.TextUtils;

import com.ta.utdid2.android.utils.StringUtils;

import java.util.Comparator;

/**
 * @author Jumy
 */
public class PinyinComparator implements Comparator<String> {

    public int compare(String o1, String o2) {
        if (o1.toLowerCase().equals("@")
                || o2.toLowerCase().equals("#")) {
            return -1;
        } else if (o1.toLowerCase().equals("#")
                || o2.toLowerCase().equals("@")) {
            return 1;
        } else {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    }

}
