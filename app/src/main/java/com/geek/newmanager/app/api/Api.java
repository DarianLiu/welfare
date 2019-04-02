package com.geek.newmanager.app.api;

/**
 * ================================================
 * 存放一些与 API 有关的东西,如请求地址,请求码等
 * Created by Administrator on 2017/10/23.
 * ================================================
 */
public interface Api {
    /**
     * 测试环境与生产环境切换
     **/
    boolean RELEASE_VERSION = false;

    /**
     * 服务器IP地址
     */
    String IP = RELEASE_VERSION ? "192.168.207.66" : "192.168.207.66";

    /**
     * 端口号(用户/网格相关)
     */
    String PORT_USER = RELEASE_VERSION ? ":8016" : ":8016";

    /**
     * 端口号(上传文件)
     */
    String PORT_FILE_UPLOAD = RELEASE_VERSION ? ":8088" : ":8088";

    /**
     * 端口号(案件/地区相关)
     */
    String PORT_CASE = RELEASE_VERSION ? ":8016" : ":8016";

    /**
     * 用户相关的服务器URL
     */
    String USER_DOMAIN_NAME = "user";
    String URL_USER = "http://" + IP + PORT_USER;

    /**
     * 案件相关的服务器URL
     */
//    String CASE_DOMAIN_NAME = "case";
    String BASE_URL = "http://" + IP + PORT_CASE;

    /**
     * 上传文件的服务器URL
     */
    String FILE_UPLOAD_DOMAIN_NAME = "file_upload";
//    String URL_FILE_UPLOAD = "http://" + IP + PORT_FILE_UPLOAD;
    String URL_FILE_UPLOAD = "http://221.180.255.233:8088";
}
