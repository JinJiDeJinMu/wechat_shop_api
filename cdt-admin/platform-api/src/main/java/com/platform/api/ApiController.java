package com.platform.api;

import com.chundengtai.base.result.Result;
import com.platform.annotation.IgnoreAuth;
import com.platform.util.ApiPageUtils;
import com.platform.utils.Query;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Api(value = "接口", tags = "接口")
@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    /**
     * 购买人群显示接口
     */
//    @ApiOperation(value = "购买人群显示接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "goodId", value = "用户id", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "pagesize", value = "每页显示数量 ", dataType = "int", paramType = "query")
//    })
//    @ApiResponses({@ApiResponse(code = 200, message = "If successful, this method return JwtAccessTokenVO."),
//            @ApiResponse(code = 400, message = "If bad request."),
//            @ApiResponse(code = 500, message = "If internal server error."),
//            @ApiResponse(code = 503, message = "If service unavailable.")
//    })
//    @PostMapping("/detail/people")
//    @IgnoreAuth
//    public Result<Object> List(@RequestParam Integer goodId,
//                               @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
//                               @RequestParam(value = "pagesize", defaultValue = "10") Integer pagesize
//    ) {
//
//        Map<String, Object> resultObj = new HashMap();
//        List<CommentReq> commentList = new ArrayList();
//        Map param = new HashMap();
//        param.put("page", pageIndex);
//        param.put("limit", pagesize);
//        param.put("goodId", goodId);
//        param.put("order", "desc");
//        param.put("sidx", "id");
//        //查询列表数据
//        Query query = new Query(param);
//        commentList = apiCommentV2Service.queryList(query);
//        int total = apiCommentV2Service.queryTotal(query);
//        ApiPageUtils pageUtil = new ApiPageUtils(commentList, total, query.getLimit(), query.getPage());
//        //
//        for (CommentReq commentItem : commentList) {
//            commentItem.setContent(Base64.decode(commentItem.getContent()));
//            commentItem.setUserInfo(userService.queryObject(commentItem.getUserId()));
//
//            Map paramPicture = new HashMap();
//            paramPicture.put("comment_id", commentItem.getId());
//            List<CommentPictureVo> commentPictureEntities = commentPictureService.queryList(paramPicture);
//            commentItem.setCommentPictureList(commentPictureEntities);
//        }
//        return Result.success(pageUtil);
//    }
}
