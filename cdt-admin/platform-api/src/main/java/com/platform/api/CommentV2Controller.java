package com.platform.api;

import com.chundengtai.base.result.Result;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.CommentPictureVo;
import com.platform.entity.CommentVo;
import com.platform.entity.UserVo;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.utils.Base64;
import com.platform.utils.Query;
import com.platform.utils.StringUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "窝边生活版本", tags = "窝边生活首页")
@RestController
@RequestMapping("/api/v2/comment")
@Slf4j
public class CommentV2Controller extends ApiBaseAction {
    @Autowired
    private ApiCommentService commentService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiCommentPictureService commentPictureService;
    @Autowired
    private ApiCouponService apiCouponService;
    @Autowired
    private ApiUserCouponService apiUserCouponService;

    /**
     * 发表评论
     */
    @ApiOperation(value = "获取评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodId", value = "用户id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pagesize", value = "每页显示数量 ", dataType = "int", paramType = "query")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "If successful, this method return JwtAccessTokenVO."),
            @ApiResponse(code = 400, message = "If bad request."),
            @ApiResponse(code = 500, message = "If internal server error."),
            @ApiResponse(code = 503, message = "If service unavailable.")
    })
    @PostMapping("list")
    public Result<Object> List(@RequestParam Integer goodId,
                               @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                               @RequestParam(value = "pagesize", defaultValue = "10") Integer pagesize

    ) {

        Map resultModel = new HashMap();
        resultModel.put("comment_id", 0);
        return Result.success(resultModel);
    }

    @ApiOperation(value = "回复评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "replyUserId", value = "被回复的用户id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "replyId", value = "回复的评论id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "commentId", value = "评价id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "评论内容", dataType = "long", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "If successful, this method return JwtAccessTokenVO."),
            @ApiResponse(code = 400, message = "If bad request."),
            @ApiResponse(code = 500, message = "If internal server error."),
            @ApiResponse(code = 503, message = "If service unavailable.")
    })
    @PostMapping("reply")
    public Result<Object> reply(@LoginUser UserVo loginUser,
                                Integer replyUserId,
                                Integer replyId,
                                @RequestParam Integer commentId,
                                @RequestParam String content

    ) {


        Map resultModel = new HashMap();
        resultModel.put("comment_id", 0);
        return Result.success(resultModel);
    }

    @ApiOperation(value = "发表评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "用户名", dataType = "string", paramType = "query", example = "2223333"),
            @ApiImplicitParam(name = "goodId", value = "用户id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "用户id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "starLevel", value = "星级", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "用户id", dataType = "long", paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "If successful, this method return JwtAccessTokenVO."),
            @ApiResponse(code = 400, message = "If bad request."),
            @ApiResponse(code = 500, message = "If internal server error."),
            @ApiResponse(code = 503, message = "If service unavailable.")
    })
    @PostMapping("post")
    public Result<Object> post(@LoginUser UserVo loginUser,
                               @RequestParam String orderNo,
                               @RequestParam Integer goodId,
                               @RequestParam String content,
                               @RequestParam Integer starLevel,
                               MultipartFile[] imageList
    ) {


        Map resultModel = new HashMap();
        resultModel.put("comment_id", 0);
        return Result.success(resultModel);
    }

    /**
     */
    @ApiOperation(value = "评论数量")
    @GetMapping("count")
    public Result<Object> count(Integer typeId, Integer valueId) {
        Map<String, Object> resultObj = new HashMap();
        Map param = new HashMap();
        param.put("type_id", typeId);
        param.put("value_id", valueId);
        Integer allCount = commentService.queryTotal(param);
        Integer hasPicCount = commentService.queryhasPicTotal(param);
        resultObj.put("allCount", allCount);
        resultObj.put("hasPicCount", hasPicCount);
        return Result.success(resultObj);
    }


    /**
     * @param typeId
     * @param valueId
     * @param showType 选择评论的类型 0 全部， 1 只显示图片
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "选择评论类型")
    @IgnoreAuth
    @GetMapping("list1")
    public Object list1(Integer typeId, Integer valueId, Integer showType,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                        String sort, String order) {
        Map<String, Object> resultObj = new HashMap();
        List<CommentVo> commentList = new ArrayList();
        Map param = new HashMap();
        param.put("type_id", typeId);
        param.put("value_id", valueId);
        param.put("page", page);
        param.put("limit", size);
        if (StringUtils.isNullOrEmpty(sort)) {
            param.put("order", "desc");
        } else {
            param.put("order", sort);
        }
        if (StringUtils.isNullOrEmpty(order)) {
            param.put("sidx", "id");
        } else {
            param.put("sidx", order);
        }
        if (null != showType && showType == 1) {
            param.put("hasPic", 1);
        }
        //查询列表数据
        Query query = new Query(param);
        commentList = commentService.queryList(query);
        int total = commentService.queryTotal(query);
        ApiPageUtils pageUtil = new ApiPageUtils(commentList, total, query.getLimit(), query.getPage());
        //
        for (CommentVo commentItem : commentList) {
            commentItem.setContent(Base64.decode(commentItem.getContent()));
            commentItem.setUser_info(userService.queryObject(commentItem.getUser_id()));

            Map paramPicture = new HashMap();
            paramPicture.put("comment_id", commentItem.getId());
            List<CommentPictureVo> commentPictureEntities = commentPictureService.queryList(paramPicture);
            commentItem.setPic_list(commentPictureEntities);
        }
        return toResponsSuccess(pageUtil);
    }
}
