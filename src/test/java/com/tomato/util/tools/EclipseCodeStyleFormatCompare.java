package com.tomato.util.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author charpty
 * @version $Id$
 * @since Nov 22, 2018 10:44
 */
public class EclipseCodeStyleFormatCompare {

    private static final String CONFIG_ID_START = "<setting id=\"";
    private static final String CONFIG_VALUE_START = "value=\"";
    private static final String CONFIG_END = "\"";

    // private static String file1 = "/Users/charpty/Desktop/rap-format/Preferences-Java-CodeStyle-Formatter.xml";
    // private static String file2 = "/Users/charpty/Desktop/rap-format/eclipse-codestyle.xml";
    private static String file1 = "/Users/charpty/Desktop/rap-format/Preferences-Editor-CodeStyle-Java.xml";
    private static String file2 = "/Users/charpty/Desktop/rap-format/P3C-CodeStyle-with-eclipse-out-idea.xml";

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            file1 = args[0];
            file2 = args[1];
        }

        System.out.println("Eclipse code style formatter file1 is: " + file1);
        System.out.println("Eclipse code style formatter file2 is: " + file2);

        Map<String, String> format1 = readFormatFile(file1);
        Map<String, String> format2 = readFormatFile(file2);

        compareFormatConfig(format1, format2);

    }

    private static void compareFormatConfig(Map<String, String> file1, Map<String, String> file2) {
        Set<String> keySet = new HashSet<>();
        keySet.addAll(file1.keySet());
        keySet.addAll(file2.keySet());

        for (String key : keySet) {
            String value1 = file1.get(key);
            String value2 = file2.get(key);
            if (value1 == null) {
                // impassable
                System.out.println("[key unbalance] file2 has key=" + key + ", but file1 not");
                continue;
            }
            if (value2 == null) {
                System.out.println("[key unbalance] file1 has key=" + key + ", but file2 not");
                continue;
            }
            if (value1.equals(value2)) {
                // System.out.println("[same] key=" + key + ", value=" + value1);
            } else {
                System.out
                        .println("[value conflict] key=" + key + ", file1 value=" + value1 + ", file2 value=" + value2);
            }
        }
    }

    private static Map<String, String> readFormatFile(String filepath) throws Exception {
        Map<String, String> result = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
        String config = "";
        String c;
        while ((c = br.readLine()) != null) {
            // 配置总是以新行"<setting"开始
            config = config + c;
            int i = config.indexOf(CONFIG_ID_START);
            if (i < 0) {
                config = "";
                continue;
            }
            int e = config.indexOf(CONFIG_END, i + CONFIG_ID_START.length());
            if (e < 0) {
                continue;
            }
            String id = config.substring(i + CONFIG_ID_START.length(), e);

            i = config.indexOf(CONFIG_VALUE_START, e);
            if (i < 0) {
                continue;
            }
            e = config.indexOf(CONFIG_END, i + CONFIG_VALUE_START.length());
            if (e < 0) {
                continue;
            }
            String value = config.substring(i + CONFIG_VALUE_START.length(), e);
            result.put(id, value);
            config = "";
        }
        return result;
    }
}
