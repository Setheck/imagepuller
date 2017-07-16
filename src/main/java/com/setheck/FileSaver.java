package com.setheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by seth.thompson on 3/28/17.
 */
public class FileSaver
{
    private static final Logger log = LoggerFactory.getLogger(FileSaver.class);

    private String targetPath;

    FileSaver(String targetPath)
    {
        this.targetPath = targetPath;
    }

    private Path buildFilePath(String fileName)
    {
        String strPath;
        if (targetPath.endsWith("/"))
        {
            strPath = targetPath + fileName;
        }
        else
        {
            strPath = targetPath + "/" + fileName;
        }

        return Paths.get(strPath);
    }

    public static String extractFileName(String fullPath)
    {
        if (fullPath.contains("/"))
        {
            int lastSlash = fullPath.lastIndexOf("/");
            return fullPath.substring(lastSlash + 1);
        }

        return fullPath;
    }

    public void saveFile(RetrievedFile retreivedFile)
    {
        saveFile(retreivedFile.getFileName(), retreivedFile.getFileContent());
    }

    public void saveFile(String fileName, byte[] content)
    {
        Path targetFile = buildFilePath(fileName);
        if (Files.exists(targetFile))
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
                log.error(ioe.getMessage());
            }
        }
    }
}
