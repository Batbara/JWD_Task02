package by.tc.task02.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
        attributes = new LinkedHashMap<>();
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
    public void addChildEntity(Entity childEntity){
        this.childEntities.add(0,childEntity);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        if (nestingLevel != entity.nestingLevel) return false;
        if (!name.equals(entity.name)) return false;
        if (!value.equals(entity.value)) return false;
        if (!childEntities.equals(entity.childEntities)) return false;
        return attributes.equals(entity.attributes);
    }

    @Override
    public int hashCode() {
        int result = nestingLevel;
        result = 31 * result + name.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + childEntities.hashCode();
        result = 31 * result + attributes.hashCode();
        return result;
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
