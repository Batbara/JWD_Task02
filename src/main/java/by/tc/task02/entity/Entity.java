package by.tc.task02.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity implements Serializable{

    private int nestingLevel;
    private String name;
    private String value;
    private List<Entity> childEntities;
    private Map<String, String> attributes;

    public Entity(){
        childEntities = new ArrayList<>();
        attributes = new HashMap<>();
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public void setNestingLevel(int nestingLevel) {
        this.nestingLevel = nestingLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Entity> getChildEntities() {
        return childEntities;
    }

    public void setChildEntities(List<Entity> childEntities) {
        this.childEntities = childEntities;
    }
    public void addChildEntities(Entity childEntity){
        this.childEntities.add(childEntity);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "nestingLevel=" + nestingLevel +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", childEntities=" + childEntities +
                ", attributes=" + attributes +
                '}';
    }
}
