package com.maxbilbow.testtools.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bilbowm (Max Bilbow) on 09/03/2016.
 */
@Controller
@RequestMapping
public class PostBoxController extends AbstractView
{

  @Resource
  Environment mEnvironment;


  Map<String, Data> mLastFileReceived = new HashMap<>();

  DateTimeFormatter mFileDateTimeFormatter = DateTimeFormat.forPattern("yy-MM-dd-HHmmss");
  DateTimeFormatter mWebDateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss");

  private Logger mLogger = LoggerFactory.getLogger(getClass());

  private File mOutputDirectory;

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

  @RequestMapping(value = "/",method = RequestMethod.POST)
  public
  @ResponseBody
  ModelAndView post(HttpServletRequest aRequest,
                       HttpServletResponse aResponse)
  {
    return post(aRequest,aResponse,"index");
  }


  @RequestMapping(value = "/{address}", method = RequestMethod.POST)
  public
  @ResponseBody
  ModelAndView post(HttpServletRequest aRequest,
                       HttpServletResponse aResponse,
                       @PathVariable("address") String aAddress
  )
  {
    FileWriter writer = null;
    try
    {
      byte[] bytes = new byte[aRequest.getContentLength()];

      aRequest.getInputStream().read(bytes);

      Data data = storeBytes(bytes);
      mLastFileReceived.put(aAddress,data);
      mLogger.debug("File content: " + data.mContent);

      File file = Files.createFile(Paths.get(mOutputDirectory.getPath() + "/" + data.mFileName)).toFile();
      writer =new FileWriter(file);
      writer.write(new String(bytes));
      return getModelAndView(aAddress).addObject("message","Received data with size: " + bytes.length);
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

  private Data storeBytes(final byte[] aBytes)
  {
    String content = new String(aBytes);
    if (content.isEmpty())
    {
      throw new RuntimeException(content);
    }
    mLogger.debug(content);
    return new Data(content);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView get(final HttpServletRequest aRequest,
                            final HttpServletResponse aResponse)
  {
    return get(aRequest,aResponse,"index");
    }

  @RequestMapping(value = "/{address}", method = RequestMethod.GET)
  public ModelAndView get(
          final HttpServletRequest aRequest,
          final HttpServletResponse aResponse,
          @PathVariable("address") String aAddress)
  {

//   aRequest.getSession().setAttribute("address", aAddress);
   return getModelAndView(aAddress);//.addObject("address",aAddress);
  }

  @Override
  protected void renderMergedOutputModel(final Map<String, Object> aMap,
                                         final HttpServletRequest aHttpServletRequest,
                                         final HttpServletResponse aHttpServletResponse) throws Exception
  {
    String message;
    String address = (String) aMap.get("address");//(String) aHttpServletRequest.getAttribute("address");
    Data lastReceived = mLastFileReceived.get(address);

    if (lastReceived != null)
    {
      message = "<html>" +
                "<body>" +
                "<h1>/" + address + "</h1>" +
                "LAST FILE RECEIVED: " + lastReceived.mDateTime.toString(mWebDateTimeFormatter) +
                "<br/>" +
                lastReceived.getForHtml() +
                "</body>" +
                "</html>";
    }
    else
    {
      message = "<html>" +
                "<body>" +
                "<h1>/"+address+"</h1>" +
                "No files received. Direct posts to: " +
                "<br/>http://localhost:"+mEnvironment.getProperty("server.port")+"/{id)" +
                "<br/> where {id} is the unique url you want to test" +
                "</body>" +
                "</html>";
    }
//    aHttpServletResponse.setHeader("Content-Disposition", "attachment; filename="+aFileName);
    aHttpServletResponse.setContentType("text/html");
    aHttpServletResponse.getWriter().write(message);
  }

  public ModelAndView getModelAndView(String aAddress)
  {
    getAttributesMap().put("address", aAddress);
    ModelAndView mav = new ModelAndView();
    mav.setView(this);
    return mav;
  }

  class Data {
    boolean mXml = false;
    Data(String aContent)
    {
      mDateTime = DateTime.now();
      mContent = aContent;
      mFileName = "Received-"+mDateTime.toString(mFileDateTimeFormatter);
      if (mContent.startsWith("<"))
      {
        mXml = true;
      }
      if (mContent.startsWith("<?xml"))
      {
        mFileName += ".xml";
      }
      else
      {
        mFileName += ".txt";
      }
    }
    String mFileName;
    DateTime mDateTime;
    String mContent;
    String getForHtml()
    {
     if (mXml)
     {
       return "<xmp>" +
              mContent +
              "</xmp";
     }
      else
     {
       return "<p>"+
              mContent +
              "</p>";
     }
    }
  }
}
