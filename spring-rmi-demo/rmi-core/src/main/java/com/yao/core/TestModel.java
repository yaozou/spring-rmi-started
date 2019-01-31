package com.yao.core;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @author: yaozou
 * @Date: 2019/1/17 11:17
 */
@Data
public class TestModel implements Serializable {
    private String name;
    private String desc;

    public TestModel(String name,String desc){
        this.name = name;
        this.desc = desc;
    }

   @Override
   public String toString(){
        return "name="+name+",desc="+desc;
    }
}
