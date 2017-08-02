package com.tomato.util;

import java.util.LinkedHashMap;
import java.util.Map;

public final class HanziUtil {

	private static final Map<Integer, String> SYMBOL = new LinkedHashMap<>();

	static {
		initSymbol();
	}

	/**
	 * GB2312全角字母、数字、符号转换成半角，返回转换后字符串
	 * 
	 * @param source
	 * @return
	 */
	public static String symbol2Ascii(String source) {
		if (source == null) {
			return "";
		}
		int len = source.length();
		StringBuilder sb = new StringBuilder(len);
		char ch;
		String symbolValue = null;
		for (int l = 0; l < len; l++) {
			ch = source.charAt(l);
			if (ch <= 128) {
				sb.append(ch);
			} else {
				if (ch == 0xA1A1) { // " " White space
					sb.append(' ');
					sb.append(' ');
				} else if (ch >= '\uff10' && ch <= '\uff19') {
					// "０" // "９"
					sb.append((char) (ch - 0xff10 + '0'));
				} else if (ch >= '\uff21' && ch <= '\uff3a') {
					// "Ａ" // "Ｚ"
					sb.append((char) (ch - 0xff21 + 'A'));
				} else if (ch >= '\uff41' && ch <= '\uff5a') {
					// "ａ" // "ｚ"
					sb.append((char) (ch - 0xff41 + 'a'));
				} else {
					symbolValue = SYMBOL.get((int) ch);
					if (symbolValue != null) {
						sb.append(symbolValue);
					} else {
						sb.append(ch);
					}
				}
			}
		}

		return sb.toString();
	}

	/**
	 * GB2312萃取代号(方括弧和花括弧全部替换为括弧，剔除空白字符，字母小写转换为大写)，返回转换后长度
	 * 
	 * @param source
	 * @param allowLeadingZeros
	 *            是否允许数字前导0
	 * @return
	 */
	public static String cleanCode(String source, boolean allowLeadingZeros) {
		source = symbol2Ascii(source);
		if (source.isEmpty()) {
			return "";
		}
		char ch;
		boolean lastDigit = allowLeadingZeros;
		boolean leadingZeros = false;
		int len = source.length();
		StringBuilder sb = new StringBuilder(len);
		for (int l = 0; l < len; l++) {
			ch = source.charAt(l);
			if (ch == '0') {
				if (lastDigit) {
					sb.append(ch);
				} else {
					leadingZeros = true;
				}
				continue;
			} else if (ch >= '1' && ch <= '9') {
				sb.append(ch);
				lastDigit = true;
				leadingZeros = false;
				continue;
			} else {
				lastDigit = allowLeadingZeros;
				if (leadingZeros) {
					sb.append('0');
					leadingZeros = false;
				}
			}

			if (ch >= 'a' && ch <= 'z') {
				sb.append((char) (ch + ('A' - 'a')));
			} else if (ch == '[' || ch == '{') {
				sb.append('(');
			} else if (ch == ']' || ch == '}') {
				sb.append(')');
			} else if (Character.isWhitespace(ch) || ch == '-') {
				continue;
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * GB2312萃取代号(方括弧和花括弧全部替换为括弧，剔除空白字符，字母小写转换为大写，数字剔除前导0)，返回转换后长度
	 * 
	 * @param source
	 * @return
	 */
	public static String cleanCode(String source) {
		return cleanCode(source, false);
	}

	private static void initSymbol() {
		SYMBOL.put(0xff01, "!"); // "！"
		SYMBOL.put(0xfe57, "!"); // "﹗"

		SYMBOL.put(0xff02, "\""); // "＂"
		SYMBOL.put(0x201c, "\""); // "“"
		SYMBOL.put(0x201d, "\""); // "”"
		SYMBOL.put(0x3003, "\""); // "〃"
		SYMBOL.put(0x2033, "\""); // "″"
		SYMBOL.put(0x301d, "\""); // "〝"
		SYMBOL.put(0x301e, "\""); // "〞"

		SYMBOL.put(0xff03, "#"); // "＃"
		SYMBOL.put(0xfe5f, "#"); // "﹟"

		SYMBOL.put(0xffe5, "$"); // "￥"
		SYMBOL.put(0xff04, "$"); // "＄"
		SYMBOL.put(0xfe69, "$"); // "﹩"

		SYMBOL.put(0xff05, "%"); // "％"
		SYMBOL.put(0xfe6a, "%"); // "﹪"

		SYMBOL.put(0xff06, "&"); // "＆"
		SYMBOL.put(0xfe60, "&"); // "﹠"

		SYMBOL.put(0xff07, "'"); // "＇"
		SYMBOL.put(0x2018, "'"); // "‘"
		SYMBOL.put(0x2019, "'"); // "’"
		SYMBOL.put(0x2032, "'"); // "′"

		SYMBOL.put(0xff08, "("); // "（"
		SYMBOL.put(0xfe59, "("); // "﹙"

		SYMBOL.put(0xff09, ")"); // "）"
		SYMBOL.put(0xfe5a, ")"); // "﹚"

		SYMBOL.put(0xff0a, "*"); // "＊"
		SYMBOL.put(0xfe61, "*"); // "﹡"
		SYMBOL.put(0x00d7, "*"); // "×"

		SYMBOL.put(0xff0b, "+"); // "＋"
		SYMBOL.put(0xfe62, "+"); // "﹢"

		SYMBOL.put(0xff0c, ","); // "，"
		SYMBOL.put(0xfe50, ","); // "﹐"
		SYMBOL.put(0x3001, ","); // "、"
		SYMBOL.put(0xfe51, ","); // "﹑"

		SYMBOL.put(0xff0d, "-"); // "－"
		SYMBOL.put(0xfe63, "-"); // "﹣"
		SYMBOL.put(0x2010, "-"); // "‐"
		SYMBOL.put(0x2013, "-"); // "–"
		SYMBOL.put(0x2015, "-"); // "―"
		SYMBOL.put(0x2014, "-"); // "—"

		SYMBOL.put(0xff0e, "."); // "．"
		SYMBOL.put(0xfe52, "."); // "﹒"
		SYMBOL.put(0x3002, "."); // "。"
		SYMBOL.put(0x00b7, "."); // "·"
		SYMBOL.put(0x2025, ".."); // "‥"
		SYMBOL.put(0x2026, ".."); // "…"

		SYMBOL.put(0xff0f, "/"); // "／"
		SYMBOL.put(0x2215, "/"); // "∕"
		SYMBOL.put(0x00f7, "/"); // "÷" Not reverse

		SYMBOL.put(0x3220, "1"); // "㈠"
		SYMBOL.put(0x3221, "2"); // "㈡"
		SYMBOL.put(0x3222, "3"); // "㈢"
		SYMBOL.put(0x3223, "4"); // "㈣"
		SYMBOL.put(0x3224, "5"); // "㈤"
		SYMBOL.put(0x3225, "6"); // "㈥"
		SYMBOL.put(0x3226, "7"); // "㈦"
		SYMBOL.put(0x3227, "8"); // "㈧"
		SYMBOL.put(0x3228, "9"); // "㈨"
		SYMBOL.put(0x3229, "10"); // "㈩"

		SYMBOL.put(0x2460, "1"); // "①"
		SYMBOL.put(0x2461, "2"); // "②"
		SYMBOL.put(0x2462, "3"); // "③"
		SYMBOL.put(0x2463, "4"); // "④"
		SYMBOL.put(0x2464, "5"); // "⑤"
		SYMBOL.put(0x2465, "6"); // "⑥"
		SYMBOL.put(0x2466, "7"); // "⑦"
		SYMBOL.put(0x2467, "8"); // "⑧"
		SYMBOL.put(0x2468, "9"); // "⑨"
		SYMBOL.put(0x2469, "10"); // "⑩"

		SYMBOL.put(0x2170, "1"); // "ⅰ"
		SYMBOL.put(0x2171, "2"); // "ⅱ"
		SYMBOL.put(0x2172, "3"); // "ⅲ"
		SYMBOL.put(0x2173, "4"); // "ⅳ"
		SYMBOL.put(0x2174, "5"); // "ⅴ"
		SYMBOL.put(0x2175, "6"); // "ⅵ"
		SYMBOL.put(0x2176, "7"); // "ⅶ"
		SYMBOL.put(0x2177, "8"); // "ⅷ"
		SYMBOL.put(0x2178, "9"); // "ⅸ"
		SYMBOL.put(0x2179, "10"); // "ⅹ"

		SYMBOL.put(0x2160, "1"); // "Ⅰ"
		SYMBOL.put(0x2161, "2"); // "Ⅱ"
		SYMBOL.put(0x2162, "3"); // "Ⅲ"
		SYMBOL.put(0x2163, "4"); // "Ⅳ"
		SYMBOL.put(0x2164, "5"); // "Ⅴ"
		SYMBOL.put(0x2165, "6"); // "Ⅵ"
		SYMBOL.put(0x2166, "7"); // "Ⅶ"
		SYMBOL.put(0x2167, "8"); // "Ⅷ"
		SYMBOL.put(0x2168, "9"); // "Ⅸ"
		SYMBOL.put(0x2169, "10"); // "Ⅹ"
		SYMBOL.put(0x216a, "11"); // "Ⅺ"
		SYMBOL.put(0x216b, "12"); // "Ⅻ"

		SYMBOL.put(0x2474, "1"); // "⑴"
		SYMBOL.put(0x2475, "2"); // "⑵"
		SYMBOL.put(0x2476, "3"); // "⑶"
		SYMBOL.put(0x2477, "4"); // "⑷"
		SYMBOL.put(0x2478, "5"); // "⑸"
		SYMBOL.put(0x2479, "6"); // "⑹"
		SYMBOL.put(0x247a, "7"); // "⑺"
		SYMBOL.put(0x247b, "8"); // "⑻"
		SYMBOL.put(0x247c, "9"); // "⑼"
		SYMBOL.put(0x247d, "10"); // "⑽"
		SYMBOL.put(0x247e, "11"); // "⑾"
		SYMBOL.put(0x247f, "12"); // "⑿"
		SYMBOL.put(0x2480, "13"); // "⒀"
		SYMBOL.put(0x2481, "14"); // "⒁"
		SYMBOL.put(0x2482, "15"); // "⒂"
		SYMBOL.put(0x2483, "16"); // "⒃"
		SYMBOL.put(0x2484, "17"); // "⒄"
		SYMBOL.put(0x2485, "18"); // "⒅"
		SYMBOL.put(0x2486, "19"); // "⒆"
		SYMBOL.put(0x2487, "20"); // "⒇"

		SYMBOL.put(0x2488, "1"); // "⒈"
		SYMBOL.put(0x2489, "2"); // "⒉"
		SYMBOL.put(0x248a, "3"); // "⒊"
		SYMBOL.put(0x248b, "4"); // "⒋"
		SYMBOL.put(0x248c, "5"); // "⒌"
		SYMBOL.put(0x248d, "6"); // "⒍"
		SYMBOL.put(0x248e, "7"); // "⒎"
		SYMBOL.put(0x248f, "8"); // "⒏"
		SYMBOL.put(0x2490, "9"); // "⒐"
		SYMBOL.put(0x2491, "10"); // "⒑"
		SYMBOL.put(0x2492, "11"); // "⒒"
		SYMBOL.put(0x2493, "12"); // "⒓"
		SYMBOL.put(0x2494, "13"); // "⒔"
		SYMBOL.put(0x2495, "14"); // "⒕"
		SYMBOL.put(0x2496, "15"); // "⒖"
		SYMBOL.put(0x2497, "16"); // "⒗"
		SYMBOL.put(0x2498, "17"); // "⒘"
		SYMBOL.put(0x2499, "18"); // "⒙"
		SYMBOL.put(0x249a, "19"); // "⒚"
		SYMBOL.put(0x249b, "20"); // "⒛"

		SYMBOL.put(0xff1a, ":"); // "："
		SYMBOL.put(0xfe55, ":"); // "﹕"

		SYMBOL.put(0xff1b, ";"); // "；"
		SYMBOL.put(0xfe54, ";"); // "﹔"

		SYMBOL.put(0xff1c, "<"); // "＜"
		SYMBOL.put(0xfe64, "<"); // "﹤"
		SYMBOL.put(0x3008, "<"); // "〈"
		SYMBOL.put(0x300a, "<"); // "《"
		SYMBOL.put(0x2264, "<="); // "≤"
		SYMBOL.put(0x2266, "<="); // "≦"

		SYMBOL.put(0xff1d, "="); // "＝"
		SYMBOL.put(0xfe66, "="); // "﹦"

		SYMBOL.put(0x2260, "<>"); // "≠"

		SYMBOL.put(0xff1e, ">"); // "＞"
		SYMBOL.put(0xfe65, ">"); // "﹥"
		SYMBOL.put(0x3009, ">"); // "〉"
		SYMBOL.put(0x300b, ">"); // "》"
		SYMBOL.put(0x2265, ">="); // "≥"
		SYMBOL.put(0x2267, ">="); // "≧"

		SYMBOL.put(0xff1f, "?"); // "？"
		SYMBOL.put(0xfe56, "?"); // "﹖"

		SYMBOL.put(0xff20, "@"); // "＠"
		SYMBOL.put(0xfe6b, "@"); // "﹫"

		SYMBOL.put(0xff3b, "["); // "［"
		SYMBOL.put(0xfe5d, "["); // "﹝"
		SYMBOL.put(0x3010, "["); // "【"
		SYMBOL.put(0x3016, "["); // "〖"
		SYMBOL.put(0x300e, "["); // "『"
		SYMBOL.put(0x300c, "["); // "「"
		SYMBOL.put(0x3014, "["); // "〔"

		SYMBOL.put(0xff3c, "\\"); // "＼"
		SYMBOL.put(0xfe68, "\\"); // "﹨"

		SYMBOL.put(0xff3d, "]"); // "］"
		SYMBOL.put(0xfe5e, "]"); // "﹞"
		SYMBOL.put(0x3011, "]"); // "】"
		SYMBOL.put(0x3017, "]"); // "〗"
		SYMBOL.put(0x300f, "]"); // "』"
		SYMBOL.put(0x300d, "]"); // "」"
		SYMBOL.put(0x3015, "]"); // "〕"

		SYMBOL.put(0xff3e, "^"); // "＾"

		SYMBOL.put(0xff3f, "_"); // "＿"

		SYMBOL.put(0xff40, "`"); // "｀"
		SYMBOL.put(0x2035, "`"); // "‵"

		SYMBOL.put(0xff5b, "{"); // "｛"
		SYMBOL.put(0xfe5b, "{"); // "﹛"

		SYMBOL.put(0xff5c, "|"); // "｜"
		SYMBOL.put(0x2223, "|"); // "∣"

		SYMBOL.put(0xff5d, "}"); // "｝"
		SYMBOL.put(0xfe5c, "}"); // "﹜"

		SYMBOL.put(0xff5e, "~"); // "～"

		// SYMBOL.put(0x00b1, "+-"); // "±" Not reverse
		//
		// SYMBOL.put(0x25cb, "0"); // "○" PY++ & MS WORD
		// SYMBOL.put(0x3007, "0"); // "〇" Punctuation & Alphabetic
		// SYMBOL.put(0x4e00, "1"); // "一"
		// SYMBOL.put(0x4e8c, "2"); // "二"
		// SYMBOL.put(0x4e09, "3"); // "三"
		// SYMBOL.put(0x56db, "4"); // "四"
		// SYMBOL.put(0x4e94, "5"); // "五"
		// SYMBOL.put(0x516d, "6"); // "六"
		// SYMBOL.put(0x4e03, "7"); // "七"
		// SYMBOL.put(0x516b, "8"); // "八"
		// SYMBOL.put(0x4e5d, "9"); // "九"
		// SYMBOL.put(0x5341, "10"); // "十"
		//
		// SYMBOL.put(0x96f6, "0"); // "零"
		// SYMBOL.put(0x58f9, "1"); // "壹"
		// SYMBOL.put(0x8d30, "2"); // "贰"
		// SYMBOL.put(0x53c1, "3"); // "叁"
		// SYMBOL.put(0x8086, "4"); // "肆"
		// SYMBOL.put(0x4f0d, "5"); // "伍"
		// SYMBOL.put(0x9646, "6"); // "陆"
		// SYMBOL.put(0x67d2, "7"); // "柒"
		// SYMBOL.put(0x634c, "8"); // "捌"
		// SYMBOL.put(0x7396, "9"); // "玖"
		// SYMBOL.put(0x62fe, "10"); // "拾"
		//
		// SYMBOL.put(0x3000, " "); // " " White space
		//
		// SYMBOL.put(0xff10, "0"); // "０"
		// SYMBOL.put(0xff11, "1"); // "１"
		// SYMBOL.put(0xff12, "2"); // "２"
		// SYMBOL.put(0xff13, "3"); // "３"
		// SYMBOL.put(0xff14, "4"); // "４"
		// SYMBOL.put(0xff15, "5"); // "５"
		// SYMBOL.put(0xff16, "6"); // "６"
		// SYMBOL.put(0xff17, "7"); // "７"
		// SYMBOL.put(0xff18, "8"); // "８"
		// SYMBOL.put(0xff19, "9"); // "９"
		//
		// SYMBOL.put(0xff21, "A"); // "Ａ"
		// SYMBOL.put(0xff22, "B"); // "Ｂ"
		// SYMBOL.put(0xff23, "C"); // "Ｃ"
		// SYMBOL.put(0xff24, "D"); // "Ｄ"
		// SYMBOL.put(0xff25, "E"); // "Ｅ"
		// SYMBOL.put(0xff26, "F"); // "Ｆ"
		// SYMBOL.put(0xff27, "G"); // "Ｇ"
		// SYMBOL.put(0xff28, "H"); // "Ｈ"
		// SYMBOL.put(0xff29, "I"); // "Ｉ"
		// SYMBOL.put(0xff2a, "J"); // "Ｊ"
		// SYMBOL.put(0xff2b, "K"); // "Ｋ"
		// SYMBOL.put(0xff2c, "L"); // "Ｌ"
		// SYMBOL.put(0xff2d, "M"); // "Ｍ"
		// SYMBOL.put(0xff2e, "N"); // "Ｎ"
		// SYMBOL.put(0xff2f, "O"); // "Ｏ"
		// SYMBOL.put(0xff30, "P"); // "Ｐ"
		// SYMBOL.put(0xff31, "Q"); // "Ｑ"
		// SYMBOL.put(0xff32, "R"); // "Ｒ"
		// SYMBOL.put(0xff33, "S"); // "Ｓ"
		// SYMBOL.put(0xff34, "T"); // "Ｔ"
		// SYMBOL.put(0xff35, "U"); // "Ｕ"
		// SYMBOL.put(0xff36, "V"); // "Ｖ"
		// SYMBOL.put(0xff37, "W"); // "Ｗ"
		// SYMBOL.put(0xff38, "X"); // "Ｘ"
		// SYMBOL.put(0xff39, "Y"); // "Ｙ"
		// SYMBOL.put(0xff3a, "Z"); // "Ｚ"
		//
		// SYMBOL.put(0xff41, "a"); // "ａ"
		// SYMBOL.put(0xff42, "b"); // "ｂ"
		// SYMBOL.put(0xff43, "c"); // "ｃ"
		// SYMBOL.put(0xff44, "d"); // "ｄ"
		// SYMBOL.put(0xff45, "e"); // "ｅ"
		// SYMBOL.put(0xff46, "f"); // "ｆ"
		// SYMBOL.put(0xff47, "g"); // "ｇ"
		// SYMBOL.put(0xff48, "h"); // "ｈ"
		// SYMBOL.put(0xff49, "i"); // "ｉ"
		// SYMBOL.put(0xff4a, "j"); // "ｊ"
		// SYMBOL.put(0xff4b, "k"); // "ｋ"
		// SYMBOL.put(0xff4c, "l"); // "ｌ"
		// SYMBOL.put(0xff4d, "m"); // "ｍ"
		// SYMBOL.put(0xff4e, "n"); // "ｎ"
		// SYMBOL.put(0xff4f, "o"); // "ｏ"
		// SYMBOL.put(0xff50, "p"); // "ｐ"
		// SYMBOL.put(0xff51, "q"); // "ｑ"
		// SYMBOL.put(0xff52, "r"); // "ｒ"
		// SYMBOL.put(0xff53, "s"); // "ｓ"
		// SYMBOL.put(0xff54, "t"); // "ｔ"
		// SYMBOL.put(0xff55, "u"); // "ｕ"
		// SYMBOL.put(0xff56, "v"); // "ｖ"
		// SYMBOL.put(0xff57, "w"); // "ｗ"
		// SYMBOL.put(0xff58, "x"); // "ｘ"
		// SYMBOL.put(0xff59, "y"); // "ｙ"
		// SYMBOL.put(0xff5a, "z"); // "ｚ"
	}

}
