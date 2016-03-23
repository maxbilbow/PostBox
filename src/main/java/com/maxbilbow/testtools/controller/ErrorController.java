package com.maxbilbow.testtools.controller;

import com.maxbilbow.testtools.service.DataReceivedService;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by bilbowm (Max Bilbow) on 23/03/2016.
 */
//@Controller
@RequestMapping("/error")
//@RequestMapping("${server.error.path:${error.path:/error}")
public class ErrorController extends AbstractErrorController
{

  private final ErrorProperties errorProperties;

  @Resource
  private DataReceivedService mDataReceivedService;

  //  public ErrorController()
//  {
//    super(null);
//  errorProperties = null;
//  }

  /**
   * Create a new {@link BasicErrorController} instance.
   * @param errorAttributes the error attributes
   * @param errorProperties configuration properties
   */
  public ErrorController(ErrorAttributes errorAttributes,
                              ErrorProperties errorProperties) {
    super(errorAttributes);
    Assert.notNull(errorProperties, "ErrorProperties must not be null");
    this.errorProperties = errorProperties;
  }

  @Override
  public String getErrorPath() {
    return this.errorProperties.getPath();
  }

  @RequestMapping(produces = "text/html")
  public ModelAndView errorHtml(HttpServletRequest request,
                                HttpServletResponse response) {
    response.setStatus(getStatus(request).value());
    Map<String, Object> model = getErrorAttributes(request,

                                                   isIncludeStackTrace(request, MediaType.TEXT_HTML));
    Set<String> urls = new HashSet<>();
    mDataReceivedService.findAll().forEach(aDataReceived -> urls.add(aDataReceived.getAddress()));
    return new ModelAndView("error", model)
            .addObject("urls", new ArrayList<>(urls))
            .addObject("pageTitle","PostBoxHelp");
  }

  @RequestMapping
  @ResponseBody
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    Map<String, Object> body = getErrorAttributes(request,
                                                  isIncludeStackTrace(request, MediaType.ALL));
    HttpStatus status = getStatus(request);
    return new ResponseEntity<Map<String, Object>>(body, status);
  }

  /**
   * Determine if the stacktrace attribute should be included.
   * @param request the source request
   * @param produces the media type produced (or {@code MediaType.ALL})
   * @return if the stacktrace attribute should be included
   */
  protected boolean isIncludeStackTrace(HttpServletRequest request,
                                        MediaType produces) {
    IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
    if (include == IncludeStacktrace.ALWAYS) {
      return true;
    }
    if (include == IncludeStacktrace.ON_TRACE_PARAM) {
      return getTraceParameter(request);
    }
    return false;
  }

  /**
   * Provide access to the error properties.
   * @return the error properties
   */
  protected ErrorProperties getErrorProperties() {
    return this.errorProperties;
  }
}
