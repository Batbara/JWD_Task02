package by.tc.task02.main;

import by.tc.task02.entity.Entity;

import java.util.List;
import java.util.Map;

public class EntityPrinter {
    public static void printEntity(Entity entity) {
        int rootPrevLevel = 1;
        int rootNextLevel = 2;
        printRecursively(entity, rootPrevLevel, rootNextLevel);
    }

    private static void printRecursively(Entity entity, int prevLevel, int nextLevel) {
        int nestingLevel = entity.getNestingLevel();

        if (hasAttributes(entity)) {
            newLine();
            printTabs(nestingLevel);
            printAttributes(entity);
        }

        if (hasValue(entity)) {
            if (nestingLevel != prevLevel) {
                printTabs(nestingLevel + 1);
            }
            printEntityValue(entity);
            if (nestingLevel == nextLevel) {
                System.out.print(" - ");
            }
        }

        List<Entity> children = entity.getChildEntities();
        if (children.isEmpty()) {
            return;
        }

        int childEntitiesSize = children.size();
        for (int child = 0; child < childEntitiesSize; child++) {
            int previousLevel = getPreviousNestingLevel(entity, child);
            int nextNestingLevel = getNextNestingLevel(entity, child);

            Entity childEntity = entity.getChildEntities().get(child);
            printRecursively(childEntity, previousLevel, nextNestingLevel);
        }
        newLine();
    }

    private static int getPreviousNestingLevel(Entity entity, int currentChild) {
        int previousLevel = entity.getNestingLevel();
        if (currentChild != 0) {
            previousLevel = entity.getChildEntities().get(currentChild - 1).getNestingLevel();
        }
        return previousLevel;
    }

    private static int getNextNestingLevel(Entity entity, int currentChild) {
        int childEntitiesSize = entity.getChildEntities().size();
        int nextNestingLevel;
        if (currentChild != childEntitiesSize - 1) {
            nextNestingLevel = entity.getChildEntities().get(currentChild + 1).getNestingLevel();
        } else {
            nextNestingLevel = entity.getNestingLevel();
        }
        return nextNestingLevel;
    }

    private static void newLine() {
        System.out.print('\n');
    }

    private static void printTabs(int nestingLevel) {
        for (int tab = 0; tab < nestingLevel; tab++) {
            System.out.print('\t');
        }
    }

    private static boolean hasAttributes(Entity entity) {
        Map<String, String> attributes = entity.getAttributes();
        return attributes.size() != 0;
    }

    private static boolean hasValue(Entity entity) {
        String value = entity.getValue();
        return value != null;
    }

    private static void printEntityValue(Entity entity) {
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
