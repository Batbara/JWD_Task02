package by.tc.task02.dao.impl;

import by.tc.task02.entity.Entity;
import by.tc.task02.entity.Source;
import by.tc.task02.entity.Element;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityCreator {

    public static final char TAG_BEGINNING = '<';
    public static final char TAG_END = '>';

    private Source source;
    private Deque<Element> elements;
    private Deque<Entity> entityStack;


    public EntityCreator() {
        elements = new ArrayDeque<>();
        entityStack = new ArrayDeque<>();
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Entity getRootEntity() {
        processSource();
        return entityStack.pop();
    }

    private void processSource() {
        StringBuilder content = new StringBuilder(source.getContent());

        initRoot(content);

        int elementLevel = 1;
        int symbol = 0;
        while (content.toString().length() != symbol) {
            if (content.charAt(symbol) == TAG_BEGINNING) {
                int lastSymbol = getIndexOf(content, TAG_END, symbol);
                String tagName = content.toString().substring(symbol, lastSymbol);

                symbol = lastSymbol;

                if (isClosingTag(tagName)) {
                    Entity newEntity = formNewEntity(elementLevel);
                    formEntityStack(newEntity);
                    elementLevel--;
                    continue;
                }

                if (elements.peek().getLevel() <= elementLevel) {
                    pushElement(tagName, elementLevel);
                }

                elementLevel++;
            } else {
                int lastSymbol = getIndexOf(content, TAG_BEGINNING, symbol) - 1;
                String tagContent = content.toString().substring(symbol, lastSymbol);
                pushElement(tagContent, elementLevel);
                symbol = lastSymbol;
            }
        }


    }

    private void formEntityStack(Entity entityToPush) {
        if (entityStack.isEmpty()) {
            entityStack.push(entityToPush);
            return;
        }
        while (entityStack.size() != 0 && entityStack.peek().getNestingLevel() > entityToPush.getNestingLevel()) {
            Entity currentEntity = entityStack.pop();
            entityToPush.addChildEntities(currentEntity);
        }
        entityStack.push(entityToPush);
    }

    private Entity formNewEntity(int level) {
        Entity entity = new Entity();

        if (!isTag(elements.peek())) {

            Element element = elements.pop();
            entity.setValue(element.getValue());
        }

        Element element = elements.pop();
        String tagValue = element.getValue();
        if (hasAttributes(tagValue)) {
            Map<String, String> attributes = getAttributes(tagValue);
            entity.setAttributes(attributes);
        }
        String tagName = getTagName(tagValue);
        entity.setName(tagName);
        entity.setNestingLevel(level);

        return entity;

    }

    private boolean isTag(Element element) {
        String elementValue = element.getValue();
        String identifier = "<";
        return elementValue.contains(identifier);

    }

    private String getTagName(String tag) {
        String getTagNameRegExp = "(?<=<)(\\S*)(?=\\s|(?>))";
          Pattern pattern = Pattern.compile(getTagNameRegExp);
        Matcher matcher = pattern.matcher(tag);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;

    }

    private Map<String, String> getAttributes(String tag) {
        Map<String, String> attributes = new HashMap<>();

        String findAttributesRegExp = "(?<=\\s)(\\S+=(\"[^\"]+\"|'[^']+'))(?=\\s|(?>))";
        Pattern pattern = Pattern.compile(findAttributesRegExp);
        Matcher matcher = pattern.matcher(tag);

        int groupNum = 1;
        while (matcher.find()) {
            String attribute = matcher.group(groupNum);
            String attrDelimiter = "=";
            String[] attrNameAndValue = attribute.split(attrDelimiter);

            int attrNameIndex = 0;
            int attrValueIndex = 1;

            String attrName = attrNameAndValue[attrNameIndex];
            String replacement = "";
            String attrValue = attrNameAndValue[attrValueIndex].replaceAll("\"",replacement).replaceAll("'",replacement);

            attributes.put(attrName, attrValue);
        }
        return attributes;
    }

    private String cleanUpAttributeString(String attribute) {
        String extraSymbolsRegEx = "<?\"?\'?>?";
        String replacement = "";
        return attribute.replaceAll(extraSymbolsRegEx, replacement);
    }

    private boolean isClosingTag(String element) {
        String identifier = "/";
        return element.contains(identifier);
    }

    private boolean hasAttributes(String element) {
        String identifier = "=";
        return element.contains(identifier);
    }

    private int getIndexOf(StringBuilder contentBuilder, char character, int symbol) {
        while (contentBuilder.charAt(symbol) != character) {
            symbol++;
        }
        return symbol + 1;
    }

    private void pushElement(String value, int level) {
        Element element = new Element(value, level);
        this.elements.push(element);
    }

    private void initRoot(StringBuilder contentBuilder) {
        String content = contentBuilder.toString();

        char closingTag = '>';
        int stringBeginning = 0;
        int closingTagIndex = content.indexOf(closingTag) + 1;

        String rootTagName = content.substring(stringBeginning, closingTagIndex);

        int topPriority = 1;
        Element rootElement = new Element(rootTagName, topPriority);

        elements.push(rootElement);
        contentBuilder.delete(stringBeginning, closingTagIndex);
    }
}
