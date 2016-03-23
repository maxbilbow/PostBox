package com.maxbilbow.testtools.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by bilbowm (Max Bilbow) on 23/03/2016.
 */
@MappedSuperclass
public abstract class GenericDomain<IdType extends Number> implements Serializable
{
  private IdType mPk;
  private Integer mVersion;
  private DateTime mLastUpdated;

  @Id
  @GeneratedValue
  @Column(name = "PK", nullable = false)
  public IdType getPk()
  {
    return mPk;
  }

  public void setPk(IdType aPk)
  {
    mPk = aPk;
  }

  @Version
  @Column(name = "VERSION")
  public Integer getVersion()
  {
    return mVersion;
  }

  public void setVersion(Integer aVersion)
  {
    mVersion = aVersion;
  }

  @Transient
  public String byteToString(byte[] aContent)
  {
    try
    {
      return new String(aContent, "UTF-8");
    }
    catch(UnsupportedEncodingException aEx)
    {
      throw new RuntimeException(aEx);
    }
  }

  @Transient
  public byte[] stringToByte(String aContent)
  {
    try
    {
      return aContent.getBytes("UTF-8");
    }
    catch(UnsupportedEncodingException aEx)
    {
      throw new RuntimeException(aEx);
    }
  }

  @Column(name = "LAST_UPDATED")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  public DateTime getLastUpdated()
  {
    return mLastUpdated;
  }

  public void setLastUpdated(final DateTime aLastUpdated)
  {
    mLastUpdated = aLastUpdated;
  }
}

