package daniel.yyf.er017.december;


import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author danielyang
 * @Date 2017/11/8 14:06
 * 腾讯AI平台签名算法实现
 * https://ai.qq.com/doc/auth.shtml
 */
public class TencentAISign {

    public static void main(String[] args) {
        Map<String, String> para = new HashMap<>();
        para.put("text", "今天深圳的天气怎么样？明天呢");
        para.put("app_id", "1106526378");
        para.put("time_stamp", String.valueOf(System.currentTimeMillis() / 1000));
        para.put("nonce_str", "20e3408a79");
        String re = getSign(para, "iBW0zt0yRgfKOsOq");
        System.out.println(re);
        para.put("sign", re);
        String url = "https://api.ai.qq" +
                ".com/fcgi-bin/nlp/nlp_wordcom?" + getParaCompact(para);
        System.out.println(url);
    }

    public static String getSign(Map<String, String> para, String appKey) {
        String tem = getParaCompact(para);
        String toSign = tem + "&app_key=" + appKey;
        Optional<String> optional = Optional.of(md5(toSign));
        return optional.orElse(null).toUpperCase();
    }

    public static String getParaCompact(Map<String, String> para) {
        StringBuilder builder = new StringBuilder();
        List<String> keys = new ArrayList<>();
        keys.addAll(para.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            try {
                builder.append("&").append(key).append("=").append(URLEncoder.encode(para.get(key), "utf8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return builder.toString().substring(1);
    }

    public static String md5(String message) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            return base64en.encode(md5.digest(message.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
