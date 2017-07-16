package com.setheck;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by seth.thompson on 3/28/17.
 */
public class RetrievedFileTest
{
    @Test
    public void nullObjectTest()
    {
        RetrievedFile retrievedFile = new RetrievedFile();

        assertNull(retrievedFile.getFileContent());
        assertNull(retrievedFile.getFileName());

        assertFalse(retrievedFile.notNull());
    }

    @Test
    public void nonNullObjectTest()
    {
        String fileName = RandomStringUtils.random(10, true, false);
        byte[] content = new byte[]{ 0, 1, 2 };
        RetrievedFile retrievedFile = new RetrievedFile(fileName, content);

        assertEquals(retrievedFile.getFileName(), fileName);
        assertEquals(retrievedFile.getFileContent(), content);

        assertTrue(retrievedFile.notNull());
    }
}
