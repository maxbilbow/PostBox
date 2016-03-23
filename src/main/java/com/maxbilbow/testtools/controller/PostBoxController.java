package com.maxbilbow.testtools.controller;

import com.maxbilbow.testtools.domain.DataReceived;
import com.maxbilbow.testtools.service.DataReceivedService;
import com.maxbilbow.testtools.util.PBFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bilbowm (Max Bilbow) on 09/03/2016.
 */
@Controller
@RequestMapping
public class PostBoxController// extends AbstractView
{

  @Resource
  private PBFileWriter mFileWriter;

  @Resource
  Environment mEnvironment;

  @Resource
  DataReceivedService mDataReceivedService;

  private Logger mLogger = LoggerFactory.getLogger(getClass());



  @RequestMapping(value = "/",method = RequestMethod.POST)
  public
  @ResponseBody
  String post(HttpServletRequest aRequest,
                       HttpServletResponse aResponse)
  {
    return post(aRequest,aResponse,"index");
  }


  @RequestMapping(value = "/{address}", method = RequestMethod.POST)
  public
  @ResponseBody
  String post(HttpServletRequest aRequest,
                       HttpServletResponse aResponse,
                       @PathVariable("address") String aAddress
  )
  {
    FileWriter writer = null;
    DataReceived data;
    try
    {
      String body = aRequest.getParameter("body");
      String type = aRequest.getParameter("type");
      if (body == null || body.isEmpty())
      {
        byte[] bytes = new byte[aRequest.getContentLength()];

        aRequest.getInputStream().read(bytes);

        data = mDataReceivedService.newDataReceived(aAddress,bytes);
//        body = data.getContent();
      }
      else
      {
        data = mDataReceivedService.newDataReceived(aAddress,body,type);
      }


      mLogger.debug("Creating file with content: " + data.getContent());
      mFileWriter.createFile(data);

      return data.getContent();//getModelAndView(aAddress).addObject("message","Received data with size: " + body.length());
    } catch (Exception e)
    {
      mLogger.error("Failed to get message: " + e.getMessage());
      throw new RuntimeException();
    }
    finally
    {
      if (writer != null)
      {
        try
        {
          writer.flush();
        }
        catch (IOException aE)
        {
          mLogger.error("Failed to flush content",aE);
        }
        try
        {
          writer.close();
        }
        catch (IOException aE)
        {
          mLogger.error("Failed to close file writer",aE);
        }
      }
    }
  }

  @RequestMapping(value = "/{address}", method = RequestMethod.GET)
  public ModelAndView getModelAndView(@PathVariable("address") String aAddress)
  {
    if (aAddress == null || aAddress.isEmpty())
    {
      aAddress = "index";
    }
    List dataList = mDataReceivedService.getDataWithAddress(aAddress);
    if (dataList.isEmpty() || aAddress.equalsIgnoreCase("PostBoxHelp"))
    {
      return getHelp(aAddress);
    }
    return new ModelAndView("postbox")
            .addObject("dataList",mDataReceivedService.getDataWithAddress(aAddress))
            .addObject("pageTitle",aAddress)
            .addObject("postPath",aAddress)
            .addObject("serverPort",mEnvironment.getProperty("server.port"));
  }

  @RequestMapping(value = {"/","PostBoxHelp"}, method = RequestMethod.GET)
  public ModelAndView getHelp(String aAddress)
  {
    if (aAddress == null || aAddress.isEmpty())
    {
      aAddress = "PostBox";
    }
    Set<String> urls = new HashSet<>();
    mDataReceivedService.findAll().forEach(aDataReceived -> urls.add(aDataReceived.getAddress()));
    return new ModelAndView("help")
            .addObject("urls", new ArrayList<>(urls))
            .addObject("pageTitle",aAddress + " (help)")
            .addObject("postPath",aAddress)
            .addObject("serverPort",mEnvironment.getProperty("server.port"));
  }



//  @Deprecated
//  private String getPageBody(String aTitle, DataReceived aLastReceived)
//  {
//    final String content;
//    if (aLastReceived == null)
//    {
//      content = "\n      No files received. Direct posts to: " +
//                "\n      <br/>http://localhost:" + mEnvironment.getProperty("server.port") + "/{id)" +
//                "\n      <br/> where {id} is the unique url you want to test";
//    }
//    else
//    {
//      content = "\n      <p>" +
//                "\n        LAST FILE RECEIVED: " + aLastReceived.getFileName() +
//                "\n      </p>" +
//                "\n      <xmp>" +
//                "\n" +  aLastReceived.getContent() +
//                "\n      </xmp>";
//    }
//    return "<html>" +
//           "\n  <body>" +
//           "\n    <h1>/" + aTitle + "</h1>" +
//           content +
//           "\n  </body>" +
//           "\n</html>";
//  }



}
