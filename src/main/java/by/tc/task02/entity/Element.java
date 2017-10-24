package by.tc.task02.entity;

public class Element {
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
}
