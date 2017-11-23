package com.acooly.openapi.framework.core.auth.realm;

/**
 * @author qiubo@yiji.com
 */
public interface AppUserService {
    String findSecretKeyByAccessKey(String accessKey);
}
