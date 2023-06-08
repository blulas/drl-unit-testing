package com.ibm.bamoe.dmoe.testing;

import java.util.stream.Stream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Objects;
import java.util.Map;
import java.math.BigInteger;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

class Util {

    public static final String NULL_STRING="null";
    public static final String LIST_DELIMITER_REGEX="(?:\\s*,\\s*)+";
    public static final String TEST_FAIL_MISMATCHED_FIELD="the value for column \"%s\" matches: expected %s, actual %s";
    public static final String TEST_EXCEPTION_CHECKING_FIELD_MATCH="Exception checking match for field %s: %s\n\t%s";
    public static final String TEST_EXCEPTION_ASSIGNING_FIELD="Exception assigning field %s: %s\n\t%s";

    public static List<String> parseList(String s) {
        
        return s != null ? Stream.of(s.split(LIST_DELIMITER_REGEX, -1)).collect(Collectors.toList()) : null;
    }

    public static <T> List<T> parseAndTransformList(String s, Function<String, T> f) {
        return s != null ? Stream.of(s.split(LIST_DELIMITER_REGEX, -1)).map(f).collect(Collectors.toList()) : null;
    }

    public static Integer parseIntOrNull(String s) {
        Integer ret = null;
        try {
            ret = Integer.parseInt(s);
        } finally {
            return ret;
        }
    }

    public static BigInteger parseBigIntOrNull(String s) {
        BigInteger ret = null;
        try {
            ret = new BigInteger(s);
        } finally {
            return ret;
        }
    }

    public static LocalDate parseDateOrNull(String s) {
        LocalDate ret = null;
        try {
            ret = LocalDate.parse(s);
        } finally {
            return ret;
        }
    }

    public static boolean parseBooleanOrFalse(String s) {
        boolean ret = false;
        try {
            ret = Boolean.parseBoolean(s);
        } finally {
            return ret;
        }
    }
    
    public static Boolean parseBooleanOrNull(String s) {
        Boolean ret = null;
        try {
            ret = Boolean.parseBoolean(s);
        } finally {
            return ret;
        }
    }

    public static boolean fieldMatch(Map<String, String> columns, String key, Object value) {
        if(columns.get(key) == null) {
            return true;
        };
        Object converted_column_obj = null;
        if(value == null) {
            return columns.get(key).equals(NULL_STRING);
        }
        if(value instanceof String) {
            converted_column_obj = columns.get(key);
            return converted_column_obj.equals(value);
        }
        if(value instanceof Integer) {
            converted_column_obj = Util.parseIntOrNull(columns.get(key));
            return Objects.equals(value, converted_column_obj);
        }
        if(value instanceof Boolean) {
            converted_column_obj = Util.parseBooleanOrNull(columns.get(key));
            return Objects.equals(value, converted_column_obj);
        }
        if(value instanceof LocalDate) {
            converted_column_obj = Util.parseDateOrNull(columns.get(key));
            return Objects.equals(value, converted_column_obj);
        }
        if(value instanceof BigInteger) {
            converted_column_obj = Util.parseBigIntOrNull(columns.get(key));
            return Objects.equals(value, converted_column_obj);
        }
        return value.toString().equals(columns.get(key));
    }

    public static void assertFieldMatch(Map<String, String> columns, String key, Object value) {
        try {
            assertThat(String.format(TEST_FAIL_MISMATCHED_FIELD, key, columns.get(key), (value == null ? NULL_STRING : value.toString())), fieldMatch(columns, key, value));
        } catch(Exception e) {
            fail(String.format(TEST_EXCEPTION_CHECKING_FIELD_MATCH, key, e.toString(), e.getMessage()));
        }
    }
}