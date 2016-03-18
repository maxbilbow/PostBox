package com.maxbilbow.testtools.domain;

import org.joda.time.DateTime;

/**
 * Created by bilbowm (Max Bilbow) on 18/03/2016.
 */
public class DataReceived
{
  private String mType;
  private boolean mXml = false;
  private String mFileName;
  private final DateTime mDateTime;
//  "dd/MM/yy HH:mm:ss"
  private final String mContent;

  public DataReceived(String aContent, String aType)
  {
    mDateTime = DateTime.now();
    mContent = aContent;
    mFileName = "Received-"+mDateTime.toString("yy-MM-dd-HHmmss");
    if (aType != null)
    {
      String[] info = aType.split("/");
      mType = info.length == 2 ? info[1] : aType;
    }

    if (mType == null || mType.isEmpty())
    {
      if (mContent.startsWith("<"))
      {
        mXml = true;
      }
      if (mXml)
      {
        if (mContent.startsWith("<?xml"))
        {
          mType += "xml";
        }
        else
        {
          mType += "html";
        }
      }
      else
      {
        mType = "txt";
      }
    }
    mFileName += "."+mType;
  }

  public String getFileName()
  {
    return mFileName;
  }

  public String getContent()
  {
    return mContent;
  }

  public DateTime getDateTime()
  {
    return mDateTime;
  }
}
