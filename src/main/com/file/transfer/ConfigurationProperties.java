package com.file.transfer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigurationProperties {
	private static final Logger LOGGER = Logger
			.getLogger(ConfigurationProperties.class);
	private Properties prop;
	private String downloadPath;
	private String downloadOption = null;
	private String urlString;
	private String ftpDomainName;
	private String ftpUserName;
	private String ftpPassword;
	private String dirToSearch;
	private String containingFileName;

	public ConfigurationProperties() {
		try {
			InputStream input = ConfigurationProperties.class.getClassLoader()
					.getResourceAsStream("Config.properties");
			prop = new Properties();
			prop.load(input);
			urlString = prop.getProperty("urlString");
			downloadOption = prop.getProperty("DownLoadOption");
			ftpDomainName = prop.getProperty("FTPHostName");
			ftpUserName = prop.getProperty("FTPUserName");
			ftpPassword = prop.getProperty("FTPPassword");
			dirToSearch = prop.getProperty("FTPDirectory");
			containingFileName = prop.getProperty("FileName");
			downloadPath = prop.getProperty("DownLoadPath");
		} catch (FileNotFoundException ex) {
			LOGGER.error("FileNotFoundException Exception occured due to :"
					+ ex.getCause());
		} catch (IOException ex) {
			LOGGER.error("IOException Exception occured due to :"
					+ ex.getCause());
		}

	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public String getDownloadOption() {
		return downloadOption;
	}

	public String getUrlString() {
		return urlString;
	}

	public String getFtpDomainName() {
		return ftpDomainName;
	}

	public String getFtpUserName() {
		return ftpUserName;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public String getDirToSearch() {
		return dirToSearch;
	}

	public String getContainingFileName() {
		return containingFileName;
	}

}
