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

public class CheckGB11714Mod11_2 implements CheckDigit {
    /**
     * <ul>
     * i based 0:
     * <li>w<sub>i</sub> = 2<sup>(n - i)</sup> % 11</li>
     * </ul>
     * <ul>
     * i based 1:
     * <li>w<sub>i</sub> = 2<sup>(n - i + 1)</sup> % 11</li>
     * </ul>
     * 注意：此加权因子与 ISO 7064 和 GB/T 17710 标准略有差异，上标的顺序相反及起始数差1
     * <ul>
     * i based 1:
     * <li>w<sub>i</sub> = 2<sup>(i - 1)</sup> % 11</li>
     * </ul>
     */
    private static final int W[] = { 3, 7, 9, 10, 5, 8, 4, 2 };
    private static final int ZZJGDM_BODY_LEN = 8;

    /* (non-Javadoc)
     * @see com.tomato.util.checkdigits.CheckDigit#encode(java.lang.String)
     */
    @Override
    public String encode(String digits) {
        int c = computeCheck(digits);
        if (c == 10) {
            return digits + 'X';
        } else {
            return digits + c;
        }
    }

    /* (non-Javadoc)
     * @see com.tomato.util.checkdigits.CheckDigit#verify(java.lang.String)
     */
    @Override
    public boolean verify(String digits) {
        try {
            return computeCheck(getData(digits)) == getCheckDigit(digits);
        } catch (Exception e) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.tomato.util.checkdigits.CheckDigit#computeCheck(java.lang.String)
     */
    @Override
    public int computeCheck(String digits) {
        int len = digits.length();
        int c9 = 0, i = 0;
        int ci;
        char c;
        for (int j = 0; j < len; ++j) {
            c = digits.charAt(j);
            if (c >= '0' && c <= '9') {
                ci = c - '0';
            } else if (c >= 'A' && c <= 'Z') {
                ci = c - 'A' + 10;
            } else if (c >= 'a' && c <= 'z') {
                ci = c - 'a' + 10;
            } else if (Character.isWhitespace(c) || c == '-') {
                continue;
            } else {
                throw new NumberFormatException("组织机构代码 '" + digits + "' 包含无效字符: '" + c + "'");
            }
            if (i >= ZZJGDM_BODY_LEN) {
                throw new NumberFormatException("组织机构代码 '" + digits + "' 有效数据长度超过标准 " + ZZJGDM_BODY_LEN + " 位！");
            }
            c9 += ci * W[i++];
        }
        if (i < ZZJGDM_BODY_LEN) {
            throw new NumberFormatException("组织机构代码 '" + digits + "' 有效数据长度不足标准 " + ZZJGDM_BODY_LEN + " 位！");
        }
        return (11 - c9 % 11) % 11;
    }

    /* (non-Javadoc)
     * @see com.tomato.util.checkdigits.CheckDigit#getCheckDigit(java.lang.String)
     */
    @Override
    public int getCheckDigit(String digits) {
        char c = digits.charAt(digits.length() - 1);
        if (c == 'X' || c == 'x') {
            return 10;
        } else if (c >= '0' && c <= '9') {
            return c - '0';
        } else {
            return (-1);
        }
    }

    /* (non-Javadoc)
     * @see com.tomato.util.checkdigits.CheckDigit#getData(java.lang.String)
     */
    @Override
    public String getData(String digits) {
        return digits.substring(0, digits.length() - 1);
    }

}
