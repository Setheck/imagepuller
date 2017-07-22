package com.setheck.cmd;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by seth.thompson on 3/24/17.
 */

public class CliOptionHandler
{
    private final Logger LOG = LoggerFactory.getLogger(CliOptionHandler.class);

    private static Options options = new Options();
    static
    {
        options.addOption("f", "feed",true, "target feed");
        options.addOption("p", "path",true, "target path");
        options.addOption("l", "limit",true, "max items to download");
        options.addOption("h", "help",false, "print help");
    }
    private final HelpFormatter helpFormatter = new HelpFormatter();

    public void buildHelpFormatter()
    {
        helpFormatter.printHelp("app", options);
    }

    public Map<String,String> parseArgs(String... args)
    {
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options, args);
            return Arrays.stream(cmd.getOptions())
                .collect(Collectors.toMap(Option::getOpt, Option::getValue));
        }
        catch (ParseException pe)
        {
            LOG.warn(pe.getMessage());
        }

        return Collections.emptyMap();
    }
}
