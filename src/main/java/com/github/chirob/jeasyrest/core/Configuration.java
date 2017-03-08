package com.github.chirob.jeasyrest.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.github.chirob.jeasyrest.io.util.LoadedProperties;
import com.github.chirob.jeasyrest.reflect.ClassloaderResources;

public class Configuration {

    @SuppressWarnings("serial")
    public static final Map<String, Configuration> INSTANCES = Collections
            .unmodifiableMap(new HashMap<String, Configuration>() {
                @Override
                public Configuration get(Object configName) {
                    Configuration config = super.get(configName);
                    if (config == null) {
                        String name = (String) configName;
                        config = new Configuration(name);
                        put(name, config);
                    }
                    return config;
                }
            });

    public String getString(String key) {
        return get(key, String.class, null);
    }

    public String getString(String key, String defaultValue) {
        return get(key, String.class, defaultValue);
    }

    public Boolean getBoolean(String key) {
        return get(key, Boolean.class, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return get(key, Boolean.class, defaultValue);
    }

    public Number getNumber(String key) {
        return get(key, Number.class, null);
    }

    public Number getNumber(String key, Number defaultValue) {
        return get(key, Number.class, defaultValue);
    }

    public BigDecimal getBigDecimal(String key) {
        return get(key, BigDecimal.class, null);
    }

    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return get(key, BigDecimal.class, defaultValue);
    }

    public BigInteger getBigInteger(String key) {
        return get(key, BigInteger.class, null);
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return get(key, BigInteger.class, defaultValue);
    }

    public Double getDouble(String key) {
        return get(key, Double.class, null);
    }

    public Double getDouble(String key, Double defaultValue) {
        return get(key, Double.class, defaultValue);
    }

    public Float getFloat(String key) {
        return get(key, Float.class, null);
    }

    public Float getFloat(String key, Float defaultValue) {
        return get(key, Float.class, defaultValue);
    }

    public Long getLong(String key) {
        return get(key, Long.class, null);
    }

    public Long getLong(String key, Long defaultValue) {
        return get(key, Long.class, defaultValue);
    }

    public Integer getInteger(String key) {
        return get(key, Integer.class, null);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return get(key, Integer.class, defaultValue);
    }

    public Short getShort(String key) {
        return get(key, Short.class, null);
    }

    public Short getShort(String key, Short defaultValue) {
        return get(key, Short.class, defaultValue);
    }

    public Byte getByte(String key) {
        return get(key, Byte.class, null);
    }

    public Byte getByte(String key, Byte defaultValue) {
        return get(key, Byte.class, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> valueType, T defaultValue) {
        String stringValue = configProperties.getProperty(key);
        if (stringValue == null && defaultValue != null) {
            return defaultValue;
        }
        if (String.class.equals(valueType)) {
            return (T) stringValue;
        } else if (Boolean.class.equals(valueType)) {
            String value = stringValue == null || stringValue.trim().length() == 0 ? "false" : stringValue;
            return (T) Boolean.valueOf(value);
        } else if (Number.class.isAssignableFrom(valueType)) {
            String value = stringValue == null || stringValue.trim().length() == 0 ? "0" : stringValue;
            switch (NumericValueType.valueOf(valueType.getSimpleName())) {
            case Number:
                return (T) new BigDecimal(value);
            case BigDecimal:
                return (T) new BigDecimal(value);
            case BigInteger:
                return (T) new BigInteger(value);
            case Double:
                return (T) new Double(value);
            case Float:
                return (T) new Float(value);
            case Long:
                return (T) new Long(value);
            case Integer:
                return (T) new Integer(value);
            case Short:
                return (T) new Short(value);
            case Byte:
                return (T) new Byte(value);
            default:
                throw new IllegalArgumentException("Invalid numeric value type: " + valueType.getName());
            }
        } else {
            throw new IllegalArgumentException("Invalid value type: " + valueType.getName());
        }
    }

    private Configuration(String configName) {
        configProperties = new Properties();
        for (String mapName : Arrays.asList("META-INF/jeasyrest/" + configName + ".cfg",
                "jeasyrest/" + configName + ".config")) {
            ClassloaderResources resources = new ClassloaderResources(mapName);
            for (URL url : resources) {
                try {
                    configProperties.putAll(new LoadedProperties(url));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Properties configProperties;

    private static enum NumericValueType {
        Number, Byte, Short, Integer, Long, Float, Double, BigInteger, BigDecimal
    }
}
