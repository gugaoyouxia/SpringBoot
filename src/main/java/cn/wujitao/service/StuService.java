package cn.wujitao.service;

import cn.wujitao.model.domain.Stu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wujitao
* @description 针对表【stu(用户)】的数据库操作Service
* @createDate 2022-04-20 14:28:58
*/
public interface StuService extends IService<Stu> {
    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param userCheckPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String userCheckPassword);
}
