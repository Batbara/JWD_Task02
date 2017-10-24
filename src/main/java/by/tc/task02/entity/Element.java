package by.tc.task02.entity;

import java.io.Serializable;

public class Element implements Serializable{
    private String value;
    private int level;

    public Element(){}

    public Element(String name, int level){
        this.value = name;
        this.level = level;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (level != element.level) return false;
        return value.equals(element.value);
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + level;
        return result;
    }

    @Override
    public String toString() {
        return "Element{" +
                "value='" + value + '\'' +
                ", level=" + level +
                '}';
    }
}
