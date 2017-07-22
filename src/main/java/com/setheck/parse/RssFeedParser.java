package com.setheck.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Created by seth.thompson on 3/24/17.
 */
public class RssFeedParser
{
    private static final Logger LOG = LoggerFactory.getLogger(RssFeedParser.class);

    public Set<String> parseFeed(InputStream inputStream, int linkLimit)
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try
        {
            SAXParser parser = factory.newSAXParser();
            RssHandler rssHandler = new RssHandler(linkLimit);
            parser.parse(inputStream, rssHandler);

            return rssHandler.getLinks();
        }
        catch(SAXException|ParserConfigurationException|IOException e)
        {
            LOG.warn("Invalid Feed, cannot parse");
        }

        return Collections.emptySet();
    }
}
