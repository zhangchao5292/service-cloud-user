package com.service.cloud.common.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HashCodeEquestDemo {
    private String  name;
    private Integer age;

    public HashCodeEquestDemo(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashCodeEquestDemo)) return false;
        HashCodeEquestDemo demo = (HashCodeEquestDemo) o;
        return Objects.equals(getName(), demo.getName()) &&
                Objects.equals(getAge(), demo.getAge());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getAge());
    }

    public static void main(String[] args) {
//        没有重写User 的equals方法，它会调用Object的equals方法
//        Object的equals方法是比较对象的引用对象是否是同一个
//        demo1和demo2是分别new出来的，那么他们的地址肯定是不一样的，自然hashcode值也会不一样
        HashCodeEquestDemo demo1=new HashCodeEquestDemo("张三",12);
        HashCodeEquestDemo demo2=new HashCodeEquestDemo("张三",12);
        System.out.println(demo1==demo2);
//        Set区别对象是不是唯一的标准是，两个对象hashcode是不是一样，再判定两个对象是否equals
        Set set =new HashSet();
        set.add(demo1);
        set.add(demo2);
        System.out.println("=============>>"+set.size());
//        Map 是先根据Key值的hashcode分配和获取对象保存数组下标的，然后再根据equals区分唯一值
//        如果不重写equals，那么比较的将是对象的引用是否指向同一块内存地址，
//        重写之后目的是为了比较两个对象的value值是否相等。
//（如int，float等）和String类（因为该类已重写了equals和hashcode方法）对象时，默认比较的是值
    }
}
