package com.example.inote.view;

import android.net.Uri;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import org.apache.http.message.TokenParser;

public class SpanUtils {

    public static String m32643f(Spanned spanned) {
        StringBuilder sb = new StringBuilder();
        m32648k(sb, spanned);
        return sb.toString();
    }

    /* renamed from: k */
    private static void m32646i(StringBuilder sb, Spanned spanned, int i, int i2) {
        sb.append("<p dir=\"ltr\">");
        int i3 = i;
        while (i3 < i2) {
            int indexOf = TextUtils.indexOf(spanned, "\n", i3, i2);
            if (indexOf < 0) {
                indexOf = i2;
            }
            int i4 = 0;
            while (indexOf < i2 && spanned.charAt(indexOf) == 10) {
                i4++;
                indexOf++;
            }
            m32649l(sb, spanned, i3, indexOf - i4, i4, indexOf == i2);
            i3 = indexOf;
        }
        sb.append("</p>\n");
    }
    private static void m32647j(StringBuilder sb, Spanned spanned, int i, int i2) {
        while (i < i2) {
            int nextSpanTransition = spanned.nextSpanTransition(i, i2, QuoteSpan.class);
            QuoteSpan[] quoteSpanArr = (QuoteSpan[]) spanned.getSpans(i, nextSpanTransition, QuoteSpan.class);
            for (QuoteSpan quoteSpan : quoteSpanArr) {
                sb.append("<blockquote>");
            }
            m32646i(sb, spanned, i, nextSpanTransition);
            for (QuoteSpan quoteSpan2 : quoteSpanArr) {
                sb.append("</blockquote>\n");
            }
            i = nextSpanTransition;
        }
    }
    private static void m32648k(StringBuilder sb, Spanned spanned) {
        int length = spanned.length();
        int i = 0;
        while (i < spanned.length()) {
            int nextSpanTransition = spanned.nextSpanTransition(i, length, ParagraphStyle.class);
            ParagraphStyle[] paragraphStyleArr = (ParagraphStyle[]) spanned.getSpans(i, nextSpanTransition, ParagraphStyle.class);
            String str = " ";
            boolean z = false;
            for (int i2 = 0; i2 < paragraphStyleArr.length; i2++) {
                if (paragraphStyleArr[i2] instanceof AlignmentSpan) {
                    Layout.Alignment alignment = ((AlignmentSpan) paragraphStyleArr[i2]).getAlignment();
                    if (alignment == Layout.Alignment.ALIGN_CENTER) {
                        str = "align=\"center\" " + str;
                    } else if (alignment == Layout.Alignment.ALIGN_OPPOSITE) {
                        str = "align=\"right\" " + str;
                    } else {
                        str = "align=\"left\" " + str;
                    }
                    z = true;
                }
            }
            if (z) {
                sb.append("<div ");
                sb.append(str);
                sb.append(">");
            }
            m32647j(sb, spanned, i, nextSpanTransition);
            if (z) {
                sb.append("</div>");
            }
            i = nextSpanTransition;
        }
    }
    private static void m32649l(StringBuilder sb, Spanned spanned, int i, int i2, int i3, boolean z) {
        int i4;
        String str = null;
        int i5 = i;
        while (true) {
            i4 = 0;
            if (i5 >= i2) {
                break;
            }
            int nextSpanTransition = spanned.nextSpanTransition(i5, i2, CharacterStyle.class);
            CharacterStyle[] characterStyleArr = (CharacterStyle[]) spanned.getSpans(i5, nextSpanTransition, CharacterStyle.class);
            while (i4 < characterStyleArr.length) {
                if (characterStyleArr[i4] instanceof StyleSpan) {
                    int style = ((StyleSpan) characterStyleArr[i4]).getStyle();
                    if ((style & 1) != 0) {
                        sb.append("<b>");
                    }
                    if ((style & 2) != 0) {
                        sb.append("<i>");
                    }
                }
                if ((characterStyleArr[i4] instanceof TypefaceSpan) && ((TypefaceSpan) characterStyleArr[i4]).getFamily().equals("monospace")) {
                    sb.append("<tt>");
                }
                if (characterStyleArr[i4] instanceof SuperscriptSpan) {
                    sb.append("<sup>");
                }
                if (characterStyleArr[i4] instanceof SubscriptSpan) {
                    sb.append("<sub>");
                }
                if (characterStyleArr[i4] instanceof UnderlineSpan) {
                    sb.append("<u>");
                }
                if (characterStyleArr[i4] instanceof StrikethroughSpan) {
                    sb.append("<strike>");
                }
                if (characterStyleArr[i4] instanceof URLSpan) {
                    sb.append("<a href=\"");
                    sb.append(((URLSpan) characterStyleArr[i4]).getURL());
                    sb.append("\">");
                }
                if (characterStyleArr[i4] instanceof ImageSpan) {
                    if (characterStyleArr[i4] instanceof ImageSpanView) {
                        sb.append("<img src=\"");
                        sb.append(((ImageSpanView) characterStyleArr[i4]).getId());
                        sb.append("\">");
                    } else {
                        sb.append("<img src=\"");
                        sb.append(Uri.parse(((ImageSpan) characterStyleArr[i4]).getSource()).getPath());
                        sb.append("\">");
                    }
                    i5 = nextSpanTransition;
                }
                if (characterStyleArr[i4] instanceof AbsoluteSizeSpan) {
                    sb.append("<font size =\"");
                    sb.append(((AbsoluteSizeSpan) characterStyleArr[i4]).getSize() / 6);
                    sb.append("\">");
                }
                if (characterStyleArr[i4] instanceof ForegroundColorSpan) {
                    sb.append("<font color =\"#");
                    String hexString = Integer.toHexString(((ForegroundColorSpan) characterStyleArr[i4]).getForegroundColor() + 16777216);
                    while (hexString.length() < 6) {
                        hexString = "0" + hexString;
                    }
                    sb.append(hexString);
                    sb.append("\">");
                }
                if (characterStyleArr[i4] instanceof StrikethroughSpan) {
                    sb.append("<del>");
                }
//                if (characterStyleArr[i4] instanceof BackgroundColorSpan) {
//                    sb.append("<span style =\"background-color: ");
//                    sb.append(m32638a(((BackgroundColorSpan) characterStyleArr[i4]).getBackgroundColor()));
//                    sb.append("\">");
//                }
                i4++;
            }
            m32650m(sb, spanned, i5, nextSpanTransition);
            for (int length = characterStyleArr.length - 1; length >= 0; length--) {
                if (characterStyleArr[length] instanceof ForegroundColorSpan) {
                    sb.append("</font>");
                }
                if (characterStyleArr[length] instanceof AbsoluteSizeSpan) {
                    sb.append("</font>");
                }
                if (characterStyleArr[length] instanceof URLSpan) {
                    sb.append("</a>");
                }
                if (characterStyleArr[length] instanceof StrikethroughSpan) {
                    sb.append("</strike>");
                }
                if (characterStyleArr[length] instanceof UnderlineSpan) {
                    sb.append("</u>");
                }
                if (characterStyleArr[length] instanceof SubscriptSpan) {
                    sb.append("</sub>");
                }
                if (characterStyleArr[length] instanceof SuperscriptSpan) {
                    sb.append("</sup>");
                }
                if ((characterStyleArr[length] instanceof TypefaceSpan) && ((TypefaceSpan) characterStyleArr[length]).getFamily().equals("monospace")) {
                    sb.append("</tt>");
                }
                if (characterStyleArr[length] instanceof StyleSpan) {
                    int style2 = ((StyleSpan) characterStyleArr[length]).getStyle();
                    if ((style2 & 1) != 0) {
                        sb.append("</b>");
                    }
                    if ((style2 & 2) != 0) {
                        sb.append("</i>");
                    }
                }
                if (characterStyleArr[length] instanceof StrikethroughSpan) {
                    sb.append("</del>");
                }
                if (characterStyleArr[length] instanceof BackgroundColorSpan) {
                    sb.append("</span>");
                }
            }
            i5 = nextSpanTransition;
        }
        if (z) {
            str = "";
        } else {
//            str = "</p>\n" + m32641d(spanned, i, i2);
        }
        if (i3 == 1) {
            sb.append("<br>\n");
        } else if (i3 == 2) {
//            sb.append(str);
        } else {
            while (i4 < i3) {
                sb.append("<br>");
                i4++;
            }
//            sb.append(str);
        }
        ShareUtils.setStr("aaaa",sb.toString());

    }
    private static void m32650m(StringBuilder sb, CharSequence charSequence, int i, int i2) {
        int i3;
        char charAt;
        while (i < i2) {
            char charAt2 = charSequence.charAt(i);
            if (charAt2 == '<') {
                sb.append("&lt;");
            } else if (charAt2 == '>') {
                sb.append("&gt;");
            } else if (charAt2 == '&') {
                sb.append("&amp;");
            } else if (charAt2 < 55296 || charAt2 > 57343) {
                if (charAt2 > '~' || charAt2 < ' ') {
                    sb.append("&#");
                    sb.append(charAt2);
                    sb.append(";");
                } else if (charAt2 == ' ') {
                    while (true) {
                        int i4 = i + 1;
                        if (i4 >= i2 || charSequence.charAt(i4) != ' ') {
                            sb.append(TokenParser.SP);
                        } else {
                            sb.append("&nbsp;");
                            i = i4;
                        }
                    }
                } else {
                    sb.append(charAt2);
                }
            } else if (charAt2 < 56320 && (i3 = i + 1) < i2 && (charAt = charSequence.charAt(i3)) >= 56320 && charAt <= 57343) {
                sb.append("&#");
                sb.append(65536 | ((charAt2 - 55296) << 10) | (charAt - 56320));
                sb.append(";");
                i = i3;
            }
            i++;
        }
    }


}
