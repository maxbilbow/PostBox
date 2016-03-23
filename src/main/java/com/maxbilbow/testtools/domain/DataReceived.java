package com.maxbilbow.testtools.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by bilbowm (Max Bilbow) on 18/03/2016.
 */
@Entity(name = "DataReceived")
public class DataReceived extends GenericDomain<Long>
{
  private String mType;
  private Boolean mXml;
  private String mFileName;
  private DateTime mDateTime;
  private String mAddress;
  private String mContent;

  public DataReceived(){}

  public DataReceived(String aContent, String aType, String aAddress)
  {
    mXml = false;
    mAddress = aAddress;
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

  @Column(columnDefinition = "varchar(255)")
  public String getFileName()
  {
    return mFileName;
  }

  @Column(columnDefinition = "varchar(max)")
  public String getContent()
  {
    return mContent;
  }

  @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
  @Type(type = "com.maxbilbow.testtools.domain.hibernate.JodaDateType")
  @Column
  public DateTime getDateTime()
  {
    return mDateTime;
  }

  @Column(columnDefinition = "varchar(255)", nullable = false)
  public String getAddress()
  {
    return mAddress;
  }

  @Column(columnDefinition = "varchar(10)")
  public String getType()
  {
    return mType;
  }

  @Column(nullable = false)
  public Boolean isXml()
  {
    return mXml;
  }

  public void setAddress(final String aAddress)
  {
    mAddress = aAddress;
  }

  public void setContent(final String aContent)
  {
    mContent = aContent;
  }

  public void setDateTime(final DateTime aDateTime)
  {
    mDateTime = aDateTime;
  }

  public void setFileName(final String aFileName)
  {
    mFileName = aFileName;
  }

  public void setType(final String aType)
  {
    mType = aType;
  }

  public void setXml(final boolean aXml)
  {
    mXml = aXml;
  }
}
