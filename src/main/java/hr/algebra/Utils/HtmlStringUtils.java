package hr.algebra.Utils;

public class HtmlStringUtils
{
    private  HtmlStringUtils(){}

    public static void wrapInTags(String startTag, String text, String endTag,StringBuilder sb) {
        sb.append(startTag);
        sb.append(text);
        sb.append(endTag);
    }

}
