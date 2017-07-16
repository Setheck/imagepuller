package com.setheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class ImagepullerApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ImagepullerApplication.class);

	@Resource
	private CliOptionHandler cliOptionHandler;

	@Resource
	private RssFeedParser rssFeedParser;

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ImagepullerApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		springApplication.setLogStartupInfo(false);
		springApplication.run(args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		//Parse cmd options
		if (!cliOptionHandler.parseArgs(args))
		{
			return;
		}

		String rssFeed = "";
		//If feed was set, retrieve feed content.
		if (!cliOptionHandler.getFeed().isEmpty())
		{
			rssFeed = LinkRetriever.getLink(cliOptionHandler.getFeed());
		}

		//If path was set, ensure that it exists.
		if (!cliOptionHandler.getPath().isEmpty())
		{
			if (!Files.exists(Paths.get(cliOptionHandler.getPath())))
			{
				log.warn("Target path does not exist, aborting.");
				return;
			}
		}

		if (cliOptionHandler.getLimit() > 0)
		{
			rssFeedParser.setLinkLimit(cliOptionHandler.getLimit());
		}

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rssFeed.getBytes());

		//InputStream inputStream = ClassLoader.getSystemResourceAsStream("example-feed.xml");
		FileSaver fileSaver = new FileSaver(cliOptionHandler.getPath());

		rssFeedParser.parseFeed(byteArrayInputStream)
			.stream()
			.map(LinkRetriever::getFile)
			.filter(RetrievedFile::notNull)
			.forEach(fileSaver::saveFile);


	}
}
