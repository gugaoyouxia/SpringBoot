package cn.wujitao.service.impl;

import cn.wujitao.common.ErrorCode;
import cn.wujitao.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.wujitao.model.domain.Stu;
import cn.wujitao.service.StuService;
import cn.wujitao.mapper.StuMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.wujitao.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author wujitao
*/
@Service
@Slf4j
public class StuServiceImpl extends ServiceImpl<StuMapper, Stu>
    implements StuService{
    @Resource
    private StuMapper stuMapper;
    /**
     * 混淆值
     */
    private final String SALT = "wujitao";

    @Override
    public long userRegister(String userAccount, String userPassword, String userCheckPassword) {
        //1.校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, userCheckPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }
        if(userPassword.length() < 8 || userCheckPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            return -4;
        }
        if(!userPassword.equals(userCheckPassword)){
            return -5;
        }
        //账户不能重复
        QueryWrapper<Stu> stuQueryWrapper = new QueryWrapper<>();
        stuQueryWrapper.eq("userAccount", userAccount);
        long count = this.count(stuQueryWrapper);
        if(count > 0){
            return -6;
        }

        // 2.加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        Stu stu = new Stu();
        stu.setUserPassword(newPassword);
        stu.setUserAccount(userAccount);
        boolean saveResult = this.save(stu);
        if(!saveResult){
            return -7;
        }
        return stu.getId();
    }

    @Override
    public Stu doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        if(userAccount.length() < 4){
            return null;
        }
        if(userPassword.length() < 8){
            return null;
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            return null;
        }
        // 2.加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<Stu> stuQueryWrapper = new QueryWrapper<>();
        stuQueryWrapper.eq("userAccount", userAccount);
        stuQueryWrapper.eq("userPassword", newPassword);
        Stu stu = stuMapper.selectOne(stuQueryWrapper);
        //用户不存在
        if(stu == null){
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        //用户脱敏
        Stu safeStu = getSafeStu(stu);
        //记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, stu);
        return safeStu;
    }
    @Override
    public Stu getSafeStu(Stu originStu){
        if(originStu == null){
            return null;
        }
        Stu safeStu = new Stu();
        safeStu.setId(originStu.getId());
        safeStu.setUsername(originStu.getUsername());
        safeStu.setUserAccount(originStu.getUserAccount());
        safeStu.setAvatarUrl(originStu.getAvatarUrl());
        safeStu.setGender(originStu.getGender());
        safeStu.setEmail(originStu.getEmail());
        safeStu.setUserRole(originStu.getUserRole());
        safeStu.setUserStatus(originStu.getUserStatus());
        safeStu.setCreateTime(originStu.getCreateTime());
        return safeStu;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




