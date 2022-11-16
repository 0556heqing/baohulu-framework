package com.baohulu.framework.common.utils.file.word;

/**
 * @author heqing
 * @date 2022/11/14 10:28
 */
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * word文档工具类
 *
 * @author heqing
 * @date 2022/11/02 13:49
 */
@Slf4j
public class WordUtil {

    /**
     * 装载模板所对应的对象
     * @param object
     * @return map
     */
    public static Map<String,Object> setMapObject(Object object){
        Map<String,Object> mapObject = new HashMap<>(4);
        if(object != null){
            Field[] fields = object.getClass().getDeclaredFields();
            log.info("fields" + fields.length);
            for(Field field : fields){
                try {
                    field.setAccessible(true);
                    String str = "${" + field.getName() + "}";
                    Object objValue = field.get(object);
                    if(objValue != null){
                        log.info(str + "----" + objValue.toString());
                        mapObject.put(str, objValue);
                    }else{
                        mapObject.put(str, "");
                    }
                } catch(Exception e) {
                    log.error("模板对象出现错误", e);
                }
            }
        } else {
            log.error("模板对象不是对应的一个类对象");
        }
        return mapObject;
    }

//    /**
//     * 根据指定的参数值、模板，生成 word 文档
//     * @param param 需要替换的变量
//     * @param template 模板
//     * @return XWPFDocument
//     * @throws IOException
//     * @throws InvalidFormatException
//     */
//    public static XWPFDocument generateWord(Map<String, Object> param, String template) throws IOException, InvalidFormatException {
//        if(param == null || template == null){
//            return null ;
//        }
//        XWPFDocument xwpfDocument = null;
//        log.info("解析的模版为：" + template);
//        try {
//            OPCPackage pack = POIXMLDocument.openPackage(template);
//            xwpfDocument = new XWPFDocument(pack);
//            if (param.size() > 0) {
//                //处理段落
//                List<XWPFParagraph> paragraphList = xwpfDocument.getParagraphs();
//                processParagraphs(paragraphList, param, xwpfDocument);
//                //处理表格
//                Iterator<XWPFTable> it = xwpfDocument.getTablesIterator();
//                while (it.hasNext()) {
//                    XWPFTable table = it.next();
//                    List<XWPFTableRow> rows = table.getRows();
//                    for (XWPFTableRow row : rows) {
//                        List<XWPFTableCell> cells = row.getTableCells();
//                        for (XWPFTableCell cell : cells) {
//                            List<XWPFParagraph> paragraphListTable = cell.getParagraphs();
//                            processParagraphs(paragraphListTable, param, xwpfDocument);
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            log.error("文件处理异常", e);
//            throw(e);
//        }catch (InvalidFormatException e) {
//            log.error("文档中内容处理异常", e);
//            throw(e);
//        }
//        return xwpfDocument;
//    }
//
//    /**
//     * 处理段落
//     * @param paragraphList
//     * @param param
//     * @param doc
//     * @throws InvalidFormatException
//     * @throws FileNotFoundException
//     */
//    public static void processParagraphs(List<XWPFParagraph> paragraphList, Map<String, Object> param, XWPFDocument doc) throws InvalidFormatException, FileNotFoundException{
//        if(paragraphList != null && paragraphList.size() > 0){
//            for(XWPFParagraph paragraph : paragraphList){
//                List<XWPFRun> runs = paragraph.getRuns();
//                for (XWPFRun run : runs) {
//                    String text = run.getText(0);
//                    if(text != null){
//                        boolean isSetText = false;
//                        for (Map.Entry<String, Object> entry : param.entrySet()) {
//                            String key = entry.getKey();
//                            if(text.indexOf(key) != -1){
//                                isSetText = true;
//                                Object value = entry.getValue();
//                                //文本替换
//                                if (value instanceof String) {
//                                    log.info(key + "模板中模要转化属性对应的值:" + value);
//                                    text = text.replace(key, value.toString());
//                                } else if (value instanceof ImageData) {
//                                    //图片替换
//                                    text = text.replace(key, "");
//                                    ImageData pic = (ImageData)value;
//                                    int width = Integer.parseInt(pic.getWidth().toString());
//                                    int height = Integer.parseInt(pic.getHeight().toString());
//                                    String byteArray = pic.getPath();
//                                    log.info("模板中模要转化的图片" + byteArray);
//                                    CTInline inline = run.getCTR().addNewDrawing().addNewInline();
//                                    insertPicture(doc,byteArray,inline, width, height);
//                                }
//                            }
//                        }
//                        if(isSetText){
//                            run.setText(text,0);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     *
//     * @param document
//     * @param filePath
//     * @param inline
//     * @param width
//     * @param height
//     * @throws InvalidFormatException
//     * @throws FileNotFoundException
//     */
//    private static void insertPicture(XWPFDocument document, String filePath, CTInline inline, int width, int height) throws InvalidFormatException, FileNotFoundException {
//        document.addPictureData(new FileInputStream(filePath), XWPFDocument.PICTURE_TYPE_PNG);
//        int id = document.getAllPictures().size() - 1;
//        final int emu = 9525;
//        width *= emu;
//        height *= emu;
//        Integer blipIdString = document.getAllPictures().get(id).getPictureType();
//        String blipId = blipIdString.toString();
//        String picXml = getPicXml(blipId, width, height);
//        XmlToken xmlToken = null;
//        try {
//            xmlToken = XmlToken.Factory.parse(picXml);
//        } catch (XmlException xe) {
//            log.error("XmlException",xe);
//        }
//        inline.set(xmlToken);
//        inline.setDistT(0);
//        inline.setDistB(0);
//        inline.setDistL(0);
//        inline.setDistR(0);
//        CTPositiveSize2D extent = inline.addNewExtent();
//        extent.setCx(width);
//        extent.setCy(height);
//        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
//        docPr.setId(id);
//        docPr.setName("IMG_" + id);
//        docPr.setDescr("IMG_" + id);
//    }
//
//    /**
//     * get the xml of the picture
//     * @param blipId
//     * @param width
//     * @param height
//     * @return
//     */
//    private static String getPicXml(String blipId, int width, int height) {
//        String picXml =
//                "" + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
//                        "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
//                        "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
//                        "         <pic:nvPicPr>" + "            <pic:cNvPr id=\"" + 0 +
//                        "\" name=\"Generated\"/>" + "            <pic:cNvPicPr/>" +
//                        "         </pic:nvPicPr>" + "         <pic:blipFill>" +
//                        "            <a:blip r:embed=\"" + blipId +
//                        "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
//                        "            <a:stretch>" + "               <a:fillRect/>" +
//                        "            </a:stretch>" + "         </pic:blipFill>" +
//                        "         <pic:spPr>" + "            <a:xfrm>" +
//                        "               <a:off x=\"0\" y=\"0\"/>" +
//                        "               <a:ext cx=\"" + width + "\" cy=\"" + height +
//                        "\"/>" + "            </a:xfrm>" +
//                        "            <a:prstGeom prst=\"rect\">" +
//                        "               <a:avLst/>" + "            </a:prstGeom>" +
//                        "         </pic:spPr>" + "      </pic:pic>" +
//                        "   </a:graphicData>" + "</a:graphic>";
//        return picXml;
//    }

}
