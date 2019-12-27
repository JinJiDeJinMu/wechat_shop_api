package com.platform.api;

import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: ApiUploadController <br>
 */
@Api(tags = "上传")
@RestController
@RequestMapping("/api/upload")
public class ApiUploadController extends ApiBaseAction {

//    /**
//     * 上传文件
//     */
//	@ApiOperation(value = "上传文件")
//    @IgnoreAuth
//    @PostMapping("/upload")
//    public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
//        if (file.isEmpty()) {
//            throw new RRException("上传文件不能为空");
//        }
//        //上传文件
//        String url = OSSFactory.build().upload(file);
//        return toResponsSuccess(url);
//    }
}