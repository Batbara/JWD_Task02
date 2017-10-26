package by.tc.task02.dao.impl;

import by.tc.task02.entity.Entity;
import by.tc.task02.entity.Source;
import by.tc.task02.entity.Element;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityCreator {

    private static final char TAG_BEGINNING = '<';
    private static final char TAG_END = '>';

    private Source source;
    private Deque<Element> elements;
    private Deque<Entity> entityStack;
    private int currentNestingLevel;


    public EntityCreator() {
        elements = new ArrayDeque<>();
        entityStack = new ArrayDeque<>();
        currentNestingLevel = 1;
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
        clearProlog(content);
        initRoot(content);

        int currentSymbol = 0;
        int contentLength = content.toString().length();
        while (contentLength != currentSymbol) {
            char currentCharSymbol = content.charAt(currentSymbol);

            if (currentCharSymbol == TAG_BEGINNING) {

                int lastTagSymbol = getIndexOf(TAG_END, content, currentSymbol);
                String tagName = content.toString().substring(currentSymbol, lastTagSymbol);
                processTag(tagName);

                currentSymbol = lastTagSymbol;
            } else {
                int lastElementSymbol = getIndexOf(TAG_BEGINNING, content, currentSymbol) - 1;
                String elementValue = content.toString().substring(currentSymbol, lastElementSymbol);
                pushElement(elementValue.trim(), currentNestingLevel);

                currentSymbol = lastElementSymbol;
            }
        }
    }

    private void clearProlog(StringBuilder content) {
        String prologRegExp = "<(\\?|!)[^>]*>";
        Pattern pattern = Pattern.compile(prologRegExp);
        Matcher matcher = pattern.matcher(content.toString());

        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            content.delete(startIndex, endIndex);
        }
    }

    private void initRoot(StringBuilder contentBuilder) {
        String content = contentBuilder.toString();

        int stringBeginning = 0;
        int closingTagIndex = content.indexOf(TAG_END) + 1;

        String rootTagName = content.substring(stringBeginning, closingTagIndex);

        currentNestingLevel = 1;
        Element rootElement = new Element(rootTagName, currentNestingLevel);
        elements.push(rootElement);

        contentBuilder.delete(stringBeginning, closingTagIndex);
    }

    private int getIndexOf(char character, StringBuilder content, int symbol) {
        while (content.charAt(symbol) != character) {
            symbol++;
        }
        return symbol + 1;
    }

    private boolean isTag(Element element) {
        String elementValue = element.getValue();
        String identifier = "<";
        return elementValue.contains(identifier);

    }

    private boolean isClosingTag(String element) {
        String identifier = "</";
        return element.contains(identifier);
    }

    private String getTagName(String tag) {
        String getTagNameRegExp = "(?<=<)(\\S*)(?=\\s|>)";

        Pattern pattern = Pattern.compile(getTagNameRegExp);
        Matcher matcher = pattern.matcher(tag);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private void processTag(String tagName) {

        if (isClosingTag(tagName)) {
            Entity newEntity = formNewEntity(currentNestingLevel);
            addToEntityStack(newEntity);
            currentNestingLevel--;
            return;
        }

        pushElement(tagName, currentNestingLevel);
        currentNestingLevel++;
    }

    private boolean hasAttributes(String element) {
        String identifier = "=";
        return element.contains(identifier);
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
            String attrValue = attrNameAndValue[attrValueIndex];

            String formattedAttrValue = formatAttributeValue(attrValue);

            attributes.put(attrName, formattedAttrValue);
        }
        return attributes;
    }

    private String formatAttributeValue(String attribute) {
        String quotes = "\"";
        String singleQuotes = "'";
        String replacement = "";
        return attribute.replaceAll(quotes, replacement).replaceAll(singleQuotes, replacement);
    }

    private void pushElement(String value, int level) {
        Element element = new Element(value, level);
        this.elements.push(element);
    }


    private Entity formNewEntity(int level) {
        Entity entity = new Entity();

        setEntityValue(entity);

        Element element = elements.pop();
        if (!isTag(element)) {
            return null;
        }

        String tag = element.getValue();

        if (hasAttributes(tag)) {
            Map<String, String> attributes = getAttributes(tag);
            entity.setAttributes(attributes);
        }

        String tagName = getTagName(tag);
        entity.setName(tagName);
        entity.setNestingLevel(level);

        return entity;

    }

    private void setEntityValue(Entity entity) {
        Element topElement = elements.peek();
        if (!isTag(topElement)) {
            Element element = elements.pop();
            entity.setValue(element.getValue());
        }
    }

    private void addToEntityStack(Entity entityToPush) {
        if (entityStack.isEmpty()) {
            entityStack.push(entityToPush);
            return;
        }

        int entityToPushNestingLevel = entityToPush.getNestingLevel();

        while (!entityStack.isEmpty() && entityStack.peek().getNestingLevel() > entityToPushNestingLevel) {

            Entity entityFromStack = entityStack.pop();
            entityToPush.addChildEntity(entityFromStack);
        }

        entityStack.push(entityToPush);
    }
}
