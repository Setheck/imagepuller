package com.setheck;

/**
 * Created by seth.thompson on 3/28/17.
 */
public class RetrievedFile
{
    private String fileName = null;
    private byte[] fileContent = null;

    RetrievedFile()
    {
    }

    RetrievedFile(String fileName, byte[] fileContent)
    {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public String getFileName()
    {
        return fileName;
    }

    public byte[] getFileContent()
    {
        return fileContent;
    }

    public Boolean notNull()
    {
        return (fileContent != null || fileName != null);
    }
}
