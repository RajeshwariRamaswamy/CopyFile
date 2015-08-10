package com.file.transfer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.log4j.Logger;

public class FileUpload {
	private static final Logger LOGGER = Logger.getLogger(FileUpload.class);
	private FileOutputStream fileOutput = null;
	private final ConfigurationProperties properties = new ConfigurationProperties();

	public static void main(String[] args) {
		FileUpload fileUpload = new FileUpload();
		try {
			fileUpload.transferFiles();
		} catch (IOException ex) {
			LOGGER.error("An IOException has occured due to : " + ex.getCause());
		}
	}

	public void transferFiles() throws IOException {
		switch (properties.getDownloadOption()) {
		case "FTP":
			downloadFieFromFTP();
			break;
		case "URL":
			downloadFileFromURL();
			break;
		default:
			break;
		}

	}

	private void downloadFileFromURL() throws IOException {
		ReadableByteChannel rbc = null;
		try {
			URL websiteURL = new URL(properties.getUrlString());
			rbc = Channels.newChannel(websiteURL.openStream());
			fileOutput = new FileOutputStream(properties.getDownloadPath());
			fileOutput.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileOutput != null) {
				fileOutput.close();
			}
			if (rbc != null) {
				rbc.close();
			}
		}
	}

	private void downloadFieFromFTP() {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(properties.getFtpDomainName());
			// Pass username and password
			ftpClient.login(properties.getFtpUserName(),
					properties.getFtpPassword());

			FTPFileFilter filter = new FTPFileFilter() {

				@Override
				public boolean accept(FTPFile ftpFile) {
					return (ftpFile.isFile() && ftpFile.getName().contains(
							properties.getContainingFileName()));
				}
			};
			FTPFile[] result = ftpClient.listFiles(properties.getDirToSearch(),
					filter);

			for (FTPFile ftpFile : result) {
				String fileName = ftpFile.getName();
				fileOutput = new FileOutputStream(properties.getDownloadPath()
						+ "/" + fileName);
				// Download file from FTP server
				ftpClient.retrieveFile(properties.getDirToSearch() + "/"
						+ fileName, fileOutput);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileOutput != null) {
					fileOutput.close();
				}
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}