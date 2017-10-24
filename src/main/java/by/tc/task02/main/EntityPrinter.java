package by.tc.task02.main;

import by.tc.task02.entity.Entity;

import java.util.List;
import java.util.Map;

public class EntityPrinter {
    public static void printEntity(Entity entity) {
        printRecursively(entity);
    }

    private static void printRecursively(Entity entity) {
        int nestingLevel = entity.getNestingLevel();

        if (hasAttributes(entity)) {
            printTabs(nestingLevel);
            printAttributes(entity);
        }

        if (hasValue(entity)) {
            printTabs(nestingLevel + 1);
            printEntityValue(entity);
        }

        List<Entity> children = entity.getChildEntities();
        if (children.isEmpty()) {
            return;
        }

        int childEntitiesSize = children.size();
        for (int child = 0; child < childEntitiesSize; child++) {
            Entity childEntity = entity.getChildEntities().get(child);
            printRecursively(childEntity);
            newLine();
        }
    }

    private static void newLine(){
        System.out.print('\n');
    }
    private static void printTabs(int nestingLevel) {
        for (int tab = 0; tab < nestingLevel; tab++) {
            System.out.print('\t');
        }
    }

    private static boolean hasAttributes(Entity entity){
        return entity.getAttributes().isEmpty();
    }
    private static boolean hasValue(Entity entity){
        return entity.getValue().isEmpty() || entity.getValue() == null;
    }
    private static void printEntityValue(Entity entity){
        System.out.print(entity.getValue());
    }
    private static void printAttributes(Entity entity) {
        Map<String, String> attributes = entity.getAttributes();
        int count = 1;
        int attrSize = attributes.size();
        for (String attributeName : attributes.keySet()) {
            if (count == attrSize) {
                System.out.print(attributes.get(attributeName) + ":");
                newLine();
            } else {
                System.out.print(attributes.get(attributeName) + ", ");
            }
            count++;
        }
    }
}