
package com.chundengtai.base.controller;

import com.chundengtai.base.annotation.APPLoginUser;
import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.entity.MlsUserEntity2;
import com.chundengtai.base.entity.UserRecord;
import com.chundengtai.base.service.UserRecordSer;
import com.chundengtai.base.util.ApiBaseAction;
import com.chundengtai.base.util.ApiPageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 分销用户<br>
 */
@Api(tags = "分销用户")
@Controller
@RequestMapping("/api/userRecord")
public class UserRecordCtr extends ApiBaseAction {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRecordSer userRecordSer;

    /**
     * 分页获取品牌
     */
    @ApiOperation(value = "分页获取品牌")
    @IgnoreAuth
    @RequestMapping("list")
    @ResponseBody
    public Object list(UserRecord userRecord, @APPLoginUser MlsUserEntity2 mlsuser) {
        if (userRecord.getPage() == null) {
            userRecord.setPage(1);
            userRecord.setSize(10);
        }
        userRecord.setOffset((userRecord.getPage() - 1) * userRecord.getSize());

        userRecord.setMlsUserId(mlsuser.getMlsUserId());
        List<UserRecord> rliset = userRecordSer.getEntityMapper().findByEntity(userRecord);
        Long total = userRecordSer.getEntityMapper().findByEntity_count(userRecord);
        ApiPageUtils pageUtil = new ApiPageUtils(rliset, total.intValue(), userRecord.getSize(), userRecord.getPage());
        return toResponsSuccess(pageUtil);
    }
}

