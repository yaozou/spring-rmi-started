package com.yao.core.logger;

import com.yao.core.RmiThreadLocal;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/28 15:48
 */
@Plugin(name = "query", category = "Converter")
@ConverterKeys({"traceId"})
public class QueryConverter extends LogEventPatternConverter {
    public static final QueryConverter INSTANCE = new QueryConverter("traceId", "");
    /**
     * Constructs an instance of LoggingEventPatternConverter.
     *
     * @param name  name of converter.
     * @param style CSS style for output.
     */
    protected QueryConverter(String name, String style) {
        super(name, style);
    }

    public static QueryConverter newInstance(
            final String[] options) {
        return INSTANCE;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        if (RmiThreadLocal.get() != null){
            toAppendTo.append("request-id:"+RmiThreadLocal.get());
        }
    }
}
