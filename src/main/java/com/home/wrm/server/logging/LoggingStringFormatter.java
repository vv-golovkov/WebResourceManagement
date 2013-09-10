package com.home.wrm.server.logging;

import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

/**
 * Supports %d, %b, %f, %s arguments.
 * 
 */
public class LoggingStringFormatter {
    private static final char DIGITAL_F = 'd';
    private static final char BOOLEAN_F = 'b';
    private static final char DECIMAL_F = 'f';
    private static final char STRING_F = 's';
    
    private LinkedList<Character> supports = new LinkedList<Character>();
    private static final String NULL = "null";
    private static final char PERSENT = '%';
    
    {
        supports.add(DIGITAL_F);
        supports.add(STRING_F);
        supports.add(BOOLEAN_F);
        supports.add(DECIMAL_F);
    }

    /**
     * Returns a formatted string using the specified format string and
     * arguments.
     * 
     * @param str
     *            - a format string.
     * @param parameters
     *            - arguments referenced by the format specifiers in the format
     *            string.
     * @return a formatted string.
     */
    public String format(String str, Object... parameters) {
        LinkedList<Object> linkedParams = new LinkedList<Object>(Arrays.asList(parameters));
        return doReplacement(str, 0, linkedParams);
    }

    /**
     * 
     * @param str
     * @param startIndex
     * @param parameters
     * @return
     */
    private String doReplacement(String str, int startIndex, LinkedList<Object> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return str;
        }
        
        final int length = str.length();
        for (int index = startIndex; index < length; index++) {
            int persentIndex = 0;
            if (str.charAt(index) == PERSENT) {
                persentIndex = index;
            } else continue;
            
            if (persentIndex != -1 && persentIndex != length - 1) {
                char nextChar = str.charAt(persentIndex + 1);
                if (supports.contains(nextChar)) {
                    Object firstObject = parameters.getFirst();
                    String firstParameter = (firstObject != null) ? firstObject.toString() : NULL;
                    String formattedString = StringUtils.replaceOnce(str, str.substring(persentIndex, persentIndex + 2), firstParameter);
                    parameters.removeFirst();
                    int newStartIndex = persentIndex + firstParameter.length();
                    return doReplacement(formattedString, newStartIndex, parameters);
                }
            }
        }
        return str;
    }
}
