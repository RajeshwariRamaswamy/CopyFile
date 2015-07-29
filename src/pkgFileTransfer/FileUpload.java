package pkgFileTransfer;

import org.apache.commons.net.ftp.FTPClient; 
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import java.io.FileInputStream;
import java.io.IOException; 
import java.io.FileOutputStream; 
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;


public class FileUpload {
    static FileOutputStream fileOutput = null; 
    static Properties prop ;
    static String downloadPath;
    
  public static void main(String[] args) throws IOException { 
	  
	    String downloadOption= null; 
	    InputStream input = null;
	    prop = new Properties();
	  	input = new FileInputStream("Config.properties");
			// load a properties file
		prop.load(input);
		downloadOption = prop.getProperty("DownLoadOption");
	    switch (downloadOption)
	    {
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
  public static void downloadFileFromURL() throws IOException {    
	  FileOutputStream fos = null;
      ReadableByteChannel rbc = null;
      try {
    	String urlString = prop.getProperty("urlString");
    	downloadPath = prop.getProperty("DownLoadPath");
    	
          URL websiteURL = new URL(urlString);
          rbc = Channels.newChannel(websiteURL.openStream());
          fos = new FileOutputStream(downloadPath);
          fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

      } catch (IOException e) {
          e.printStackTrace();
      }
      finally{
    	  if(fos != null)
    	  {
    	        fos.close();
    	  }
    	  if(rbc != null)
    	  {
    		  rbc.close();
    	  }
      }
  }
  public static void downloadFieFromFTP()
  {
	    FTPClient ftpClient = new FTPClient(); 
        String dirToSearch ;
	    final String containingFileName ;
	    String FTPDomainName;
	    String FTPUserName;
	    String FTPPassword;
	    

  try { 
		FTPDomainName = prop.getProperty("FTPHostName");
		FTPUserName = prop.getProperty("FTPUserName");
		FTPPassword = prop.getProperty("FTPPassword");
		dirToSearch = prop.getProperty("FTPDirectory");
		containingFileName = prop.getProperty("FileName");
		downloadPath = prop.getProperty("DownLoadPath");

  	ftpClient.connect(FTPDomainName);
    // Pass username and password 
  	ftpClient.login(FTPUserName, FTPPassword); 
    
    FTPFileFilter filter = new FTPFileFilter() {
  	  
  	    @Override
  	    public boolean accept(FTPFile ftpFile) {
  	 
  	        return (ftpFile.isFile() && ftpFile.getName().contains(containingFileName));
  	    }
  	};
  	 FTPFile[] result = ftpClient.listFiles(dirToSearch, filter);

  	 for(FTPFile ftpFile : result)
  	 {
  		 String fileName =ftpFile.getName();
  		 fileOutput = new FileOutputStream(downloadPath + "/" + fileName); 
         // Download file from FTP server 
           ftpClient.retrieveFile(dirToSearch + "/" + fileName, fileOutput); 
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