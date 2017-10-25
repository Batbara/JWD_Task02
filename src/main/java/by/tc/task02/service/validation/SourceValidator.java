package by.tc.task02.service.validation;

import java.net.URL;

public class SourceValidator {
    public static boolean isSourceNameValid(String sourceName){
        ClassLoader classLoader = SourceValidator.class.getClassLoader();
        URL sourceURL = classLoader.getResource(sourceName);
        return sourceURL != null;
    }
}
