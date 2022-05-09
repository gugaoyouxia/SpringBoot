package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.wujitao.model.domain.Stu;
import generator.service.StuService;
import generator.mapper.StuMapper;
import org.springframework.stereotype.Service;

/**
* @author wujitao
* @description 针对表【stu(用户)】的数据库操作Service实现
* @createDate 2022-04-23 10:20:20
*/
@Service
public class StuServiceImpl extends ServiceImpl<StuMapper, Stu>
    implements StuService{

}




