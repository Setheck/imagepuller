package com.setheck.parse;

import java.io.InputStream;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by seth.thompson on 3/26/17.
 */
public class RssFeedParserTest
{
    private InputStream inputStream;

    private RssFeedParser rssFeedParser;

    @BeforeClass
    public void setUp()
    {
        rssFeedParser = new RssFeedParser();
        inputStream = ClassLoader.getSystemResourceAsStream("example-feed.xml");
    }

    @Test
    public void parseFeed()
    {
        Set<String> links = rssFeedParser.parseFeed(inputStream, 1);
        Assert.assertTrue(links.contains("http://www.nasa.gov/sites/default/files/thumbnails/image/potw1712a.jpg"));
    }

}
