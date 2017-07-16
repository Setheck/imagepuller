package com.setheck;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * Created by seth.thompson on 3/26/17.
 */
public class RssFeedParserTest
{
    private InputStream inputStream;

    @Resource
    private RssFeedParser rssFeedParser;

    @BeforeClass
    public void setUp()
    {
        //inputStream = ClassLoader.getSystemResourceAsStream("example-feed.xml");
    }

    @Test
    public void happyPath()
    {
        //List<String> links = rssFeedParser.parseFeed(inputStream);

        //assertFalse(links.isEmpty());
    }
}
