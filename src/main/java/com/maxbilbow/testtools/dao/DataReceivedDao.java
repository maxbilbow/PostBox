package com.maxbilbow.testtools.dao;

import com.maxbilbow.testtools.domain.DataReceived;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bilbowm (Max Bilbow) on 21/03/2016.
 */
@Repository
public class DataReceivedDao extends GenericDao<DataReceived>
{
//  Map<String, DataReceived> mLastFileReceived = new HashMap<>();

  public List<DataReceived> findWithAddress(final String aAddress)
  {
    return getSession().createQuery("From DataReceived WHERE address = :aAddress" +
                                    " ORDER BY dateTime desc ")
            .setString("aAddress",aAddress)
            .list();
  }

//  public DataReceived makePersistent(final DataReceived aData)
//  {
//    return mLastFileReceived.put(aData.getAddress(),aData);
//  }
}
