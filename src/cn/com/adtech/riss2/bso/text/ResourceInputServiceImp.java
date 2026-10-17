package cn.com.adtech.riss2.bso.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.Sheet;
import jxl.Workbook;

import org.springframework.web.bind.ServletRequestUtils;

import cn.com.adtech.riss2.controller.console.text.ResourceFileUploadUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ExcelTool;
import cn.com.adtech.riss2.core.ExcelUtil;
import cn.com.adtech.riss2.domain.text.ResTextLibrary;
import cn.com.adtech.riss2.domain.text.ResTextResource;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.dao.ILuceneDaoService;
import cn.com.unionlib.lucene.dao.IndexCommonService;
import cn.com.unionlib.lucene.dao.LuceneDaoServiceImp;

/**
 * 
 * @description 资源录入
 * 
 * @author MaChao
 * 
 * @version 1.0
 * 
 * @date Nov 17, 201510:35:24 AM
 * 
 */
public class ResourceInputServiceImp extends IndexCommonService implements IResourceInputService {
    private List<String> fileList = null;

    private ResourceFileUploadUtil upload = new ResourceFileUploadUtil();
    
    //索引管理
    private ILuceneDaoService luceneDaoService = new LuceneDaoServiceImp();

    public int getResLibCols(Long rtliId) {
        ResTextLibrary rtli = (ResTextLibrary) this.get(ResTextLibrary.class, rtliId);

        int temp = 0;
        // 获取字段
        if (rtli.getRtliCol1Label() != null && CommonUtil.isNotNull(rtli.getRtliCol1Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol2Label() != null && CommonUtil.isNotNull(rtli.getRtliCol2Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol3Label() != null && CommonUtil.isNotNull(rtli.getRtliCol3Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol4Label() != null && CommonUtil.isNotNull(rtli.getRtliCol4Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol5Label() != null && CommonUtil.isNotNull(rtli.getRtliCol5Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol6Label() != null && CommonUtil.isNotNull(rtli.getRtliCol6Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol7Label() != null && CommonUtil.isNotNull(rtli.getRtliCol7Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol8Label() != null && CommonUtil.isNotNull(rtli.getRtliCol8Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol9Label() != null && CommonUtil.isNotNull(rtli.getRtliCol9Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol10Label() != null && CommonUtil.isNotNull(rtli.getRtliCol10Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol11Label() != null && CommonUtil.isNotNull(rtli.getRtliCol11Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol12Label() != null && CommonUtil.isNotNull(rtli.getRtliCol12Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol13Label() != null && CommonUtil.isNotNull(rtli.getRtliCol13Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol14Label() != null && CommonUtil.isNotNull(rtli.getRtliCol14Label())) {
            temp += 1;
        }
        if (rtli.getRtliCol15Label() != null && CommonUtil.isNotNull(rtli.getRtliCol15Label())) {
            temp += 1;
        }

        return temp;
    }

    /**
     * 获取分类map
     */
    @Override
    public Map<String, Long> getResLibClassfiyList(Long rtliId) {
        String hql = "select r.rlclId, r.rlclName from ResLibraryClassify r where r.rlclRtliId=" + rtliId;
        return parseGroupToMap(hql);
    }

    /**
     * 将查询分类名称和ID的hql语句查询并封装成map
     *
     * @author MaChao
     * @date Nov 24, 2015
     *
     * @param hql
     * @return
     */
    private Map<String, Long> parseGroupToMap(String hql) {
        List<Object[]> list = getHibernateTemplate().find(hql);
        if (list == null || list.size() < 1) {
            return new HashMap<String, Long>();
        }
        Object[] obj;
        Map<String, Long> map = new HashMap<String, Long>();
        for (int i = 0, s = list.size(); i < s; i++) {
            obj = list.get(i);
            try {
                map.put(obj[1].toString(), Long.parseLong(obj[0].toString()));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        list = null;
        return map;
    }

    /**
     * 效验excel表格中的数据
     * 
     * @author MaChao
     * @date Nov 24, 2015
     * 
     * @param rrtyId
     * @param excelfile
     * @return 返回效验错误的信息，如果返回空则说明全部验证通过
     */
    @Override
    public String checkImportData(HttpServletRequest request, File excelfile) {
        Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
        int beginLine = ServletRequestUtils.getIntParameter(request, "beginLine", 2) - 1;
        String classfiy = ServletRequestUtils.getStringParameter(request, "classfiy", "");
        String coverF = ServletRequestUtils.getStringParameter(request, "coverF", "");

        if (rtliId == null || rtliId < 1L) {
            return "<li>未知资源库！</li>";
        }
        if (excelfile == null || !excelfile.exists()) {
            return "<li>未找到上传的excel文件</li>";
        }

        try {
            InputStream is = new FileInputStream(excelfile);
            jxl.Workbook rwb = Workbook.getWorkbook(is);
            // 获取第一张Sheet表
            Sheet rs = (Sheet) rwb.getSheet(0);

            // 获取模板列数
            int templetCols = this.getResLibCols(rtliId);
            int rows = rs.getRows();
            int columns = rs.getColumns();

            if (columns < templetCols) {
                return "<li>文件中的列数低于模板中设置的列！</li>";
            }
            if (rows <= beginLine) {
                return "<li>文件中数据总行数低于起始行数！</li>";
            }

            StringBuffer buffer = new StringBuffer();
            ResTextLibrary rtli = (ResTextLibrary) this.get(ResTextLibrary.class, rtliId);

            Map<String, Long> ClassfiyMap = getResLibClassfiyList(rtliId);

            String data; // 分类名称
            int colindex;
            for (int i = beginLine; i < rows; i++) {
                // 验证分类
                if (CommonUtil.isNotNull(rtli.getRtliTitleImp())) {
                    colindex = ExcelTool.getColumnIndex(rtli.getRtliTitleImp());
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    if (!CommonUtil.isNotNull(data)) {
                        buffer.append("<li>第：" + (i + beginLine - 1) + " 行数据" + rtli.getRtliTitle() + "不存在！</li>");
                    }
                }
                // 验证分类
                if (CommonUtil.isNotNull(classfiy)) {
                    colindex = ExcelTool.getColumnIndex(classfiy);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    if (ClassfiyMap.containsKey(data) == false) {
                        buffer.append("<li>第：" + (i + beginLine - 1) + " 行数据" + rtli.getRtliName() + "分类不存在！</li>");
                    }
                }
                // 验证封面
                if (CommonUtil.isNotNull(coverF)) {
                    colindex = ExcelTool.getColumnIndex(coverF);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    if (!CommonUtil.isNotNull(data)) {
                        buffer.append("<li>第：" + (i + beginLine - 1) + " 行数据,封面信息不存在！</li>");
                    }
                }
            }
            rwb.close();
            String result = buffer.toString();
            buffer = null;
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "<li>未找到上传的excel文件！</li>";
        } catch (IOException e) {
            e.printStackTrace();
            return "<li>excel文件读取失败！</li>";
        } catch (Exception e) {
            e.printStackTrace();
            return "<li>excle文件验证失败！</li>";
        }
    }

    /**
     * 保存导入的数据
     *
     * @author MaChao
     * @date Nov 24, 2015
     *
     * @param request
     * @param excelfile
     * @return
     */
    @Override
    public String saveImportData(HttpServletRequest request, File excelfile) {
        Long rtliId = ServletRequestUtils.getLongParameter(request, "rtliId", 0L);
        int beginLine = ServletRequestUtils.getIntParameter(request, "beginLine", 2) - 1;
        String classfiy = ServletRequestUtils.getStringParameter(request, "classfiy", "");//分类
        String coverF = ServletRequestUtils.getStringParameter(request, "coverF", "");//封面
        String fileFolder = ServletRequestUtils.getStringParameter(request, "fileFolder", "");//封面所在目录

        StringBuffer buffer = new StringBuffer();
        Long rihiId = null;

        try {
            //将文件写入集合中
            File file = new File(fileFolder);
            String[] fileArr = file.list();
            fileList = new ArrayList<String>();
            if (fileArr != null && fileArr.length > 0) {
                for (int i = 0, s = fileArr.length; i < s; i++) {
                    fileList.add(fileArr[i].toLowerCase());
                }
            }

            InputStream is = new FileInputStream(excelfile);
            jxl.Workbook rwb = Workbook.getWorkbook(is);
            //获取第一张Sheet表
            Sheet rs = (Sheet) rwb.getSheet(0);
            int rows = rs.getRows();

            Map<String, Long> ClassfiyMap = getResLibClassfiyList(rtliId);
            ResTextLibrary rtli = (ResTextLibrary) this.get(ResTextLibrary.class, rtliId);

            String data; //元素值
            String excelcol; //模板中设置的列
            int colindex;
            ResTextResource resource = null;
            int impNum = 0;

            List<ResMainBean> tempList = new ArrayList<ResMainBean>();
            ResMainBean dataBean = null;
            for (int i = beginLine; i < rows; i++) {
                resource = new ResTextResource();
                resource.setRtreRtliId(rtliId);
                resource.setRtreRecommend(0L);
                resource.setRtreDate(new Date());
                resource.setRtreUsed(1L);
                resource.setRtreAccess(0L);
                resource.setRtreLanguage(rtli.getRtliLanguage());

                //题名标题
                if (CommonUtil.isNotNull(rtli.getRtliTitleImp())) {
                    excelcol = rtli.getRtliTitleImp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 512);
                    resource.setRtreTitle(data);
                }

                //字段1
                if (CommonUtil.isNotNull(rtli.getRtliCol1Label())) {
                    excelcol = rtli.getRtliCol1Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 256);
                    resource.setRtreCol1(data);
                }

                //字段2
                if (CommonUtil.isNotNull(rtli.getRtliCol2Label())) {
                    excelcol = rtli.getRtliCol2Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 256);
                    resource.setRtreCol2(data);
                }

                //字段3
                if (CommonUtil.isNotNull(rtli.getRtliCol3Label())) {
                    excelcol = rtli.getRtliCol3Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 256);
                    resource.setRtreCol3(data);
                }

                //字段4
                if (CommonUtil.isNotNull(rtli.getRtliCol4Label())) {
                    excelcol = rtli.getRtliCol4Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 256);
                    resource.setRtreCol4(data);
                }

                //字段5
                if (CommonUtil.isNotNull(rtli.getRtliCol5Label())) {
                    excelcol = rtli.getRtliCol5Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 256);
                    resource.setRtreCol5(data);
                }

                //字段6
                if (CommonUtil.isNotNull(rtli.getRtliCol6Label())) {
                    excelcol = rtli.getRtliCol6Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 512);
                    resource.setRtreCol6(data);
                }

                //字段7
                if (CommonUtil.isNotNull(rtli.getRtliCol7Label())) {
                    excelcol = rtli.getRtliCol7Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 512);
                    resource.setRtreCol7(data);
                }

                //字段8
                if (CommonUtil.isNotNull(rtli.getRtliCol8Label())) {
                    excelcol = rtli.getRtliCol8Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 512);
                    resource.setRtreCol8(data);
                }

                //字段9
                if (CommonUtil.isNotNull(rtli.getRtliCol9Label())) {
                    excelcol = rtli.getRtliCol9Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 512);
                    resource.setRtreCol9(data);
                }

                //字段10
                if (CommonUtil.isNotNull(rtli.getRtliCol10Label())) {
                    excelcol = rtli.getRtliCol10Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 512);
                    resource.setRtreCol10(data);
                }

                //字段11
                if (CommonUtil.isNotNull(rtli.getRtliCol11Label())) {
                    excelcol = rtli.getRtliCol11Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 1024);
                    resource.setRtreCol11(data);
                }

                //字段12
                if (CommonUtil.isNotNull(rtli.getRtliCol12Label())) {
                    excelcol = rtli.getRtliCol12Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    data = CommonUtil.subUtf8Str(data, 1024);
                    resource.setRtreCol12(data);
                }

                //字段13
                if (CommonUtil.isNotNull(rtli.getRtliCol13Label())) {
                    excelcol = rtli.getRtliCol13Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    //data = CommonUtil.subString(data, 512);
                    resource.setRtreCol13(data);
                }

                //字段14
                if (CommonUtil.isNotNull(rtli.getRtliCol14Label())) {
                    excelcol = rtli.getRtliCol14Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    //data = CommonUtil.subString(data, 512);
                    resource.setRtreCol14(data);

                }

                //字段15
                if (CommonUtil.isNotNull(rtli.getRtliCol15Label())) {
                    excelcol = rtli.getRtliCol15Imp();
                    colindex = ExcelUtil.getColumnIndex(excelcol);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    //data = CommonUtil.subString(data, 512);
                    resource.setRtreCol15(data);
                }

                //分类
                if (CommonUtil.isNotNull(classfiy)) {
                    colindex = ExcelUtil.getColumnIndex(classfiy);
                    data = rs.getCell(colindex - 1, i).getContents().trim();
                    if (ClassfiyMap.containsKey(data)) {
                        resource.setRtreRlclId(ClassfiyMap.get(data));
                    }
                }

                //封面
                String coverFile = "";
                if (CommonUtil.isNotNull(coverF)) {
                    colindex = ExcelUtil.getColumnIndex(coverF);
                    data = rs.getCell(colindex - 1, i).getContents().trim();

                    if (fileList.contains(data.toLowerCase())) {
                        coverFile = data;
                    }
                    if (CommonUtil.isNotNull(data)) {
                        String fileurl = upload.uploadfiles(fileFolder + "\\" + coverFile);
                        resource.setRtreCover(fileurl);
                    }
                }

                try {
                    resource = (ResTextResource) save(resource);
                    impNum++;
                    
                    //添加到索引
                    dataBean = textResToDatabean(resource);
                    tempList.add(dataBean);
                    if(tempList.size() >= 100){
                    	this.luceneDaoService.addMultiDatabean(tempList);
                    	tempList = new ArrayList<ResMainBean>();
                    }
                    System.out.println("已完成：" + (i + 1) + "/" + rows);
                } catch (Exception e) {
                    e.printStackTrace();
                    buffer.append("<li>第：" + (i + beginLine - 1) + " 行数据保存失败</li>");
                }
            }
            
            //把剩下的加入索引
            if(tempList.size() > 0){
            	this.luceneDaoService.addMultiDatabean(tempList);
            }

            rwb.close();
            String result = "<li>共导入数据：" + impNum + "条！</li>";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "<li>数据导入失败！</li>";
        }
    }
}
