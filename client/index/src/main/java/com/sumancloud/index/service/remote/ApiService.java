package com.sumancloud.index.service.remote;

import com.sumancloud.mp.WxMpUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mp")
public interface ApiService {

    @RequestMapping(value = "/openapi/tms",method = RequestMethod.GET)
    String hello(@RequestParam("username") String username) ;

    @RequestMapping(value = "/openapi/getwxuser",method = RequestMethod.GET)
    WxMpUser getWxMpUser(@RequestParam("code") String code) ;

    /*
    @RequestMapping("/hello")
    String hello();

    @RequestMapping(value = "/hellol", method= RequestMethod.GET)
    String hello(@RequestParam("name") String name) ;

    @RequestMapping(value = "/hello2", method= RequestMethod.GET)
    User hello(@RequestHeader("name") String name, @RequestHeader("age") Integer age);

    @RequestMapping(value = "/hello3", method= RequestMethod.POST)
    String hello(@RequestBody User user);
     */
}