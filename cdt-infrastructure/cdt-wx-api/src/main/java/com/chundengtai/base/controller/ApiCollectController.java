package com.chundengtai.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.chundengtai.base.annotation.LoginUser;
import com.chundengtai.base.entity.CollectVo;
import com.chundengtai.base.entity.UserVo;
import com.chundengtai.base.service.ApiCollectService;
import com.chundengtai.base.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "用户收藏")
@RestController
@RequestMapping("/api/collect")
public class ApiCollectController extends ApiBaseAction {
    @Autowired
    private ApiCollectService collectService;

    /**
     * 获取用户收藏
     */
    @ApiOperation(value = "获取用户收藏")
    @GetMapping("list")
    public Object list(@LoginUser UserVo loginUser, Integer typeId) {

        Map param = new HashMap();
        param.put("user_id", loginUser.getUserId());
        param.put("type_id", typeId);
        List<CollectVo> collectEntities = collectService.queryList(param);
        return toResponsSuccess(collectEntities);
    }

    /**
     * 获取用户收藏
     */
    @ApiOperation(value = "添加取消收藏")
    @PostMapping("addordelete")
    public Object addordelete(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        Integer typeId = jsonParam.getInteger("typeId");
        Integer valueId = jsonParam.getInteger("valueId");

        Map param = new HashMap();
        param.put("user_id", loginUser.getUserId());
        param.put("type_id", typeId);
        param.put("good_id", valueId);
        List<CollectVo> collectEntities = collectService.queryList(param);
        //
        Integer collectRes = null;
        String handleType = "add";
        if (null == collectEntities || collectEntities.size() < 1) {
            CollectVo collectEntity = new CollectVo();
            collectEntity.setAdd_time(new Date());
            collectEntity.setType_id(typeId);
            collectEntity.setGood_id(valueId);
            collectEntity.setIs_attention(0);
            collectEntity.setUser_id(loginUser.getUserId());
            //添加收藏
            collectRes = collectService.save(collectEntity);
        } else {
            //取消收藏
            collectRes = collectService.delete(collectEntities.get(0).getId());
            handleType = "delete";
        }

        if (collectRes > 0) {
            Map data = new HashMap();
            data.put("type", handleType);
            return toResponsSuccess(data);
        }
        return toResponsFail("操作失败");
    }
}