package com.yilin.csuftspider.controller;

import com.yilin.csuftspider.common.BaseResponse;
import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.common.ResultUtils;
import com.yilin.csuftspider.constant.UrlConstant;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.domain.Notice;
import com.yilin.csuftspider.service.NoticeService;
import com.yilin.csuftspider.utils.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: NoticeController
 * Description: TODO
 * 教务处通知
 * @author Yilin
 * @version V1.0
 * @date 2023-02-19
 */

@RestController //适用于restful风格 json

public class NoticeController {

    @Resource
    NoticeService noticeService;

    @GetMapping("/notice")
    public BaseResponse<List<Notice>> getNotice(){

        List<Notice> lists = noticeService.getNotices();


        return ResultUtils.success(lists);



    }


}
