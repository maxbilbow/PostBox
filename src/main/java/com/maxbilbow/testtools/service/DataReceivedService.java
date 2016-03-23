package com.maxbilbow.testtools.service;

import com.maxbilbow.testtools.dao.DataReceivedDao;
import com.maxbilbow.testtools.domain.DataReceived;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bilbowm (Max Bilbow) on 18/03/2016.
 */
@Service
public class DataReceivedService
{

  Logger mLogger = LoggerFactory.getLogger(getClass());

  @Resource
  private DataReceivedDao mDataReceivedDao;

  @Resource
  private TransactionTemplate mTransactionTemplate;

  public DataReceived getLastFileForAddress(String aAddress)
  {
    final List<DataReceived> data = mTransactionTemplate.execute(status -> mDataReceivedDao.findWithAddress(aAddress));
    return data.isEmpty() ? null : data.get(0);
  }

  @Transactional
  public DataReceived newDataReceived(String aAddress, String aContent, String aType)
  {
    DataReceived data = new DataReceived(aContent,aType,aAddress);
    mTransactionTemplate.execute(status -> mDataReceivedDao.makePersistent(data));
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
