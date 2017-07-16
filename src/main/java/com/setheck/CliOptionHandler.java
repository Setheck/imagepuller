package com.setheck;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by seth.thompson on 3/24/17.
 */

@Component
public class CliOptionHandler
{
    private final Logger log = LoggerFactory.getLogger(CliOptionHandler.class);

    private final Options options = new Options();
    private final HelpFormatter helpFormatter = new HelpFormatter();

    private static Map<String,String> mappedOptions = new HashMap<>();

    CliOptionHandler()
    {
        buildOptions();
        //buildHelpFormatter();
    }

    private void buildOptions()
    {
        options.addOption("f", "feed",true, "target feed");
        options.addOption("p", "path",true, "target path");
        options.addOption("l", "limit",true, "max items to download");
        options.addOption("h", "help",false, "print help");
    }

    private void buildHelpFormatter()
    {
        helpFormatter.printHelp("app", options);
    }

    public Boolean parseArgs(String... args)
    {
        Boolean parsSuccess = true;
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h"))
            {
                buildHelpFormatter();
                return false;
            }

            mappedOptions = Arrays.stream(cmd.getOptions())
                .collect(Collectors.toMap(Option::getOpt, Option::getValue));

            if (!validateFeed(getFeed()))
            {
                throw new ParseException("Invalid Feed Format.");
            }

        }
        catch (ParseException pe)
        {
            parsSuccess = false;
            log.warn(pe.getMessage());
        }

        return parsSuccess;
    }

    public String getFeed()
    {
        return mappedOptions.getOrDefault("f", "");
    }

    public String getPath()
    {
        String path = mappedOptions.getOrDefault("p", "");
        if (!path.endsWith("/"))
        {
            return path + "/";
        }

        return path;
    }

    public int getLimit()
    {
        String limit = mappedOptions.getOrDefault("l", "0");
        try
        {
            return Integer.parseInt(limit);
        }
        catch(NumberFormatException ne)
        {
            //We don't really care about the ne, we just need a default int.
            return 0;
        }
    }

    private static boolean validateFeed(String uri) {
        final URL url;
        try {
            url = new URL(uri);
        } catch (Exception e1) {
            return false;
        }

        return "http".equals(url.getProtocol()) || "https".equalsIgnoreCase(url.getProtocol());
    }
}
