package cn.wujitao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.wujitao.model.domain.Stu;
import cn.wujitao.service.StuService;
import cn.wujitao.mapper.StuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author wujitao
*/
@Service
public class StuServiceImpl extends ServiceImpl<StuMapper, Stu>
    implements StuService{

    @Override
    public long userRegister(String userAccount, String userPassword, String userCheckPassword) {
        //1.校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, userCheckPassword)){
            return -1;
        }
        if(userAccount.length() < 4){
            return -2;
        }
        if(userPassword.length() < 8 || userCheckPassword.length() < 8){
            return -3;
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
        final String SALT = "wujitao";
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
}




