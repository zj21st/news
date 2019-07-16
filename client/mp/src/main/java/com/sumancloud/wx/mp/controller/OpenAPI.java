package com.sumancloud.wx.mp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpKefuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;


@RestController
@RequestMapping("/openapi")
public class OpenAPI {
	/**
	 * 测试
	 */
	private WxMpKefuService wxMpKefuService;
	private WxMpUserService wxMpUserService;
	private WxMpService wxMpService;
	
	/*
	 * sdsd
	 */
	private WxMpTemplateMsgService wxMpTemplateMsgService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FastFileStorageClient storageClient;

    
    
    @Autowired
    public OpenAPI(WxMpService wxService) {
        this.wxMpKefuService = wxService.getKefuService();
        this.wxMpTemplateMsgService = wxService.getTemplateMsgService();
        this.wxMpUserService = wxService.getUserService();
        this.wxMpService = wxService;
        //wxService.oauth2buildAuthorizationUrl(redirectURI, scope, state)
    }
    
    
    @GetMapping("/index")
    public String index() {
    	String url = "http://wx.sumancloud.com/#/auth";
    	return wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
    }
    
    @GetMapping("/getwxuser")
    public WxMpUser getWxuser(@RequestParam  String code) {
    	System.out.println("MP Code:"+code);
    	WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
    	WxMpUser wxMpUser = null;
		try {
			wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
			
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}
    	
		try {
			wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
			System.out.println(wxMpUser.getNickname());
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	return wxMpUser;
    }   

	@GetMapping("/hello")
    public String hello(@RequestParam(value="username") String userName,@RequestParam  String title,@RequestParam  String content,@RequestParam  String url) {
		//WxMpKefuMessage message = WxMpKefuMessage.TEXT().toUser(userName).content(content).build();
		
		WxMpKefuMessage.WxArticle article1 = new WxMpKefuMessage.WxArticle();
		article1.setUrl(url);
		article1.setPicUrl("https://s.gravatar.com/avatar/b5a404a2e40c9c596576e1ff12fcf182?s=80");
		article1.setDescription(content);
		article1.setTitle(title);

		WxMpKefuMessage message = WxMpKefuMessage.NEWS()
		    .toUser(userName)
		    .addArticle(article1)
		    .build();

		
		try {
			wxMpKefuService.sendKefuMessage(message);
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return userName;
    }
	
	@GetMapping("/fast")
    public String fast() {

        LOGGER.debug("##上传文件..##");
        File file = new File("d:\\V63开发环境安装与配置.pdf");
        FileInputStream inputStream;
		try {
			inputStream = new FileInputStream (file);
	        StorePath storePath = storageClient.uploadFile(inputStream,file.length(), FilenameUtils.getExtension(file.getName()),null);
	        return storePath.getFullPath();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		}
	
	@GetMapping("/smspic")
    public String smspic(@RequestParam(value="username") String userName,@RequestParam  String title,@RequestParam  String content,@RequestParam  String url) {
		//WxMpKefuMessage message = WxMpKefuMessage.TEXT().toUser(userName).content(content).build();
		
		WxMpKefuMessage message = WxMpKefuMessage
		  .IMAGE()
		  .toUser(userName)
		  .mediaId("MEDIA_ID")
		  .build();



		
		try {
			wxMpKefuService.sendKefuMessage(message);
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return userName;
    }
	
	/**
	 * 模板消息发送
	 * @param userName
	 * @param title
	 * @param content
	 * @param url
	 * @return
	 */
	@GetMapping("/tms")
    public String tms(@RequestParam(value="username") String userName) {
		WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
				.toUser(userName)
				.templateId("O9PcL8Y7KKRtlFnqmQQokSF2sELgZaWypRhbw3KtvwU")
				.url("http://dz.xdkb.net/page/1/2019-06/06/A1/20190606A1_pdf.pdf")
				.build();

				templateMessage.addData(new WxMpTemplateData("first", "尊敬的客户，您的货物我们已经安排车辆运输。当车辆卸货时，请您务必填写签收确认单", "#00008B"));
				templateMessage.addData(new WxMpTemplateData("keyword1", "苏A12345,苏AF2345，苏A12341", "#8B0A50"));
				templateMessage.addData(new WxMpTemplateData("keyword2", "8月30日10:00", "#8B0A50"));
				templateMessage.addData(new WxMpTemplateData("keyword3", "8月30日12:00，时效2小时", "#8B0A50"));
				templateMessage.addData(new WxMpTemplateData("keyword4", "乌克兰切尔诺贝利--日本福岛工业区", "#8B0A50"));
				templateMessage.addData(new WxMpTemplateData("remark", "感谢您的使用", "#FF0000"));
				try {
					wxMpTemplateMsgService.sendTemplateMsg(templateMessage);
					return "message send successful!";
				} catch (WxErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "message sent failed！";
				}
				 
    }
	
	@GetMapping("/getuser")
    public String getuser(@RequestParam(value="username") String userName) {
		String lang = "zh_CN"; //语言
		WxMpUser user = null;
		try {
			user = wxMpUserService.userInfo(userName,lang);
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user.toString()+" UnionId:"+user.getUnionId();
				 
    }


}
