package com.tomato.util;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.BitSet;

public final class URLUtil {
    public static final String CHARSET_UTF_8 = "UTF-8"; // DO NOT CHANGE
    public static final String URI_ENCODING_DEFAULT = CHARSET_UTF_8; // DO NOT CHANGE
    public static final String DEFAULT_ENDPOINT = "{scheme}://{server.name}:{server.port}/{context.root}";
    public static final String ENDPOINT_SCHEME = "{scheme}";
    public static final String ENDPOINT_SERVER_NAME = "{server.name}";
    public static final String ENDPOINT_SERVER_PORT = "{server.port}";
    public static final String ENDPOINT_CONTEXT_ROOT = "{context.root}";
    private static final String HEADER_IFMODSINCE = "If-Modified-Since";
    private static final String HEADER_LASTMOD = "Last-Modified";
    private static final BitSet unescapedURIComponentSet;
    private static final BitSet unescapedURISet;
    private static final int caseDiff = ('a' - 'A');
    private static final String QUERY_BASE64_PUBLIC_KEY_ZIP = "=bpkz.";
    private static final String QUERY_BASE64_SECRET_KEY_ZIP = "=bskz.";
    private static final String ROOT_RELATIVE_PREFIX1 = "~/";
    private static final int ROOT_RELATIVE_PREFIX1_LENGTH = ROOT_RELATIVE_PREFIX1.length();
    private static final int ROOT_RELATIVE_PREFIX1_START = ROOT_RELATIVE_PREFIX1_LENGTH - 1;
    private static final String URL_PROTOCOL_HTTP = "http"; // Must be lower case
    private static final String URL_PROTOCOL_HTTPS = "https"; // Must be lower case
    private static final String URL_PROTOCOL_FTP = "ftp"; // Must be lower case
    private static final String URL_PROTOCOL_FILE = "file"; // Must be lower case
    private static final String URL_PROTOCOL_JAR = "jar"; // Must be lower case
    private static final String JAR_PATH_SUFFIX1 = ".jar";
    private static final String JAR_PATH_SUFFIX2 = "!/";

    static {
        /* The list of characters that are not encoded has been
         * determined as follows:
         *
         * RFC 2396 states:
         * -----
         * Data characters that are allowed in a URI but do not have a
         * reserved purpose are called unreserved.  These include upper
         * and lower case letters, decimal digits, and a limited set of
         * punctuation marks and symbols.
         *
         * uric        = reserved | unreserved | escaped
         * reserved    = ";" | "/" | "?" | ":" | "@" | "&" | "=" | "+" | "$" | ","
         *
         * unreserved  = alphanum | mark
         * mark        = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
         *
         * escaped     = "%" hex hex
         * hex         = digit | "A" | "B" | "C" | "D" | "E" | "F"
         *                     | "a" | "b" | "c" | "d" | "e" | "f"
         *
         * alphanum    = alpha | digit
         * alpha       = lowalpha | upalpha
         *
         * Unreserved characters can be escaped without changing the
         * semantics of the URI, but this should not be done unless the
         * URI is being used in a context that does not allow the
         * unescaped character to appear.
         * -----
         *
         * It appears that both Netscape and Internet Explorer escape
         * all special characters from this list with the exception
         * of "-", "_", ".", "*". While it is not clear why they are
         * escaping the other characters, perhaps it is safest to
         * assume that there might be contexts in which the others
         * are unsafe if not escaped. Therefore, we will use the same
         * list. It is also noteworthy that this is consistent with
         * O'Reilly's "HTML: The Definitive Guide" (page 164).
         *
         * As a last note, Intenet Explorer does not encode the "@"
         * character which is clearly not unreserved according to the
         * RFC. We are being consistent with the RFC in this matter,
         * as is Netscape.
         *
         */

        unescapedURIComponentSet = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            unescapedURIComponentSet.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            unescapedURIComponentSet.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            unescapedURIComponentSet.set(i);
        }
        unescapedURIComponentSet.set('-');
        unescapedURIComponentSet.set('_');
        unescapedURIComponentSet.set('.');
        unescapedURIComponentSet.set('*');
        unescapedURISet = (BitSet) unescapedURIComponentSet.clone();
        unescapedURISet.set(';');
        unescapedURISet.set('/');
        unescapedURISet.set('?');
        unescapedURISet.set(':');
        unescapedURISet.set('@');
        unescapedURISet.set('&');
        unescapedURISet.set('=');
        unescapedURISet.set('+');
        unescapedURISet.set('$');
        unescapedURISet.set(',');
    }

    // Prevent instantiation
    private URLUtil() {
        super();
    }

    /**
     * @param value
     *
     * @return
     */
    public static String decodeURI(String value) {
        if (null != value && value.length() > 0) {
            return decode(value, URI_ENCODING_DEFAULT);
        }
        return value;
    }

    /**
     * @param value
     *
     * @return
     */
    public static String decodeURIComponent(String value) {
        if (null != value && value.length() > 0) {
            return decode(value, URI_ENCODING_DEFAULT);
        }
        return value;
    }

    /**
     * @param value
     *
     * @return 若<b>value</b>为<b>null</b>，则返回""，以避免被转换为"null"
     */
    public static String encodeURI(String value) {
        if (null != value && value.length() > 0) {
            return encode(value, URI_ENCODING_DEFAULT, unescapedURISet);
        }
        return (""); // 代替返回value
    }

    /**
     * @param value
     *
     * @return 若<b>value</b>为<b>null</b>，则返回""，以避免被转换为"null"
     */
    public static String encodeURIComponent(String value) {
        if (null != value && value.length() > 0) {
            return encode(value, URI_ENCODING_DEFAULT, unescapedURIComponentSet);
        }
        return (""); // 代替返回value
    }

    /**
     * Translates a string into <code>ECMAScript/encode</code> format using a specific encoding
     * scheme. This method uses the supplied encoding scheme to obtain the bytes for unsafe
     * characters.
     * <p>
     * <em><strong>Note:</strong> The <a href=
     * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
     * World Wide Web Consortium Recommendation</a> states that
     * UTF-8 should be used. Not doing so may introduce
     * incompatibilites.</em>
     *
     * @param s
     *         <code>String</code> to be translated.
     * @param enc
     *         The name of a supported <a href="../lang/package-summary.html#charenc">character
     *         encoding</a>.
     *
     * @return the translated <code>String</code>.
     */
    private static String encode(String s, String enc, BitSet dontNeedEncoding) {
        byte[] ba;
        String str;
        int bLen, sLen = s.length();
        boolean needToChange = false;
        StringBuilder out = new StringBuilder(sLen);
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        for (int i = 0; i < sLen; ) {
            int c = s.charAt(i);
            // System.out.println("Examining character: " + c);
            if (dontNeedEncoding.get(c)) {
                // System.out.println("Storing: " + c);
                out.append((char) c);
                ++i;
            } else {
                // convert to external encoding before hex conversion
                do {
                    charArrayWriter.write(c);
                    /*
                     * If this character represents the start of a Unicode
                     * surrogate pair, then pass in two characters. It's not
                     * clear what should be done if a bytes reserved in the
                     * surrogate pairs range occurs outside of a legal
                     * surrogate pair. For now, just treat it as if it were
                     * any other character.
                     */
                    if (c >= 0xD800 && c <= 0xDBFF) {
                        // System.out.println(Integer.toHexString(c) +
                        // " is high surrogate");
                        if ((i + 1) < sLen) {
                            int d = s.charAt(i + 1);
                            // System.out.println("\tExamining " +
                            // Integer.toHexString(d));
                            if (d >= 0xDC00 && d <= 0xDFFF) {
                                // System.out.println("\t" +
                                // Integer.toHexString(d) +
                                // " is low surrogate");
                                charArrayWriter.write(d);
                                ++i;
                            }
                        }
                    }
                    ++i;
                } while (i < sLen && !dontNeedEncoding.get((c = s.charAt(i))));

                charArrayWriter.flush();
                str = new String(charArrayWriter.toCharArray());
                try {
                    ba = str.getBytes(enc);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                bLen = ba.length;
                for (int j = 0; j < bLen; ++j) {
                    out.append('%');
                    char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
                    // converting to use uppercase letter as part of
                    // the hex value if ch is a letter.
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                    ch = Character.forDigit(ba[j] & 0xF, 16);
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                }
                charArrayWriter.reset();
                needToChange = true;
            }
        }
        return (needToChange ? out.toString() : s);
    }

    /**
     * Decodes a <code>ECMAScript/decode</code> string using a specific encoding scheme. The
     * supplied encoding is used to determine what characters are represented by any consecutive
     * sequences of the form " <code>%<i>xy</i></code>".
     * <p>
     * <em><strong>Note:</strong> The <a href=
     * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
     * World Wide Web Consortium Recommendation</a> states that
     * UTF-8 should be used. Not doing so may introduce
     * incompatibilites.</em>
     *
     * @param s
     *         the <code>String</code> to decode
     * @param enc
     *         The name of a supported <a href="../lang/package-summary.html#charenc">character
     *         encoding</a>.
     *
     * @return the newly decoded <code>String</code>
     */
    private static String decode(String s, String enc) {
        char c;
        byte[] bytes = null;
        boolean needToChange = false;
        int numChars = s.length();
        StringBuilder sb = new StringBuilder(numChars > 500 ? numChars / 2 : numChars);
        for (int i = 0; i < numChars; ) {
            c = s.charAt(i);
            if (c == '%') {
                /*
                 * Starting with this instance of %, process all
                 * consecutive substrings of the form %xy. Each
                 * substring %xy will yield a byte. Convert all
                 * consecutive  bytes obtained this way to whatever
                 * character(s) they represent in the provided
                 * encoding.
                 */
                try {
                    // (numChars-i)/3 is an upper bound for the number of
                    // remaining bytes
                    if (null == bytes) {
                        bytes = new byte[(numChars - i) / 3];
                    }
                    int pos = 0;
                    while (((i + 2) < numChars) && (c == '%')) {
                        bytes[pos++] = (byte) Integer.parseInt(s.substring(i + 1, i + 3), 16);
                        i += 3;
                        if (i < numChars) {
                            c = s.charAt(i);
                        }
                    }
                    // A trailing, incomplete byte encoding such as
                    // "%x" will cause an exception to be thrown
                    if ((i < numChars) && (c == '%')) {
                        throw new IllegalArgumentException("Incomplete trailing escape (%) pattern");
                    }
                    sb.append(new String(bytes, 0, pos, enc));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Illegal hex characters in escape (%) pattern - " + e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                needToChange = true;
            } else {
                sb.append(c);
                ++i;
            }
        }
        return (needToChange ? sb.toString() : s);
    }

    /**
     * @param contextPath
     *         因为 ServletContext.getContextPath() 和 HttpServletRequest.getContextPath()
     *         方法在某些时候略有区别，为避免歧义而保持高度兼容，应优先采用后者获取。
     * @param servletPath
     *
     * @return
     */
    public static String aheadContextPath(String contextPath, String servletPath) {
        int len = contextPath.length();
        if (len > 1) {
            StringBuilder sb = new StringBuilder(len + servletPath.length());
            return sb.append(contextPath).append(servletPath).toString();
        } else {
            return servletPath;
        }
    }

    /**
     * 解析以"~/"开始的相对URL调整为当前应用环境(由 contextPath 指定)的绝对URL
     *
     * @param contextPath
     *         因为 ServletContext.getContextPath() 和 HttpServletRequest.getContextPath()
     *         方法在某些时候略有区别，为避免歧义而保持高度兼容，应优先采用后者获取。
     * @param url
     *
     * @return
     */
    public static String resolveUrl(String contextPath, String url) {
        if (null != url && url.startsWith(ROOT_RELATIVE_PREFIX1)) {
            int prefixLen = contextPath.length();
            if (prefixLen > 1) {
                int urlLen = url.length();
                StringBuilder sb = new StringBuilder(prefixLen + urlLen);
                sb.append(contextPath).append(url, ROOT_RELATIVE_PREFIX1_START, urlLen);
                return sb.toString();
            } else {
                return url.substring(ROOT_RELATIVE_PREFIX1_START);
            }
        }
        return url;
    }

    /**
     * @param url
     *
     * @return
     */
    public static boolean isHttpProtocol(URL url) {
        if (null != url && URL_PROTOCOL_HTTP.equals(url.getProtocol())) {
            return true;
        }
        return false;
    }

    /**
     * @param url
     *
     * @return
     */
    public static boolean isHttpsProtocol(URL url) {
        if (null != url && URL_PROTOCOL_HTTPS.equals(url.getProtocol())) {
            return true;
        }
        return false;
    }

    /**
     * @param url
     *
     * @return
     */
    public static boolean isFtpProtocol(URL url) {
        if (null != url && URL_PROTOCOL_FTP.equals(url.getProtocol())) {
            return true;
        }
        return false;
    }

    /**
     * @param url
     *
     * @return
     */
    public static boolean isFileProtocol(URL url) {
        if (null != url && URL_PROTOCOL_FILE.equals(url.getProtocol())) {
            return true;
        }
        return false;
    }

    /**
     * @param url
     *
     * @return
     */
    public static boolean isJarProtocol(URL url) {
        if (null != url && URL_PROTOCOL_JAR.equals(url.getProtocol())) {
            return true;
        }
        return false;
    }

    /**
     * @param url
     *
     * @return
     */
    public static String getJarPath(URL url) {
        if (isJarProtocol(url)) {
            return getJarPath(url.getPath());
        }
        return null;
    }

    /**
     * @param path
     *
     * @return
     */
    public static String getJarPath(String path) {
        if (null != path) {
            String suffix1 = JAR_PATH_SUFFIX1;
            int suffix1Len = suffix1.length();
            String suffix2 = JAR_PATH_SUFFIX2;
            for (int index = path.lastIndexOf(suffix2); index > 0; index = path.lastIndexOf(suffix2, index - 1)) {
                if (suffix1.equalsIgnoreCase(path.substring(index - suffix1Len, index))) {
                    return path.substring(0, index);
                }
            }
        }
        return null;
    }

    /**
     * @param spec
     *
     * @return
     */
    public static URL getDummyURL(String spec) {
        try {
            return new URL(null, spec, new DummyURLStreamHandler());
        } catch (MalformedURLException e) {
            throw new RuntimeException("试图解析错误的URL: " + spec);
        }
    }

    private static class DummyURLStreamHandler extends URLStreamHandler {

        public DummyURLStreamHandler() {
        }

        @Override
        protected URLConnection openConnection(URL url) throws IOException {
            return new DummyURLConnection(url);
        }

        private static class DummyURLConnection extends URLConnection {

            public DummyURLConnection(URL url) {
                super(url);
            }

            @Override
            public void connect() throws IOException {
            }

            @Override
            public InputStream getInputStream() throws IOException {
                throw new UnsupportedOperationException();
            }
        }
    }

}
