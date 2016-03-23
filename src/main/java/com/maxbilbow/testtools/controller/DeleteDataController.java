package com.maxbilbow.testtools.controller;

import com.maxbilbow.testtools.service.DataReceivedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by bilbowm (Max Bilbow) on 23/03/2016.
 */
@RestController
public class DeleteDataController
{
  private Logger mLogger = LoggerFactory.getLogger(getClass());

  @Resource
  private DataReceivedService mDataReceivedService;

  @RequestMapping(value = "/", params = "delete", method = RequestMethod.GET)
  public Object deleteAll()
  {
    mLogger.warn("DELETING ALL DATA");
    return mDataReceivedService.deleteAll()!= null;
  }

  @RequestMapping(value = "/{address}" ,params = "delete", method = RequestMethod.GET)
  public Object deleteAllFromAddress(@PathVariable("address")String aAddress)
  {
    mLogger.warn("DELETING ALL DATA FOR /"+aAddress);
    return mDataReceivedService.deleteAllWithAddress(aAddress)!= null;
  }

  @RequestMapping(value = "/", params = {"delete","pk"},method = RequestMethod.GET)
  public Object deleteOne(@RequestParam("pk") Long aPk)
  {
    mLogger.warn("DELETING DATA WITH PK: "+aPk);

    return mDataReceivedService.delete(aPk) != null;
  }
}
