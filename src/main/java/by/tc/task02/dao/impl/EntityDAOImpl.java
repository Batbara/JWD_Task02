package by.tc.task02.dao.impl;

import by.tc.task02.dao.DAOException;
import by.tc.task02.dao.EntityDAO;
import by.tc.task02.entity.Entity;
import by.tc.task02.entity.Source;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EntityDAOImpl implements EntityDAO {


    public Entity parseSource(String sourceName) throws DAOException {
        Entity root;

        String sourcePath = getSourcePath(sourceName);
        try {
            Source source = readSourceFile(sourcePath, Charset.defaultCharset());

            EntityCreator entityCreator = new EntityCreator();
            entityCreator.setSource(source);

            root = entityCreator.getRootEntity();

        } catch (IOException e) {
            throw new DAOException("IOException!", e);
        }
        return root;
    }

    private Source readSourceFile(String path, Charset encoding)
            throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(path));
        String fileString = new String(fileBytes, encoding);

        String spacesAndNewLinesRegExp = "\\r?\\n?(?<=>)(\\s*)(?=<)";
        String replacement = "";
        String sourceContent = fileString.replaceAll(spacesAndNewLinesRegExp, replacement);

        Source source = new Source();
        source.setContent(sourceContent);

        return source;
    }

    private String getSourcePath(String sourceName) {

        ClassLoader classLoader = getClass().getClassLoader();
        URL sourceURL = classLoader.getResource(sourceName);
        String rawPath = sourceURL.getPath();

        String incorrectDriveLetterRegEx = "^/(.:/)";
        String capturedGroup = "$1";
        String correctPath = rawPath.replaceFirst(incorrectDriveLetterRegEx, capturedGroup);

        return correctPath;

    }

}
