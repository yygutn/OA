package cn.edu.jumy.oa.Utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.edu.jumy.jumyframework.BaseActivity;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/7/18  上午11:19
 */
public class StringUtils {
    /**
     * Get XML String of utf-8
     *
     * @return XML-Formed string
     */
    public static String getUTF8XMLString(String xml) {
        // A StringBuffer Object
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(xml);
        String xmString;
        String xmlUTF8="";
        try {
            xmString = new String(stringBuffer.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            BaseActivity.showDebugLogd("utf-8 编码：" + xmlUTF8);
        } catch (UnsupportedEncodingException e) {
            BaseActivity.showDebugException(e);
        }
        // return to String Formed
        return xmlUTF8;
    }
}
