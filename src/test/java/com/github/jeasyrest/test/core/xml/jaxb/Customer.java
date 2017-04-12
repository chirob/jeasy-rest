package com.github.jeasyrest.test.core.xml.jaxb;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Customer {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<String> getAddress() {
        return address;
    }

    public void setAddress(Collection<String> address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", age=" + age + ", address=" + address + "]";
    }

    @XmlElement
    private int id;
    @XmlElement
    private String name;
    @XmlElement
    private int age;
    @XmlElementWrapper(name="address_array")
    @XmlElement
    private Collection<String> address;

}