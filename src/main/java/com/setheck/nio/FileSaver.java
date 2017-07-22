package com.setheck.nio;

import com.setheck.retrieve.RetrievedFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by seth.thompson on 3/28/17.
 */
public class FileSaver
{
    private static final Logger log = LoggerFactory.getLogger(FileSaver.class);

    private final String FILE_SEPARATOR = System.getProperty("file.separator");

    private String targetPath;

    public FileSaver(String targetPath)
    {
        this.targetPath = targetPath;
    }

    private Path buildFilePath(String fileName)
    {
        String strPath;
        if (targetPath.endsWith(FILE_SEPARATOR))
        {
            strPath = targetPath + fileName;
        }
        else
        {
            strPath = targetPath + FILE_SEPARATOR + fileName;
        }

        return Paths.get(strPath);
    }

    public void saveFile(RetrievedFile retreivedFile)
    {
        saveFile(retreivedFile.getFileName(), retreivedFile.getFileContent());
    }

    public void saveFile(String fileName, byte[] content)
    {
        Path targetFile = buildFilePath(fileName);
        if (targetFile.toFile().exists())
        {
            log.warn("File already exists, skipping: {}", targetFile);
        }
        else
        {
            log.info("Saving file: {}", targetFile);
            try
            {
                Files.write(targetFile, content);
            }
            catch(IOException ioe)
            {
                log.error("Error Saving file: ", ioe);
            }
        }
    }
}
