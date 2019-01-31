package com.yao.core.logger;


import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/29 11:20
 */
public class XpandPatternLayout extends PatternLayout {
    public XpandPatternLayout(){
        this(DEFAULT_CONVERSION_PATTERN);
    }
    public XpandPatternLayout(String pattern) {
        super(pattern);
    }

    @Override
    protected PatternParser createPatternParser(String pattern) {
        return new XpandPatternParser(pattern == null ? DEFAULT_CONVERSION_PATTERN : pattern);
    }
}
