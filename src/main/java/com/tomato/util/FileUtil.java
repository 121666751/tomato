package com.tomato.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.regex.Pattern;

public final class FileUtil {
    protected static final Pattern INVALID_FILE_NAME = Pattern.compile("[\\\\/:*?\"<>|]");
    protected static final int MAX_DIR_NAME_LENGTH = Integer.valueOf(System.getProperty("MaximumDirNameLength", "200")).intValue();
    protected static final int MAX_FILE_NAME_LENGTH = Integer.valueOf(System.getProperty("MaximumFileNameLength", "64")).intValue();
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final int MIN_CODE_POINT = 0;
    private static final int FAST_PATH_MAX = 255;
    private static final int MULTI_BYTE_SIZE = 2;
    private static final char PERIOD = '.';
    private static final char SLASH = '/';
    private static final char BACKSLASH = '\\';

    // Prevent instantiation
    private FileUtil() {
        super();
    }

    /**
     * @param fileName
     *
     * @return
     */
    public static File getFile(String fileName) {
        return new File(fileName);
    }

    /**
     * 获取文件名，即最后一个路径分隔符（“/”或“\”）后面部分。
     *
     * @param path
     *
     * @return
     */
    public static String getFileName(String path) {
        if (null != path) {
            char ch;
            for (int i = path.length() - 1; i >= 0; --i) {
                ch = path.charAt(i);
                if (ch == SLASH || ch == BACKSLASH) {
                    return path.substring(i + 1);
                }
            }
            return path;
        }
        return null;
    }

    /**
     * 获取文件扩展名，包括点(".")前缀
     *
     * @param fileName
     *
     * @return 如果没有找到则返回 null
     */
    public static String getFileExtension(String fileName) {
        if (null != fileName) {
            char ch;
            for (int i = fileName.length() - 1; i >= 0; --i) {
                ch = fileName.charAt(i);
                if (ch == PERIOD) {
                    return fileName.substring(i);
                } else if (ch == SLASH || ch == BACKSLASH) {
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 替换文件扩展名，包括点(".")前缀
     *
     * @param fileName
     * @param newExtName
     *         新文件扩展名，包括点(".")前缀。允许空值（null）表示去除扩展名
     *
     * @return
     */
    public static String replaceFileExtension(String fileName, String newExtName) {
        String oldExtName = getFileExtension(fileName);
        if (null == oldExtName) {
            if (null != newExtName && newExtName.length() > 0) {
                return (fileName + newExtName);
            }
            return fileName;
        } else {
            int len = oldExtName.length();
            if (len > 0) {
                fileName = fileName.substring(0, fileName.length() - len);
            }
            if (null != newExtName && newExtName.length() > 0) {
                return (fileName + newExtName);
            }
            return fileName;
        }
    }

    /**
     * @param fileName
     *
     * @return
     */
    public static boolean isFileExist(String fileName) {
        return getFile(fileName).isFile();
    }

    /**
     * @param suffix
     *         The suffix string to be used in generating the file's name; may be
     *         <code>null</code>, in which case the suffix <code>".tmp"</code> will be used
     *
     * @return
     *
     * @throws IOException
     * @see #getTempFile(String)
     */
    public static File createTempFile(String suffix) throws IOException {
        File tempFile = File.createTempFile("~rap", suffix);
        tempFile.deleteOnExit();
        return tempFile;
    }

    /**
     * @param url
     *
     * @return
     *
     * @throws IOException
     */
    public static File createTempFileCopyFrom(URL url) throws IOException {
        File tempFile = createTempFile(getFileExtension(url.getFile()));
        copyFile(url, tempFile);
        return tempFile;
    }

    /**
     * @param istream
     * @param suffix
     *         The suffix string to be used in generating the file's name; may be
     *         <code>null</code>, in which case the suffix <code>".tmp"</code> will be used
     *
     * @return
     *
     * @throws IOException
     */
    public static File createTempFileCopyFrom(InputStream istream, String suffix) throws IOException {
        File tempFile = createTempFile(suffix);
        copyFile(istream, tempFile);
        return tempFile;
    }

    /**
     * @param istream
     *
     * @return
     *
     * @throws IOException
     */
    public static File createTempFileCopyFrom(InputStream istream) throws IOException {
        File tempFile = createTempFile(null);
        copyFile(istream, tempFile);
        return tempFile;
    }

    /**
     * @return 返回临时文件夹，如：C:\Temp\
     */
    public static String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * @param fileName
     *
     * @return 返回存放在临时文件夹的文件路径，如：C:\Temp\fileName
     */
    public static String getTempPathName(String fileName) {
        Path path = new Path(getTempDirectory());
        path.append(fileName);
        return path.toString();
    }

    /**
     * @param fileName
     *
     * @return 返回存放在临时文件夹的文件对象，如：C:\Temp\fileName
     *
     * @see #createTempFile(String)
     */
    public static File getTempFile(String fileName) {
        Path path = new Path(getTempDirectory());
        path.append(fileName);
        return new File(path.toString());
    }

    /**
     * @param fileName
     * @param limitSize
     *
     * @return
     *
     * @throws IOException
     */
    public static String readFile(String fileName, int limitSize) throws IOException {
        File file = new File(fileName);
        long len = file.length(); // in bytes
        if (len <= 0 || limitSize > 0 && len > limitSize) {
            return null;
        } else {
            FileInputStream in = new FileInputStream(file);
            try {
                byte bt[] = new byte[(int) len];
                in.read(bt);
                return new String(bt);
            } finally {
                in.close();
            }
        }
    }

    /**
     * @param fileName
     *
     * @return
     *
     * @throws IOException
     */
    public static String readFile(String fileName) throws IOException {
        return readFile(fileName, 0);
    }

    /**
     * @param file
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] readFileByteArray(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copyStream(input, output);
        return output.toByteArray();
    }

    /**
     * @param fileName
     *
     * @return
     *
     * @throws IOException
     */
    public static byte[] readFileByteArray(String fileName) throws IOException {
        return readFileByteArray(new File(fileName));
    }

    /**
     * @param file
     *
     * @return
     *
     * @throws FileNotFoundException
     */
    public static Reader getFileReader(File file) throws FileNotFoundException {
        FileInputStream input = new FileInputStream(file);
        return new InputStreamReader(input);
    }

    /**
     * @param fileName
     *
     * @return
     *
     * @throws FileNotFoundException
     */
    public static Reader getFileReader(String fileName) throws FileNotFoundException {
        return getFileReader(new File(fileName));
    }

    /**
     * @param file
     *
     * @return
     *
     * @throws FileNotFoundException
     */
    public static Writer getFileWriter(File file) throws FileNotFoundException {
        FileOutputStream output = new FileOutputStream(file);
        return new OutputStreamWriter(output);
    }

    /**
     * @param fileName
     *
     * @return
     *
     * @throws FileNotFoundException
     */
    public static Writer getFileWriter(String fileName) throws FileNotFoundException {
        return getFileWriter(new File(fileName));
    }

    /**
     * @param name
     *
     * @return
     */
    public static String toFileSystemDirectorySafeName(String name) {
        return toFileSystemSafeName(name, true, MAX_DIR_NAME_LENGTH, MULTI_BYTE_SIZE);
    }

    /**
     * @param name
     *
     * @return
     */
    public static String toFileSystemSafeName(String name) {
        return toFileSystemSafeName(name, false, MAX_FILE_NAME_LENGTH, MULTI_BYTE_SIZE);
    }

    /**
     * 文件名特殊字符更为宽泛的过滤与规范处理，适合所有操作系统。
     *
     * @param name
     * @param dirSeparators
     * @param maxFileLength
     * @param multiByteSize
     *
     * @return
     *
     * @see #toWindowsFileName(String fileName, String replacement)
     * @see #toWindowsFileName(String fileName)
     */
    public static String toFileSystemSafeName(String name, boolean dirSeparators, int maxFileLength, int multiByteSize) {
        char ch;
        char last = 0;
        int count = 0;
        int size = name.length();
        StringBuilder sb = new StringBuilder(size * 2);
        for (int i = 0; i < size; i++) {
            ch = name.charAt(i);
            if (multiByteSize > 1 && (ch < MIN_CODE_POINT || ch > FAST_PATH_MAX)) {
                count += multiByteSize;
                last = ch;
            } else if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9' || ch == '-' || ch == '#' || ch == '.' && last != 0
                    || dirSeparators && (ch == '/' || ch == '\\')) {
                count += 1;
                last = ch;
            } else if (last != '_') {
                count += 1;
                last = '_';
            } else {
                continue;
            }
            if (count <= maxFileLength) {
                sb.append(last);
            }
            if (count >= maxFileLength) {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 按Windows操作系统标准，文件名称不能包含如下9个字符，将被全部替换：
     * <p>
     * \ / : * ? " < > |
     *
     * @param fileName
     * @param replacement
     *
     * @return
     *
     * @see #toFileSystemSafeName(String name, boolean dirSeparators, int maxFileLength, int
     * multiByteSize)
     */
    public static String toWindowsFileName(String fileName, String replacement) {
        if (null == fileName || fileName.length() <= 0) {
            throw new IllegalArgumentException("fileName");
        } else if (null == replacement) {
            throw new IllegalArgumentException("replace");
        } else {
            String newFileName = INVALID_FILE_NAME.matcher(fileName).replaceAll(replacement);
            if (newFileName.length() > 0) {
                return newFileName;
            }
            throw new IllegalArgumentException("File Name \"" + fileName + "\" results in a empty fileName!");
        }
    }

    /**
     * 按Windows操作系统标准，文件名称不能包含如下9个字符，将被全部清除：
     * <p>
     * \ / : * ? " < > |
     *
     * @param fileName
     *
     * @return
     */
    public static String toWindowsFileName(String fileName) {
        return toWindowsFileName(fileName, "");
    }

    /**
     * 按Windows操作系统标准，文件名称不能包含如下9个字符：
     * <p>
     * \ / : * ? " < > |
     *
     * @param fileName
     *
     * @return
     */
    public static boolean isValidWindowsFileName(String fileName) {
        if (null != fileName && fileName.length() > 0 && !INVALID_FILE_NAME.matcher(fileName).matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param fileToDelete
     *
     * @return
     */
    public static boolean deleteFile(File fileToDelete) {
        if (fileToDelete == null || !fileToDelete.exists()) {
            return true;
        } else {
            boolean result = deleteChildren(fileToDelete);
            result &= fileToDelete.delete();
            return result;
        }
    }

    /**
     * @param parent
     *
     * @return
     */
    public static boolean deleteChildren(File parent) {
        if (parent == null || !parent.exists()) {
            return false;
        }
        boolean result = true;
        if (parent.isDirectory()) {
            File files[] = parent.listFiles();
            if (files == null) {
                result = false;
            } else {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file.getName().equals(".") || file.getName().equals("..")) {
                        continue;
                    }
                    if (file.isDirectory()) {
                        result &= deleteFile(file);
                    } else {
                        result &= file.delete();
                    }
                }

            }
        }
        return result;
    }

    /**
     * @param src
     * @param targetDirectory
     *
     * @throws IOException
     */
    public static void moveFile(File src, File targetDirectory) throws IOException {
        if (!src.renameTo(new File(targetDirectory, src.getName()))) {
            throw new IOException((new StringBuilder()).append("Failed to move ").append(src).append(" to ").append(targetDirectory).toString());
        } else {
            return;
        }
    }

    /**
     * @param srcFile
     * @param destFile
     *
     * @throws IOException
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        FileInputStream srcStream = new FileInputStream(srcFile);
        copyFile(srcStream, destFile);
    }

    /**
     * @param srcUrl
     * @param destFile
     *
     * @throws IOException
     */
    public static void copyFile(URL srcUrl, File destFile) throws IOException {
        InputStream srcStream = srcUrl.openStream();
        copyFile(srcStream, destFile);
    }

    /**
     * @param istream
     * @param destFile
     *
     * @throws IOException
     */
    public static void copyFile(InputStream istream, File destFile) throws IOException {
        try {
            FileOutputStream destStream = new FileOutputStream(destFile);
            copyStream(istream, destStream, true);
        } catch (FileNotFoundException e) {
            try {
                istream.close();
            } catch (IOException ignore) {
            }
            throw e;
        }
    }

    /**
     * @param istream
     * @param ostream
     * @param autoClose
     *
     * @throws IOException
     */
    public static void copyStream(InputStream istream, OutputStream ostream, boolean autoClose) throws IOException {
        boolean success = false;
        try {
            byte buffer[] = new byte[DEFAULT_BUFFER_SIZE];

            for (int len = istream.read(buffer); len >= 0; len = istream.read(buffer)) {
                ostream.write(buffer, 0, len);
            }
            success = true;
        } finally {
            if (autoClose) {
                IOException first = null;
                try {
                    istream.close();
                } catch (IOException ioe) {
                    if (null == first) {
                        first = ioe;
                    }
                }
                try {
                    ostream.close();
                } catch (IOException ioe) {
                    if (null == first) {
                        first = ioe;
                    }
                }
                if (success && null != first) {
                    throw first;
                }
            }
        }
    }

    /**
     * @param istream
     * @param ostream
     *
     * @throws IOException
     */
    public static void copyStream(InputStream istream, OutputStream ostream) throws IOException {
        copyStream(istream, ostream, true);
    }

    /**
     * @param dir
     *
     * @throws IOException
     */
    public static void mkdirs(File dir) throws IOException {
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new IOException(
                        (new StringBuilder()).append("Failed to create directory '").append(dir).append("', regular file already existed with that name")
                                .toString());
            }
        } else if (!dir.mkdirs()) {
            throw new IOException((new StringBuilder()).append("Failed to create directory '").append(dir).append("'").toString());
        }
    }

}
