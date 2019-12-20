package template;

import com.mybatis.plus.demo.pojo.CdtProductCommentForm;
import com.mybatis.plus.demo.pojo.CdtProductCommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

{package.Controller};
        com.chundengtai.base.service
        .CdtProductCommentService;

/**
 * <p>
 * CdtProductComment前端控制器
 * </p>
 *
 * @author Royal
 * @since 2019-12-20
 */
@Controller
@Api(tags = "CdtProductComment")
@RequestMapping("/cdtProductComment")
public class CdtProductCommentController {
    @Autowired
    public CdtProductCommentService cdtProductCommentService;

    /**
     * 保存单条
     *
     * @param param 保存参数
     * @return 是否添加成功
     */
    @ApiOperation(value = "保存", notes = "保存数据到CdtProductComment")
    @RequestMapping(value = "/add.json", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public ResponseBean addCdtProductComment(@RequestBody BaseForm<CdtProductCommentForm> param) {
        Integer result = cdtProductCommentService.save(param.getData());
        return new ResponseBean(result);
    }

    /**
     * 更新(根据主键id更新)
     *
     * @param param 修改参数
     * @return 是否更改成功
     */
    @ApiOperation(value = "更新数据", notes = "根据主键id更新CdtProductComment数据")
    @RequestMapping(value = "/updateById.json", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public ResponseBean updateCdtProductCommentById(@RequestBody BaseForm<CdtProductCommentForm> param) {
        Integer result = cdtProductCommentService.updateById(param.getData());
        return new ResponseBean(result);
    }

    /**
     * 删除(根据主键id伪删除)
     *
     * @param param 主键id
     * @return 是否删除成功
     */
    @ApiOperation(value = "删除数据", notes = "根据主键id伪删除CdtProductComment数据")
    @RequestMapping(value = "/deleteById.json", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public ResponseBean deleteCdtProductCommentById(@RequestBody BaseForm<String> param) {
        Integer result = cdtProductCommentService.deleteById(param.getData());
        return new ResponseBean(result);
    }

    /**
     * 根据主键id查询单条
     *
     * @param param 主键id
     * @return 查询结果
     */
    @ApiOperation(value = "获取单条数据", notes = "根据主键id获取CdtProductComment数据")
    @RequestMapping(value = "/getById.json", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public ResponseBean getCdtProductCommentById(@RequestBody BaseForm<String> param) {
        CdtProductCommentVO result = cdtProductCommentService.selectById(param.getData());
        return new ResponseBean(result);
    }

    /**
     * 查询全部
     *
     * @param param 查询条件
     * @return 查询结果
     */
    @ApiOperation(value = "全部查询", notes = "查询CdtProductComment全部数据")
    @RequestMapping(value = "/queryAll.json", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public ResponseBean getCdtProductCommentAll(@RequestBody BaseForm<CdtProductCommentForm> param) {
        List<CdtProductCommentVO> result = cdtProductCommentService.selectAll(param.getData());
        return new ResponseBean(result);
    }

    /**
     * 分页查询
     *
     * @param param 查询条件
     * @return 查询结果
     */
    @ApiOperation(value = "分页查询", notes = "分页查询CdtProductComment全部数据")
    @RequestMapping(value = "/queryPage.json", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public ResponseBean getCdtProductCommentPage(@RequestBody BaseForm<CdtProductCommentForm> param) {
        IPage<CdtProductCommentVO> result = cdtProductCommentService.selectPage(param.getData());
        return new ResponseBean(result);
    }
}
