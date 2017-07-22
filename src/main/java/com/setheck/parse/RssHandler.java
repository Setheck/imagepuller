package com.setheck.parse;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by seth.thompson on 7/21/17.
 */
public class RssHandler extends DefaultHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(RssHandler.class);

    private static final Pattern IMG_MATCH_PATTERN = Pattern.compile("(?i)^.+\\.(jpg|jpeg|png|bmp)$");

    private Set<String> links;

    private final int linkLimit;

    public RssHandler(int linkLimit)
    {
        super();
        this.linkLimit = linkLimit;
        this.links = new HashSet<>(linkLimit);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if (underLimit(linkLimit) && qName.equals("enclosure"))
        {
            String url = attributes.getValue("url");
            if (IMG_MATCH_PATTERN.matcher(url).matches())
            {
                links.add(url);
            }
            else
            {
                LOG.warn("Enclosure Link file type is not supported, skipping: {}", url);
            }
        }
    }

    private boolean underLimit(int limit)
    {
        if (limit == 0)
        {
            return true;
        }

        return links.size() < linkLimit;
    }

    public Set<String> getLinks()
    {
        return links;
    }
}
