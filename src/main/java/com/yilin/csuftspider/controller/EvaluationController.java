package com.yilin.csuftspider.controller;

import com.yilin.csuftspider.common.BaseResponse;
import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.common.ResultUtils;
import com.yilin.csuftspider.constant.ConstantData;
import com.yilin.csuftspider.controller.utils.SessionCheck;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.User;
import com.yilin.csuftspider.model.request.EvaluationCommitRequest;
import com.yilin.csuftspider.model.response.GradeStatus;
import com.yilin.csuftspider.service.EvaluationService;
import com.yilin.csuftspider.service.UserService;
import com.yilin.csuftspider.utils.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Title: EvaluationController
 * Description: TODO
 * 自动评教
 * @author Yilin
 * @version V1.0
 * @date 2023-03-08
 */

@RestController //适用于restful风格 json
@RequestMapping("/evaluation")
@Slf4j
public class EvaluationController {

    @Resource
    EvaluationService evaluationService;

    //检查是否有需要评教
    @GetMapping("/getStatus")
    public BaseResponse<Integer> getStatus(HttpServletRequest request){

        // 登陆检查
        Session mySession = SessionCheck.isAlive(request);
        User user =(User) request.getSession().getAttribute(UserService.USER_LOGIN_INFO);

        if(user == null || user.getSid()==null){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"请重新登录");
        }

        // 需要评教的数量

        List<String> nums = evaluationService.getNums(mySession);

        return  ResultUtils.success(nums.size());



    }

    //检查是否有需要评教
    @PostMapping("/commit")
    public BaseResponse<Boolean> commit(@RequestBody EvaluationCommitRequest evaluationCommitRequest, HttpServletRequest request){


        if(evaluationCommitRequest==null||evaluationCommitRequest.getContext()==null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");

        }
        String text = evaluationCommitRequest.getContext();
        if(text.length()<=50){

            text = ConstantData.UVALUATION_BASE;
        }
        // 登陆检查
        Session mySession = SessionCheck.isAlive(request);
        User user =(User) request.getSession().getAttribute(UserService.USER_LOGIN_INFO);

        if(user == null || user.getSid()==null){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"请重新登录");
        }

        boolean commit = evaluationService.commit(mySession, text);

        if(commit){
            log.info(user.getName() + ""+user.getSid()+"评教成功");
        }


       return ResultUtils.success(commit);




    }




}
