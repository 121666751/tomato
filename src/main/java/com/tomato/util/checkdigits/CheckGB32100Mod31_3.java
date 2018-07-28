/*
 * Copyright 2005, Nick Galbreath
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software for any purpose
 * with or without fee is hereby granted, provided that the above copyright
 * notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * Except as contained in this notice, the name of a copyright holder shall not
 * be used in advertising or otherwise to promote the sale, use or other dealings
 * in this Software without prior written authorization of the copyright holder.
 */
package com.tomato.util.checkdigits;

public class CheckGB32100Mod31_3 implements CheckDigit {
    /**
     * <ul>
     * i based 0:
     * <li>w<sub>i</sub> = 2<sup>i</sup> % 31</li>
     * </ul>
     * <ul>
     * i based 1:
     * <li>w<sub>i</sub> = 2<sup>(i - 1)</sup> % 31</li>
     * </ul>
     */
    private static final int W[] = { 1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28 };
    private static final char N2C[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' /*'I'*/, 'J', 'K', 'L', 'M',
            'N' /*'O'*/, 'P', 'Q', 'R' /*'S'*/, 'T', 'U' /*'V'*/, 'W', 'X', 'Y' /*'Z'*/ };
    private static final int C2N[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, -1 /*I*/, 18, 19, 20, 21, 22,
            -1 /*O*/, 23, 24, 25, -1 /*S*/, 26, 27, -1 /*V*/, 28, 29, 30, -1 /*Z*/, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, -1 /*i*/, 18, 19,
            20, 21, 22, -1 /*o*/, 23, 24, 25, -1 /*s*/, 26, 27, -1 /*v*/, 28, 29, 30, -1 /*z*/ };
    private static final int SHXYDM_BODY_LEN = 17;
    private static final int SHXYDM_FULL_LEN = SHXYDM_BODY_LEN + 1;
    private static final int SHXYDM_ZZJGDM_OFFSET = 8;
    private static final int SHXYDM_ZZJGDM_END = SHXYDM_BODY_LEN;
    private static final CheckDigit ZZJGDM_CHECKER = new CheckGB11714Mod11_2();

    /* (non-Javadoc)
     * @see com.ifugle.util.checkdigits.CheckDigit#encode(java.lang.String)
     */
    @Override
    public String encode(String digits) {
        int c = computeCheck(digits);
        return (digits + N2C[c]);
    }

    /* (non-Javadoc)
     * @see com.ifugle.util.checkdigits.CheckDigit#verify(java.lang.String)
     */
    @Override
    public boolean verify(String digits) {
        try {
            if (computeCheck(getData(digits)) == getCheckDigit(digits)) {
                String zzjgdm = getZzjgdm(digits);
                return ZZJGDM_CHECKER.verify(zzjgdm);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.ifugle.util.checkdigits.CheckDigit#computeCheck(java.lang.String)
     */
    @Override
    public int computeCheck(String digits) {
        int len = digits.length();
        int c18 = 0, i = 0;
        int ci;
        char c;
        for (int j = 0; j < len; ++j) {
            c = digits.charAt(j);
            if (c >= '0' && c <= 'z') {
                ci = C2N[c - '0'];
            } else if (Character.isWhitespace(c) || c == '-') {
                continue;
            } else {
                ci = -1;
            }
            if (ci < 0) {
                throw new NumberFormatException("统一社会信用代码 '" + digits + "' 包含无效字符: '" + c + "'");
            } else if (i >= SHXYDM_BODY_LEN) {
                throw new NumberFormatException("统一社会信用代码 '" + digits + "' 有效数据长度超过标准 " + SHXYDM_BODY_LEN + " 位！");
            }
            c18 += ci * W[i++];
        }
        if (i < SHXYDM_BODY_LEN) {
            throw new NumberFormatException("统一社会信用代码 '" + digits + "' 有效数据长度不足标准 " + SHXYDM_BODY_LEN + " 位！");
        }
        return ((31 - (c18 % 31)) % 31);
    }

    /* (non-Javadoc)
     * @see com.ifugle.util.checkdigits.CheckDigit#getCheckDigit(java.lang.String)
     */
    @Override
    public int getCheckDigit(String digits) {
        char c = digits.charAt(digits.length() - 1);
        if (c >= '0' && c <= 'z') {
            return C2N[c - '0'];
        } else {
            return (-1);
        }
    }

    /* (non-Javadoc)
     * @see com.ifugle.util.checkdigits.CheckDigit#getData(java.lang.String)
     */
    @Override
    public String getData(String digits) {
        return digits.substring(0, digits.length() - 1);
    }

    /**
     * 获取<strong>组织机构代码</strong>
     *
     * @param digits
     *
     * @return
     *
     * @see CheckGB11714Mod11_2
     */
    public String getZzjgdm(String digits) {
        char c;
        int len = digits.length() - 1;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; ++i) {
            c = digits.charAt(i);
            if (Character.isWhitespace(c) || c == '-') {
                continue;
            }
            sb.append(c);
        }
        len = sb.length();
        if (len != SHXYDM_BODY_LEN) {
            throw new NumberFormatException("统一社会信用代码 '" + digits + "' 有效长度不是标准 " + SHXYDM_FULL_LEN + " 位！");
        } else {
            return sb.substring(SHXYDM_ZZJGDM_OFFSET, SHXYDM_ZZJGDM_END);
        }
    }

}
