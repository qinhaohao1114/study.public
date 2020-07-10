package com.self.study;

/**
 * @Author qinhaohao
 * @Date 2020/7/9 11:36 上午
 **/
public class Test {

    public static void main(String[] args) {
        Integer i=42;
        Long l=42l;
        Double d=42.0;

//        System.out.println(i==l);
//        System.out.println(i==d);
//        System.out.println(l==d);
        System.out.println(i.equals(d));
        System.out.println(d.equals(l));
        System.out.println(i.equals(l));
        System.out.println(l.equals(42L));


        String s = "com.jd.".replaceAll("\\.", "/") + "MyClass.class";
        System.out.println(s);
    }
}
