package io.github.kmenager.getmesomefeed.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


public class ParseHTML {

    public static String parseToSimpleText(String content) {
        return Jsoup.clean(content, Whitelist.simpleText());
    }
}
