package com.shangyitong.yygh.cmn.controller;

import com.shangyitong.common.result.Result;
import com.shangyitong.yygh.cmn.service.DictService;
import com.shangyitong.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Api(tags = "数据字典管理")
@RestController
@RequestMapping("/admin/cmn/dict")
@RequiredArgsConstructor
//@CrossOrigin
public class DictController {

    private final DictService dictService;

    /**
     * 根据数据id查询子数据列表
     */
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable("id") Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    /**
     * 数据字典导出
     */
    @ApiOperation(value = "数据字典导出")
    @GetMapping("/exportData")
    public Result exportData(HttpServletResponse response) {
        dictService.exportData(response);
        return Result.ok();
    }

    /**
     * 数据字典导入
     */
    @ApiOperation(value = "数据字典导入")
    @PostMapping("/importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }

    /**
     * 根据dictcode和value查询
     */
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode,
                          @PathVariable("value") String value) {
        String dictName = dictService.getNameByParentDictCodeAndValue(dictCode,value);
        return dictName;
    }

    /**
     * 根据value查询
     */
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable("value") String value) {
        String dictName = dictService.getNameByParentDictCodeAndValue("",value);
        return dictName;
    }

    /**
     * 根据dictCode获取下级节点
     */
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }
}
