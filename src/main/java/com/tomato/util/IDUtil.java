package com.tomato.util;

import java.nio.charset.Charset;
import java.util.UUID;

public final class IDUtil {
    /**
     * <li/>默认使用 UTF-8 字符集, MD5Util 默认使用 GB18030 字符集
     * <li/>{@code Charset} 此类中定义的所有方法用于并发线程是安全的。
     */
    public static final Charset CHARSET_DEFAULT = Charset.forName("UTF-8");
    public static final String ID_PREFIX = "id";
    private static final char SALT_NEW_LINE = '\n';

    // Prevent instantiation
    private IDUtil() {
        super();
    }

    /**
     * @return
     */
    @Deprecated
    public static String randomUUID() {
        return getFormatedUUID();
    }

    /**
     * @return
     */
    public static String getFormatedUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * @param key
     *
     * @return
     */
    public static String getFormatedUUID(byte[] key) {
        return UUID.nameUUIDFromBytes(key).toString();
    }

    /**
     * @param keys
     *
     * @return
     */
    public static String getFormatedUUID(Object... keys) {
        return UUID.nameUUIDFromBytes(getBytes(keys)).toString();
    }

    /**
     * @return
     */
    public static String getUUID() {
        return toString(UUID.randomUUID());
    }

    /**
     * @param key
     *
     * @return
     */
    public static String getUUID(byte[] key) {
        return toString(UUID.nameUUIDFromBytes(key));
    }

    /**
     * @param keys
     *
     * @return
     */
    public static String getUUID(Object... keys) {
        return toString(UUID.nameUUIDFromBytes(getBytes(keys)));
    }

    /**
     * @return
     */
    @Deprecated
    public static String randomID() {
        return getID();
    }

    /**
     * @param prefixId
     *
     * @return
     */
    public static String getID(boolean prefixId) {
        return toBase16String(UUID.randomUUID(), prefixId);
    }

    /**
     * @return
     */
    public static String getID() {
        return getID(true);
    }

    /**
     * @param prefixId
     * @param key
     *
     * @return
     */
    public static String getID(boolean prefixId, byte[] key) {
        return toBase16String(UUID.nameUUIDFromBytes(key), prefixId);
    }

    /**
     * @param key
     *
     * @return
     */
    public static String getID(byte[] key) {
        return getID(true, key);
    }

    /**
     * @param prefixId
     * @param keys
     *
     * @return
     */
    public static String getID(boolean prefixId, Object... keys) {
        return toBase16String(UUID.nameUUIDFromBytes(getBytes(keys)), prefixId);
    }

    /**
     * @param keys
     *
     * @return
     */
    public static String getID(Object... keys) {
        return getID(true, keys);
    }

    /**
     * @param prefixId
     *
     * @return
     */
    public static String getBase36ID(boolean prefixId) {
        return toBase36String(UUID.randomUUID(), prefixId);
    }

    /**
     * @param length
     *
     * @return
     */
    public static String getBase36ID(int length) {
        String base36 = getBase36ID(false);
        if (length <= 0 || length >= base36.length()) {
            return base36;
        } else {
            return base36.substring(0, length);
        }
    }

    /**
     * @return
     */
    public static String getBase36ID() {
        return getBase36ID(true);
    }

    /**
     * @param prefixId
     * @param key
     *
     * @return
     */
    public static String getBase36ID(boolean prefixId, byte[] key) {
        return toBase36String(UUID.nameUUIDFromBytes(key), prefixId);
    }

    /**
     * @param key
     *
     * @return
     */
    public static String getBase36ID(byte[] key) {
        return getBase36ID(true, key);
    }

    /**
     * @param prefixId
     * @param keys
     *
     * @return
     */
    public static String getBase36ID(boolean prefixId, Object... keys) {
        return toBase36String(UUID.nameUUIDFromBytes(getBytes(keys)), prefixId);
    }

    /**
     * @param keys
     *
     * @return
     */
    public static String getBase36ID(Object... keys) {
        return getBase36ID(true, keys);
    }

    /**
     * @param name
     *
     * @return
     */
    @Deprecated
    public static String getBase32ID(String name) {
        return getBase36ID(name);
    }

    /**
     * @param prefixId
     *
     * @return
     */
    public static String getBase58ID(boolean prefixId) {
        return toBase58String(UUID.randomUUID(), prefixId);
    }

    /**
     * @param length
     *
     * @return
     */
    public static String getBase58ID(int length) {
        String base58 = getBase58ID(false);
        if (length <= 0 || length >= base58.length()) {
            return base58;
        } else {
            return base58.substring(0, length);
        }
    }

    /**
     * @return
     */
    public static String getBase58ID() {
        return getBase58ID(true);

    }

    /**
     * @param prefixId
     * @param key
     *
     * @return
     */
    public static String getBase58ID(boolean prefixId, byte[] key) {
        return toBase58String(UUID.nameUUIDFromBytes(key), prefixId);
    }

    /**
     * @param key
     *
     * @return
     */
    public static String getBase58ID(byte[] key) {
        return getBase58ID(true, key);
    }

    /**
     * @param prefixId
     * @param keys
     *
     * @return
     */
    public static String getBase58ID(boolean prefixId, Object... keys) {
        return toBase58String(UUID.nameUUIDFromBytes(getBytes(keys)), prefixId);
    }

    /**
     * @param keys
     *
     * @return
     */
    public static String getBase58ID(Object... keys) {
        return getBase58ID(true, keys);

    }

    /**
     * @param keys
     *
     * @return
     */
    public static byte[] getBytes(Object... keys) {
        // DO NOT CHANGE IT! 多处引用，不要轻易改变此算法！
        if (null != keys && keys.length > 0) {
            String salts = getSalts(keys);
            if (salts.length() > 0) {
                return salts.getBytes(CHARSET_DEFAULT);
            }
        }
        return (new byte[0]);
    }

    private static String getSalts(Object... salts) {
        // DO NOT CHANGE IT! 多处引用，不要轻易改变此算法！
        boolean firstLineSkiped = false;
        StringBuilder sb = new StringBuilder(64);
        for (Object salt : salts) {
            if (firstLineSkiped) {
                sb.append(SALT_NEW_LINE);
            } else {
                firstLineSkiped = true;
            }
            if (null != salt) {
                sb.append(StringUtil.valueOf(salt));
            }
        }
        return sb.toString();
    }

    /**
     * @param uuid
     *
     * @return
     */
    private static String toString(UUID uuid) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(NumberUtil.toHexString(uuid.getMostSignificantBits(), 16));
        sb.append(NumberUtil.toHexString(uuid.getLeastSignificantBits(), 16));
        return sb.toString();
    }

    /**
     * @param uuid
     * @param prefixId
     *
     * @return
     */
    private static String toBase16String(UUID uuid, boolean prefixId) {
        StringBuilder sb = new StringBuilder(34);
        if (prefixId) {
            sb.append(ID_PREFIX);
        }
        sb.append(NumberUtil.toHexString(uuid.getMostSignificantBits(), 16));
        sb.append(NumberUtil.toHexString(uuid.getLeastSignificantBits(), 16));
        return sb.toString();
    }

    /**
     * @param bytes
     * @param prefixId
     *
     * @return
     */
    public static String toBase36String(byte[] bytes, boolean prefixId) {
        String baseId = Base36.encode(bytes);
        if (prefixId) {
            StringBuilder sb = new StringBuilder(28);
            sb.append(ID_PREFIX);
            sb.append(baseId);
            baseId = sb.toString();
        }
        return baseId;
    }

    /**
     * @param bytes
     *
     * @return
     */
    public static String toBase36String(byte[] bytes) {
        return toBase36String(bytes, false);
    }

    /**
     * @param uuid
     * @param prefixId
     *
     * @return
     */
    private static String toBase36String(UUID uuid, boolean prefixId) {
        byte[] significant = new byte[16];
        NumberUtil.toBytes(uuid.getMostSignificantBits(), significant, 0);
        NumberUtil.toBytes(uuid.getLeastSignificantBits(), significant, 8);
        return toBase36String(significant, prefixId);
    }

    /**
     * @param bytes
     * @param prefixId
     *
     * @return
     */
    public static String toBase58String(byte[] bytes, boolean prefixId) {
        String baseId = Base58.encode(bytes);
        if (prefixId) {
            StringBuilder sb = new StringBuilder(24);
            sb.append(ID_PREFIX);
            sb.append(baseId);
            baseId = sb.toString();
        }
        return baseId;
    }

    /**
     * @param bytes
     *
     * @return
     */
    public static String toBase58String(byte[] bytes) {
        return toBase58String(bytes, false);
    }

    /**
     * @param uuid
     * @param prefixId
     *
     * @return
     */
    private static String toBase58String(UUID uuid, boolean prefixId) {
        byte[] significant = new byte[16];
        NumberUtil.toBytes(uuid.getMostSignificantBits(), significant, 0);
        NumberUtil.toBytes(uuid.getLeastSignificantBits(), significant, 8);
        return toBase58String(significant, prefixId);
    }

}
