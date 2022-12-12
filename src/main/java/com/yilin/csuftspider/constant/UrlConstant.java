package com.yilin.csuftspider.constant;

/**
 * Title: UrlConstant
 * Description: TODO
 * URL 常量  学校更新 webvpn 版可以远程访问 查看以前版本请版本回退
 * @version V1.0
 * @date 2022-12-12
 */
public interface UrlConstant {


//    self.host = "http://jwgl.csuft.edu.cn"
//    self.login = "http://authserver.csuft.edu.cn/authserver/login?service=http%3A%2F%2Fjwgl.csuft.edu.cn%2F"
//    self.checkNow = self.host + '/jsxsd/kscj/cjcx_frm'
//    self.checkLevel = self.host + '/jsxsd/kscj/djkscj_list'
//    self.checkAll = self.host + '/jsxsd/kscj/cjcx_list'
//    self.checkCourse = self.host + '/jsxsd/xskb/xskb_list.do'
//    self.checkExameNow = self.host + '/jsxsd/xsks/xsksap_query'
//    self.checkExame = self.host + '/jsxsd/xsks/xsksap_list'
//    self.checkEvaluation = self.host + '/jsxsd/xspj/xspj_find.do?Ves632DSdyV=NEW_XSD_JXPJ'

    /**
     * 基础   url
     */

    String BASE_URL = "http://jwgl.webvpn.csuft.edu.cn";

    /**
     * 登录 url
     */

    String LOGIN_URL = "http://authserver.webvpn.csuft.edu.cn/authserver/login?service=http%3A%2F%2Fwebvpn.csuft.edu.cn%2Fusers%2Fauth%2Fcas%2Fcallback%3Furl%3Dhttp%253A%252F%252Fwebvpn.csuft.edu.cn%252F";

    /**
     * 课程成绩 url (可以带参数）
     */

    String GRADES_TABEL_URL = BASE_URL + "/jsxsd/kscj/cjcx_list";

    /**
     * 本学期课程成绩 url （不用参数）
     */

    String CURRENT_GRADES_TABEL_URL = BASE_URL + "/jsxsd/kscj/cjcx_frm";

    /**
     * 考试安排信息 url
     */


    String EXAME_INFO_FORM_URL =  BASE_URL + "/jsxsd/xsks/xsksap_list";

    /**
     * 等级考试成绩 url
     */

    String LEVEL_GRADES_TABLE_URL = BASE_URL + "/jsxsd/kscj/djkscj_list";

    /**
     * 课表 url
     */

    String COURSE_TABEL_URL = BASE_URL + "/jsxsd/xskb/xskb_list.do";





}
