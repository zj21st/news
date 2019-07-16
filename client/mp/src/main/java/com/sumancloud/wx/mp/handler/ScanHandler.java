package com.sumancloud.wx.mp.handler;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sumancloud.wx.mp.builder.TextBuilder;
import com.sumancloud.wx.mp.utils.JsonUtils;

import me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class ScanHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, 
    								Map<String, Object> map,
                                    WxMpService wxMpService, 
                                    WxSessionManager wxSessionManager) throws WxErrorException {
        // 扫码事件处理



        if (!wxMpXmlMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            if (StringUtils.startsWithAny(wxMpXmlMessage.getContent(), "你好", "客服")
                && wxMpService.getKefuService().kfOnlineList()
                .getKfOnlineList().size() > 0) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
                    .fromUser(wxMpXmlMessage.getToUser())
                    .toUser(wxMpXmlMessage.getFromUser()).build();
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        //TODO 组装回复消息
        //String content = "收到信息内容：" + JsonUtils.toJson(wxMpXmlMessage);
        String content = "二维码里面的数据：" + wxMpXmlMessage.getEventKey()+",接收人："+wxMpXmlMessage.getFromUser();
        
        
        return new TextBuilder().build(content, wxMpXmlMessage, wxMpService);

     
    }
}
