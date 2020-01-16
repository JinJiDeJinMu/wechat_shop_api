package com.chundengtai.base.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.bean.CdtProductComment;
import com.chundengtai.base.bean.CommentPicture;
import com.chundengtai.base.entity.CommentReq;
import com.chundengtai.base.entity.RepCommentVo;
import com.chundengtai.base.entity.UserVo;
import com.chundengtai.base.oss.OSSFactory;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.*;
import com.chundengtai.base.util.ApiBaseAction;
import com.chundengtai.base.utils.Base64;
import com.chundengtai.base.utils.RRException;
import com.chundengtai.base.weixinapi.WeixinContants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "窝边生活商品评论", tags = "窝边生活商品评论")
@RestController
@RequestMapping("/api/v2/comment")
@Slf4j
public class WxCommentV2Controller extends ApiBaseAction {

    @Autowired
    private ApiCommentService commentService;
    @Autowired
    private ApiRepCommentService apiRepCommentService;

    @Autowired
    private ApiUserService userService;

    @Autowired
    private CdtProductCommentService cdtProductCommentService;

    @Autowired
    private CommentNewPictureService commentPictureService;

    /**
     * 获取评价列表（在商品详情里面）
     */
    @ApiOperation(value = "获取评价列表")
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
    @GetMapping("list")
    @IgnoreAuth
    public Result<Object> List(@RequestParam Integer goodId,
                               @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                               @RequestParam(value = "pagesize", defaultValue = "10") Integer pagesize
    ) {
        PageHelper.startPage(pageIndex, pagesize);
        List<CdtProductComment> list = cdtProductCommentService.list(new LambdaQueryWrapper<CdtProductComment>()
                .eq(CdtProductComment::getGoodId, goodId).eq(CdtProductComment::getStatus, 0));
        List<CommentReq> commentReqList = list.stream()
                .map(e -> JSONUtil.toBean(JSONUtil.toJsonStr(e), CommentReq.class))
                .collect(Collectors.toList());
        for (CommentReq cdtProductComment : commentReqList) {
            List<CommentPicture> commentPictureList = commentPictureService.list(new LambdaQueryWrapper<CommentPicture>()
                    .eq(CommentPicture::getStatus, 1)
                    .eq(CommentPicture::getCommentId, cdtProductComment.getId()));
            if (commentPictureList != null) {
                commentPictureList = commentPictureList.stream()
                        .sorted(Comparator.comparing(CommentPicture::getSortOrder))
                        .collect(Collectors.toList());
            }
            cdtProductComment.setCommentPictureList(commentPictureList);
            UserVo userVo = userService.queryObject(cdtProductComment.getUserId());
            userVo.setNickname(Base64.decode(userVo.getNickname()));
            cdtProductComment.setUserInfo(userVo);
        }
        commentReqList = commentReqList.stream().sorted(Comparator.comparing(CommentReq::getCreateTime).reversed()).collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(commentReqList);
        return Result.success(pageInfo);
    }


    @ApiOperation(value = "发表评价")
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
    @IgnoreAuth
    @GetMapping("post")
    @ResponseBody
    public Result<Object> post(
            @RequestParam Integer userId,
            @RequestParam Integer orderNo,
            @RequestParam Integer goodId,
            @RequestParam String content,
            @RequestParam Integer starLevel,
            @RequestParam String imageList
    ) {
        List<String> list = JSONObject.parseArray(imageList, String.class);
        log.info("=====评论" + list);
        CdtProductComment commentReq = new CdtProductComment();
        commentReq.setCommentTime(Long.valueOf(System.currentTimeMillis() / 1000));
        commentReq.setCreateTime(new Date());
        commentReq.setUserId(userId);
        commentReq.setOrderNo(orderNo);
        commentReq.setGoodId(goodId);
        if (imageList == null) {
            commentReq.setStatus(0);
        } else {
            commentReq.setStatus(1);
        }
        commentReq.setContent(content);
        commentReq.setStarLevel(starLevel);
        cdtProductCommentService.save(commentReq);
        Long insertId = commentReq.getId();
        if (insertId > 0 && imageList != null) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j) == null) {
                    throw new RRException("上传文件不能为空");
                }
                //上传文件
                CommentPicture pictureVo = new CommentPicture();
                pictureVo.setType(0);
                pictureVo.setCommentId(insertId);
                pictureVo.setPicUrl(list.get(j));
                pictureVo.setSortOrder(j + 1);
                commentPictureService.save(pictureVo);
            }
        }
        Map resultModel = new HashMap();
        resultModel.put("comment_id", insertId);
        resultModel.put("mag", "发表成功");
        return Result.success(resultModel);
    }

    @PostMapping("/up")
    @IgnoreAuth
    public String upload(HttpServletRequest request, @RequestParam("files") MultipartFile files) {
        if (files == null) {
            throw new RRException("上传文件不能为空");
        }
        String url = null;
        try {
            url = OSSFactory.build().upload(files, WeixinContants.GOODS_COMMENT_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
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
    @IgnoreAuth
    public Result<Object> reply(
//                                @LoginUser UserVo loginUser,
            Long userId,
            String userName,
            Integer replyUserId,
            Integer replyId,
            @RequestParam Integer commentId,
            @RequestParam String content
    ) {
        RepCommentVo repCommentVo = new RepCommentVo();
        repCommentVo.setCommentId(commentId);
        repCommentVo.setUserId(userId);
        repCommentVo.setUserName(userName);
        repCommentVo.setContent(content);
        repCommentVo.setAddTime(Long.valueOf(System.currentTimeMillis() / 1000));
        apiRepCommentService.save(repCommentVo);
        Map resultModel = new HashMap();
        resultModel.put("meg", "成功");
        return Result.success(resultModel);
    }


    @ApiOperation(value = "查询评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentId", value = "评论id", dataType = "Integer", paramType = "query", example = "2223333")})
    @ApiResponses({@ApiResponse(code = 200, message = "If successful, this method return JwtAccessTokenVO."),
            @ApiResponse(code = 400, message = "If bad request.")
    })
    @RequestMapping("comment")
    @IgnoreAuth
    public Result<Object> post(
//                             @LoginUser UserVo loginUser,
            Long userId,
            @RequestParam Integer commentId
    ) {
        Map resultModel = new HashMap();
        Map<String, Object> query = new HashMap<>();
        query.put("commentId", commentId);
        List<RepCommentVo> repCommentVos = apiRepCommentService.queryList(query);
        for (RepCommentVo commentItem : repCommentVos) {
            commentItem.setContent(commentItem.getContent());
            commentItem.setUserName(commentItem.getUserName());
        }
        resultModel.put("repCommentList", repCommentVos);
        return Result.success(resultModel);
    }

    /**
     *
     */
    @ApiOperation(value = "评论数量")
    @RequestMapping("count")
    @IgnoreAuth
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
    @RequestMapping("list1")
    public Object list1(Integer typeId, Integer valueId, Integer showType,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                        String sort, String order) {
        /*Map<String, Object> resultObj = new HashMap();
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
            commentItem.setContent(commentItem.getContent());
            commentItem.setUser_info(userService.queryObject(commentItem.getUser_id()));

            Map paramPicture = new HashMap();
            paramPicture.put("comment_id", commentItem.getId());
            List<CommentPictureVo> commentPictureEntities = commentPictureService.(paramPicture);
            commentItem.setPic_list(commentPictureEntities);
        }
        return toResponsSuccess(pageUtil);*/
        return null;
    }


}
