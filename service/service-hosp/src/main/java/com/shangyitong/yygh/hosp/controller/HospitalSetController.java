package com.shangyitong.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shangyitong.common.exception.YyghException;
import com.shangyitong.common.result.Result;
import com.shangyitong.common.result.ResultCodeEnum;
import com.shangyitong.common.util.MD5;
import com.shangyitong.yygh.hosp.service.HospitalSetService;
import com.shangyitong.yygh.model.hosp.HospitalSet;
import com.shangyitong.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * 医院设置
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "医院设置")
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    private final HospitalSetService hospitalSetService;

    /**
     * 1.查询医院表中的所有信息
     */
    @ApiOperation("获取医院的所有信息")
    @GetMapping("findAll")
    public Result findAllHospitalSet(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    /**
     * 2.删除医院设置
     */
    @ApiOperation("删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHosp(@PathVariable Long id){
        boolean b = hospitalSetService.removeById(id);
        if (b) return Result.ok(b);
        return Result.fail(b);
    }

    /**
     * 3.条件查询带分页查下医院设置信息
     */
    @ApiOperation("条件查询带分页查下医院设置信息")
    @PostMapping("findPage/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalQueryVo hospitalQueryVo){
        // 创建page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current,limit);
        // 构建条件
        LambdaQueryWrapper<HospitalSet> qw = new LambdaQueryWrapper<>();
        String hosname = hospitalQueryVo.getHosname();
        String hoscode = hospitalQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)){
            qw.like(HospitalSet::getHosname,hospitalQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hoscode)) {
            qw.eq(HospitalSet::getHoscode, hospitalQueryVo.getHoscode());
        }
        // 调用方法实现分页查询
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, qw);
        return Result.ok(hospitalSetPage);
    }

    /**
     * 4.添加医院设置
     */
    @ApiOperation("添加医院设置")
    @PostMapping("/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 设置状态 1使用 0不能使用
        hospitalSet.setStatus(1);
        // 设置签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 5.根据id获取医院设置
     */
    @ApiOperation("根据id获取医院设置")
    @GetMapping("/getHospSet/{id}")
    public Result getHospSet(@PathVariable("id") long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    /**
     * 6.修改医院设置
     */
    @ApiOperation(value = "修改医院设置")
    @PostMapping("/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok("修改成功");
        } else {
            return Result.fail("修改失败");
        }
    }

    /**
     * 7.批量删除医院设置
     */
    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    /**
     * 8.医院设置锁定和解锁
     */
    @ApiOperation(value = "医院设置锁定和解锁")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable("id") Long id,
                                  @PathVariable("status") Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    /**
     * 9.发送签名秘钥
     */
    @ApiOperation(value = "发送签名秘钥")
    @PutMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信

        return Result.ok();
    }
}
