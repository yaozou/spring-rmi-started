package com.xpand.logging;


import com.xpand.core.RmiThreadLocal;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/29 11:33
 */
public class XpandPatternParser extends PatternParser {
    public XpandPatternParser(String pattern) {
        super(pattern);
    }

    @Override
    public void finalizeConverter(char c) {
        if (c == 'T') {
            String exs = super.extractOption();
            addConverter(new XpandPatternConverter(formattingInfo, exs));
            currentLiteral.setLength(0);
        } else {
            super.finalizeConverter(c);
        }
    }

    private class XpandPatternConverter extends PatternConverter {
        private String cfg;
        public XpandPatternConverter(FormattingInfo formattingInfo, String cfg){
            super(formattingInfo);
            this.cfg = cfg;
        }

        @Override
        protected String convert(LoggingEvent event) {
            return RmiThreadLocal.get();
        }
    }
}


