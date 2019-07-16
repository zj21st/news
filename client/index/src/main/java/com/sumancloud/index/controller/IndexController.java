package com.sumancloud.index.controller;

import com.sumancloud.index.lock.AquiredLockWorker;
import com.sumancloud.index.lock.DistributedLocker;
import com.sumancloud.index.service.remote.ApiService;
import com.sumancloud.mp.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx")
public class IndexController {

    @Autowired
    private DistributedLocker distributedLocker;

    @Autowired
    ApiService apiService;

    @RequestMapping(value = "msg",method = RequestMethod.GET)
    public String index(){
        String str1 = apiService.hello("oojVP1hmH-pAD5pTr_Ml53ai5ovg");
        return str1;
    }

    @RequestMapping(value = "getuser",method = RequestMethod.GET)
    public WxMpUser getWxMpUser(@RequestParam  String code){
        System.out.println(code);
        WxMpUser wxMpUser = apiService.getWxMpUser(code);
        System.out.println(wxMpUser.getNickname());
        return wxMpUser;
    }

    @RequestMapping("testlock")
    public String testlock()throws Exception{
        distributedLocker.lock("test",new AquiredLockWorker<Object>() {

            @Override
            public Object invokeAfterLockAquire() {
                try {
                    System.out.println("执行方法！");
                    Thread.sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

        });
        return "hello world!";
    }
}
