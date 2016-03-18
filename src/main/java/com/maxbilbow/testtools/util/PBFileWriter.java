package com.maxbilbow.testtools.util;

import com.maxbilbow.testtools.domain.DataReceived;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by bilbowm (Max Bilbow) on 18/03/2016.
 */
@Component
public class PBFileWriter
{

  private Logger mLogger = LoggerFactory.getLogger(getClass());

  private File mOutputDirectory;

  @Resource
  Environment mEnvironment;

  @PostConstruct
  public void init() throws IOException
  {
    mOutputDirectory = new File(mEnvironment.getProperty("postbox.outDir"));

    if(!mOutputDirectory.exists())
    {
      mLogger.info("Creating new rejection folder at " + mOutputDirectory.getPath());
      Files.createDirectories(mOutputDirectory.toPath());
    }
  }

  public void createFile(final DataReceived aData)
  {
    FileWriter writer = null;
    try
    {
      File file = Files.createFile(Paths.get(mOutputDirectory.getPath() + "/" + aData.getFileName())).toFile();
      writer = new FileWriter(file);
      writer.write(aData.getContent());
    } catch (IOException e)
    {
      mLogger.error("Failed to write file",e);
    }
    finally
    {
      if (writer != null)
      {
        try
        {
          writer.flush();
          writer.close();
        }
        catch (Exception e)
        {
          mLogger.error("Failed to close file writer",e);
        }
      }
    }
  }
}
