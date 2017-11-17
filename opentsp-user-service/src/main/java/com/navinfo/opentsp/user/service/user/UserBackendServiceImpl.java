package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.common.util.date.DateUtil;
import com.navinfo.opentsp.user.dal.dao.UserEntityDao;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.service.email.EmailService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.param.backend.QueryUserListParam;
import com.navinfo.opentsp.user.service.param.backend.ResetPasswordParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.PageResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.backend.QueryUserListResult;
import com.navinfo.opentsp.user.service.security.PasswordService;
import com.navinfo.opentsp.user.service.sms.SmsService;
import com.navinfo.opentsp.user.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * Created by wupeng on 11/3/15.
 */
@Service
public class UserBackendServiceImpl implements UserBackendService {

    @Autowired
    private UserEntityDao userEntityDao;

    private static final char[] passwordChars = new char[] {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', '!', '@', '$', '#', '(', ')', '1', '2', '3', '4', '5', '6', '7',
            '9', '9', '0'
    };

    @Value("${opentsp.admin.resetpwd.length:8}")
    private Integer length;
    @Autowired
    private SmsService smsService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private TokenService tokenService;

    private Random random = new Random();

    @Override
    public PageResult<QueryUserListResult> query(QueryUserListParam param) {
        if (param.getPageNo() <= 0) {
            param.setPageNo(1);
        }
        if (param.getPageSize() <= 0 || param.getPageSize() >= 1000) {
            param.setPageSize(20);
        }
        String order=null;
        String orderType=String.valueOf(param.getOrderType());

        if(StringUtils.isEmpty(param.getOrder())){
            order="id";
        }else {
            order=param.getOrder();
        }



        List<QueryUserListResult> listResults = new LinkedList<>();
        PageResult pageResult = new PageResult();


        long count = this.userEntityDao.queryCount(param.getKey(), param.getStartDate(),
                param.getEndDate(), param.getBindInfo());
        if (count > 0) {
            List<UserEntity> userEntities = this.userEntityDao.query(param.getKey(),
                    param.getStartDate(), param.getEndDate(), param.getBindInfo(), param.getPageNo(), param.getPageSize(),order,orderType);
            for (UserEntity userEntity : userEntities) {
                QueryUserListResult result = new QueryUserListResult();
                result.setId(userEntity.getId());
                result.setEmail(userEntity.getEmail());
                result.setMobile(userEntity.getMobile());
                result.setNickname(userEntity.getNickname());
                result.setRegisterDate(DateUtil.formatDate(userEntity.getCreateTime()));
                listResults.add(result);
            }
        }

        pageResult.fillResult(ResultCode.SUCCESS);
        pageResult.setData(listResults);
        pageResult.setTotalNum(count);
        pageResult.setPageSize(param.getPageSize());
        pageResult.calculateTotalPage();

        return pageResult;
    }

    @Transactional
    @Override
    public CommonResult<String> resetPassword(ResetPasswordParam passwordParam) {
        UserEntity userEntity = this.userEntityDao.findById(passwordParam.getUserId());
        if (userEntity == null)
            return new CommonResult<String>().fillResult(ResultCode.USER_NOT_EXISTS);

        String password = this.randomPassword();

        userEntity.setPassword(passwordService.encodePassword(password, userEntity.getSalt()));
        this.userEntityDao.updateById(userEntity);

        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        map.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        if (passwordParam.getType() == 0) {
            emailService.notify(passwordParam.getNotifier(), Functions.ADMIN_RESET_PASSWD.emailSubject(),
                    Functions.ADMIN_RESET_PASSWD.func(), map);
        } else if (passwordParam.getType() == 1) {
            smsService.notify(passwordParam.getNotifier(), Functions.ADMIN_RESET_PASSWD.func(), map);
        }

        tokenService.removeUserToken(passwordParam.getUserId());
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    private String randomPassword() {
        StringBuilder sb = new StringBuilder();
        int len = passwordChars.length;
        for (int i = 0; i < length; i++) {
            char ch = passwordChars[random.nextInt(len)];
            if (random.nextInt(100) % 2 == 0) {
                sb.append(String.valueOf(ch).toUpperCase());
            } else {
                sb.append(String.valueOf(ch).toLowerCase());
            }
        }
        return sb.toString();
    }
}
