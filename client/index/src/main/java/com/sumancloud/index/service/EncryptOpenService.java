package com.sumancloud.index.service;

import com.sumancloud.common.result.SingleResult;
import com.sumancloud.common.utils.RSAUtils;
import com.sumancloud.index.request.KeyRequest;
import com.sumancloud.index.response.KeyResponse;
import com.sumancloud.index.response.RSAResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EncryptOpenService{

    @Value("${rsa.publicKey}")
    private String publicKey;
    @Value("${rsa.privateKey}")
    private String privateKey;
    @Value("${api.encrypt.key}")
    private String key;

    public SingleResult<RSAResponse> getRSA() {
        RSAResponse response = RSAResponse.options()
                .setServerPublicKey(publicKey)
                .build();
        return SingleResult.buildSuccess(response);
    }

    public SingleResult<KeyResponse> getKey(KeyRequest request)throws Exception {
        String clientPublicKey = RSAUtils.privateDecrypt(request.getClientEncryptPublicKey(), RSAUtils.getPrivateKey(privateKey));
        String encryptKey = RSAUtils.publicEncrypt(key,RSAUtils.getPublicKey(clientPublicKey));
        KeyResponse response = KeyResponse.options()
                .setKey(encryptKey)
                .build();
        return SingleResult.buildSuccess(response);
    }
}
