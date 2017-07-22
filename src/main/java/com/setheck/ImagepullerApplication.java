package com.setheck;

import com.setheck.cmd.CliOptionHandler;
import com.setheck.nio.FileSaver;
import com.setheck.parse.RssFeedParser;
import com.setheck.retrieve.LinkRetriever;
import com.setheck.retrieve.RetrievedFile;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImagepullerApplication implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ImagepullerApplication.class);

	private final CliOptionHandler cliOptionHandler = new CliOptionHandler();
	private RssFeedParser rssFeedParser = new RssFeedParser();

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ImagepullerApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		springApplication.setLogStartupInfo(false);
		springApplication.run(args);
	}

	@Override
	public void run(String... args)
	{
		//Parse cmd options
		Map<String,String> parsedArgs = cliOptionHandler.parseArgs(args);
		if (parsedArgs.containsKey("h"))
		{
			cliOptionHandler.buildHelpFormatter();
			return;
		}

		String feedUrl = parsedArgs.getOrDefault("f", "");
		String path = parsedArgs.getOrDefault("p", "");

		if (!Paths.get(path).toFile().exists())
		{
			LOG.info("Path '{}' does not exist, aborting.", path);
			return;
		}

		//If feed was set, retrieve feed content.
		String rssFeed = LinkRetriever.getLink(feedUrl);

		if (StringUtils.isNotEmpty(rssFeed) && StringUtils.isNotEmpty(path))
		{
			InputStream byteArrayInputStream = new ByteArrayInputStream(rssFeed.getBytes());

			FileSaver fileSaver = new FileSaver(path);

			int limit = 0;
			try
			{
				limit = Integer.parseInt(parsedArgs.getOrDefault("l", "0"));
			}
			catch(NumberFormatException nfe)
			{
				LOG.info("Invalid Limit, defaulting to 0");
			}

			rssFeedParser.parseFeed(byteArrayInputStream, limit)
				.parallelStream()
				.map(LinkRetriever::getFile)
				.filter(RetrievedFile::notNull)
				.forEach(fileSaver::saveFile);
		}
		else
		{
			LOG.info("Valid feed and path are required.");
		}
	}
}
