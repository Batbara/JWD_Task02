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
        String content = source.getContent();
        StringBuilder contentBuilder = new StringBuilder(content);

        initRoot(contentBuilder);

        int elementLevel = 1;
        int symbol = 0;
        char tagBeginning = '<';
        char tagEnd = '>';
        while (contentBuilder.toString().length() != symbol) {
            if (contentBuilder.charAt(symbol) == tagBeginning) {
                int lastSymbol = getIndexOf(contentBuilder, tagEnd, symbol);
                String tagName = contentBuilder.toString().substring(symbol, lastSymbol);

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
                int lastSymbol = getIndexOf(contentBuilder, tagBeginning, symbol) - 1;
                String tagContent = contentBuilder.toString().substring(symbol, lastSymbol);
                pushElement(tagContent, elementLevel);
                symbol = lastSymbol;
            }
        }


    }

    private void formEntityStack(Entity entityToPush){
        if(entityStack.isEmpty()){
            entityStack.push(entityToPush);
            return;
        }
        while(entityStack.size()!=0&& entityStack.peek().getNestingLevel()>entityToPush.getNestingLevel()) {
            Entity currentEntity = entityStack.pop();
            entityToPush.addChildEntities(currentEntity);
        }
        entityStack.push(entityToPush);
    }
    private Entity formNewEntity(int level) {
        Entity entity = new Entity();

        if(!isTag(elements.peek())) {

            Element element = elements.pop();
            entity.setValue(element.getValue());
        }

        Element element = elements.pop();
        String tagValue = element.getValue();
        if (hasAttributes(tagValue)){
            Map<String, String> attributes = getAttributes(tagValue);
            entity.setAttributes(attributes);
        }
        String tagName = getTagName(tagValue);
        entity.setName(tagName);
        entity.setNestingLevel(level);

        return entity;

    }

    private boolean isTag(Element element){
        String elementValue = element.getValue();
        String identifier = "<";
        return elementValue.contains(identifier);

    }
    private String getTagName(String tag) {
        String getTagNameRegExp = "(?<=<)(\\S*)(?=.*>)";
        /*if (hasAttributes(tag)) {
            List<String> values = Arrays.asList(tag.split("\\s"));
            int nameIndex = 0;
            tag = values.get(nameIndex);
        }*/
        Pattern pattern = Pattern.compile(getTagNameRegExp);
        Matcher matcher = pattern.matcher(tag);
        if(matcher.find()){
           return matcher.group(1);
        }
        return null;

    }

    private Map<String, String> getAttributes(String tag) {
        Map<String, String> attributes = new HashMap<>();

        String spaceDelimeter = "\\s";
        List<String> stringAttributes = new ArrayList<>();
             stringAttributes.addAll(Arrays.asList(tag.split(spaceDelimeter)));

        int tagNameIndex = 0;
        stringAttributes.remove(tagNameIndex);

        for(String attribute : stringAttributes){
            String cleanAttribute = cleanUpAttributeString(attribute);
            String attrDelimeter = "=";
            String[] attrNameAndValue = cleanAttribute.split(attrDelimeter);

            int attrNameIndex = 0;
            int attrValueIndex = 1;

            String attrName = attrNameAndValue[attrNameIndex];
            String attrValue = attrNameAndValue[attrValueIndex];

            attributes.put(attrName,attrValue);
        }
        return attributes;
    }

    private String cleanUpAttributeString(String attribute){
        String extraSymbolsRegEx = "<?\"?\'?>?";
        String replacement = "";
        return attribute.replaceAll(extraSymbolsRegEx,replacement);
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
