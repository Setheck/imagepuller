package com.setheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by seth.thompson on 3/24/17.
 */
@Component
public class RssFeedParser
{
    private static final Logger log = LoggerFactory.getLogger(RssFeedParser.class);

    private static final Pattern IMG_MATCH_PATTERN = Pattern.compile("(?i)^.+\\.(jpg|jpeg|png|bmp)$");

    private static int linkLimit = 0;

    public void setLinkLimit(int limit)
    {
        linkLimit = limit;
    }

    public List<String> parseFeed(InputStream inputStream)
    {
        List<String> links = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try
        {
            SAXParser parser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler()
            {
                public void startElement(String uri,
                    String localName,
                    String qName,
                    Attributes attributes)
                {
                    if (linkLimit > 0 && links.size() >= linkLimit)
                    {
                        return;
                    }

                    if (qName.equals("enclosure"))
                    {
                        String url = attributes.getValue("url");
                        if (IMG_MATCH_PATTERN.matcher(url).matches())
                        {
                            links.add(url);
                        }
                        else
                        {
                            log.warn("Enclosure Link file type is not supported, skipping: {}", url);
                        }
                    }
                }
            };

            parser.parse(inputStream, handler);
        }
        catch(SAXException|ParserConfigurationException|IOException e)
        {
            //log.error(e.getMessage());
            log.warn("Invalid Feed, cannot parse");
        }

        return links;
    }
}
