package cn.wujitao.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求体
 * @author wujitao
 */
@Data
public class UserLoginRequest implements Serializable{
    @Serial
    private static final long serialVersionUID = 4238936485146791439L;
    private String userAccount;
    private String userPassword;
}
