package com.maxbilbow.testtools.service;

import com.maxbilbow.testtools.domain.DataReceived;
import com.maxbilbow.testtools.util.PBFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bilbowm (Max Bilbow) on 18/03/2016.
 */
@Service
public class DataReceivedService
{
  Map<String, DataReceived> mLastFileReceived = new HashMap<>();


  Logger mLogger = LoggerFactory.getLogger(getClass());

  @Resource
  private PBFileWriter mFileWriter;

  public DataReceived getLastFileForAddress(String aAddress)
  {
    return mLastFileReceived.get(aAddress);
  }

  public DataReceived newDataReceived(String aAddress, String aContent, String aType)
  {
    DataReceived data = new DataReceived(aContent,aType);
    mLastFileReceived.put(aAddress,data);
    mFileWriter.createFile(data);
    return data;
  }

  public DataReceived newDataReceived(String aAddress, final byte[] aBytes)
  {
    String content = new String(aBytes);
    if (content.isEmpty())
    {
      throw new RuntimeException(content);
    }
    return newDataReceived(aAddress,content,null);
  }
}
