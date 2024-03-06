package com.shangyitong.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shangyitong.yygh.model.cmn.Dict;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author 86187
* @description 针对表【dict(组织架构表)】的数据库操作Service
* @createDate 2024-03-06 20:59:19
*/
public interface DictService extends IService<Dict> {
    /**
     * 根据数据id查询子数据列表
     * @param id
     * @return
     */
    List<Dict> findChildData(Long id);

    /**
     * 导出数据字典
     * @param response
     */
    void exportData(HttpServletResponse response);

    /**
     * 数据字典导入
     * @param file
     */
    void importData(MultipartFile file);

    /**
     * 根据上级编码与值获取数据字典名称
     * @param parentDictCode
     * @param value
     * @return
     */
    String getNameByParentDictCodeAndValue(String parentDictCode, String value);

    /**
     * 根据dictCode获取下级节点
     * @param dictCode
     * @return
     */
    List<Dict> findByDictCode(String dictCode);
}
