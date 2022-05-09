package cn.wujitao.controller;

import cn.wujitao.common.BaseResponse;
import cn.wujitao.common.ErrorCode;
import cn.wujitao.common.ResultUtils;
import cn.wujitao.exception.BusinessException;
import cn.wujitao.model.domain.Stu;
import cn.wujitao.model.domain.request.UserLoginRequest;
import cn.wujitao.model.domain.request.UserRegisterRequest;
import cn.wujitao.service.StuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import static cn.wujitao.constant.UserConstant.ADMIN_ROLE;
import static cn.wujitao.constant.UserConstant.USER_LOGIN_STATE;


/**
 * @author wujitao
 */


@RestController
@RequestMapping("/stu")
public class UserController {
    @Resource
    private StuService stuService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }
        long result = stuService.userRegister(userAccount, userPassword, checkPassword);
        return new BaseResponse<>(0, result, "ok");
    }

    @PostMapping("/login")
    public BaseResponse<Stu> userLogin(@RequestBody UserLoginRequest userloginRequest, HttpServletRequest request){
        // attention
        if(userloginRequest == null){
            return null;
        }
        String userAccount = userloginRequest.getUserAccount();
        String userPassword = userloginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        Stu stu = stuService.doLogin(userAccount, userPassword, request);
        return ResultUtils.success(stu);
    }
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request == null){
            return null;
        }
        int result = stuService.userLogout(request);
        return ResultUtils.success(result);

    }
    @GetMapping("/current")
    public BaseResponse<Stu> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        Stu currentStu = (Stu) userObj;
        if(currentStu == null){
            return null;
        }
        long userId = currentStu.getId();
        // todo 校验用户是否合法
        Stu stu = stuService.getById(userId);
        Stu safeStu = stuService.getSafeStu(stu);
        return ResultUtils.success(safeStu);

    }
    @GetMapping("/search")
    public BaseResponse<List<Stu>> searchUsers(String username, HttpServletRequest request){
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Stu> stuQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            stuQueryWrapper.like("username", username);
        }
        List<Stu> stuList = stuService.list(stuQueryWrapper);

        List<Stu> collect = stuList.stream().map(stu -> stuService.getSafeStu(stu)).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){

        if(!isAdmin(request)){
            return null;
        }
        if(id <= 0){

            return null;
        }
        boolean b = stuService.removeById(id);
        return ResultUtils.success(b);
    }

    private boolean isAdmin(HttpServletRequest request){
        //仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        Stu stu = (Stu) userObj;
        return stu != null && stu.getUserRole() == ADMIN_ROLE;

    }


}

