package cn.com.adtech.riss2.core.file;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.*;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.fileservice.FileServiceSettings;

/** Read-only, local Excel rendering. Formula caches are displayed, never evaluated. */
public final class SpreadsheetPreview {
    private static final String NS="urn:schemas-microsoft-com:office:spreadsheet";
    private static final int PAGE_ROWS=100, PAGE_COLUMNS=64, MAX_CELLS=500000;
    private SpreadsheetPreview() { }

    public static boolean supports(ResSourceData resource) {
        return extension(resource.getRsdaFileType()) || extension(resource.getRsdaFileName());
    }
    private static boolean extension(String value) {
        if(value==null)return false;
        value=value.trim().toLowerCase(Locale.ROOT);
        return value.equals("xls")||value.equals("xlsx")||value.endsWith(".xls")||value.endsWith(".xlsx");
    }
    public static Path resourceFile(ResSourceData resource) throws IOException {
        String relative=resource.getRsdaFileLocalName();
        if(relative==null||relative.trim().isEmpty())throw new IOException("该资源没有可预览的 Excel 源文件。");
        Path file=FileServiceSettings.resolve(FileServiceSettings.root(),relative);
        if(!Files.isRegularFile(file))throw new IOException("Excel 源文件不存在，请联系管理员检查资源附件。");
        return file;
    }

    public static Map<String,Object> read(Path file,int requestedSheet,int requestedPage,int requestedColumns) throws Exception {
        if(Files.size(file)>25L*1024*1024)throw new IOException("Excel 文件较大，暂不支持在线预览，请联系管理员处理。");
        byte[] header=new byte[8];
        try(InputStream in=Files.newInputStream(file)){in.read(header);}
        List<Grid> sheets;
        if((header[0]&255)==0xD0&&(header[1]&255)==0xCF)sheets=poi(file);
        else if(header[0]=='P'&&header[1]=='K'){checkZip(file);sheets=poi(file);}
        else sheets=xml(file);
        if(sheets.isEmpty())throw new IOException("该文件没有可显示的工作表。");
        int selected=0;
        for(int i=0;i<sheets.size();i++)if(sheets.get(i).index==requestedSheet)selected=i;
        Grid grid=sheets.get(selected);
        Map<String,Object> view=new LinkedHashMap<String,Object>();
        List<Map<String,Object>> tabs=new ArrayList<Map<String,Object>>();
        for(Grid sheet:sheets)tabs.add(map("index",sheet.index,"name",sheet.name));
        int pages=Math.max(1,(grid.rowCount+PAGE_ROWS-1)/PAGE_ROWS);
        int page=Math.min(Math.max(1,requestedPage),pages);
        int columnPages=Math.max(1,(grid.columnCount+PAGE_COLUMNS-1)/PAGE_COLUMNS);
        int columnPage=Math.min(Math.max(1,requestedColumns),columnPages);
        int start=(page-1)*PAGE_ROWS,end=Math.min(grid.rowCount,start+PAGE_ROWS);
        int firstColumn=(columnPage-1)*PAGE_COLUMNS,lastColumn=Math.min(grid.columnCount,firstColumn+PAGE_COLUMNS);
        List<Integer> visibleRows=new ArrayList<Integer>(),visibleColumns=new ArrayList<Integer>();
        for(int r=start;r<end;r++)if(!grid.hiddenRows.contains(r))visibleRows.add(r);
        for(int c=firstColumn;c<lastColumn;c++)if(!grid.hiddenColumns.contains(c))visibleColumns.add(c);
        List<Map<String,Object>> columns=new ArrayList<Map<String,Object>>();
        for(int c:visibleColumns)columns.add(map("name",columnName(c),"width",grid.widths.containsKey(c)?grid.widths.get(c):grid.autoWidth(c)));
        Map<Long,Span> starts=new HashMap<Long,Span>();Set<Long> covered=new HashSet<Long>();
        for(Span span:grid.merges){
            List<Integer> rr=intersect(visibleRows,span.r1,span.r2),cc=intersect(visibleColumns,span.c1,span.c2);
            if(rr.isEmpty()||cc.isEmpty())continue;
            Span clipped=new Span(rr.get(0),rr.size(),cc.get(0),cc.size());
            clipped.origin=grid.cell(span.r1,span.c1);starts.put(key(rr.get(0),cc.get(0)),clipped);
            for(int r:rr)for(int c:cc)if(r!=rr.get(0)||c!=cc.get(0))covered.add(key(r,c));
        }
        List<Map<String,Object>> rows=new ArrayList<Map<String,Object>>();
        for(int r:visibleRows){
            List<Map<String,Object>> cells=new ArrayList<Map<String,Object>>();
            for(int c:visibleColumns){
                if(covered.contains(key(r,c)))continue;
                Span span=starts.get(key(r,c));Value cell=span==null?grid.cell(r,c):span.origin;
                cells.add(map("text",cell==null?"":cell.text,"style",cell==null?"":cell.style,
                        "rowspan",span==null?1:span.r2,"colspan",span==null?1:span.c2));
            }
            rows.add(map("number",r+1,"height",grid.heights.containsKey(r)?grid.heights.get(r):28,"cells",cells));
        }
        view.putAll(map("sheets",tabs,"sheet",grid.index,"sheetName",grid.name,"page",page,"pages",pages,
                "columnPage",columnPage,"columnPages",columnPages,"rowCount",grid.rowCount,"columnCount",grid.columnCount,
                "startRow",grid.rowCount==0?0:start+1,"endRow",end,"columnRange",grid.columnCount==0?"":columnName(firstColumn)+" – "+columnName(lastColumn-1),
                "columns",columns,"rows",rows,"empty",grid.cells.isEmpty()));
        return view;
    }
    private static List<Integer> intersect(List<Integer> values,int low,int high){List<Integer> result=new ArrayList<Integer>();for(int n:values)if(n>=low&&n<=high)result.add(n);return result;}
    private static Map<String,Object> map(Object... values){Map<String,Object> result=new LinkedHashMap<String,Object>();for(int i=0;i<values.length;i+=2)result.put((String)values[i],values[i+1]);return result;}
    private static long key(int row,int column){return ((long)row<<16)|column;}
    public static String columnName(int column){String name="";for(int n=column+1;n>0;n=(n-1)/26)name=(char)('A'+(n-1)%26)+name;return name;}
    private static class Value {String text,style;Value(String text,String style){this.text=text;this.style=style;}}
    private static class Span {int r1,r2,c1,c2;Value origin;Span(int r1,int r2,int c1,int c2){this.r1=r1;this.r2=r2;this.c1=c1;this.c2=c2;}}
    private static class Grid {
        int index,rowCount,columnCount;String name;
        Map<Long,Value> cells=new HashMap<Long,Value>();List<Span> merges=new ArrayList<Span>();
        Map<Integer,Integer> widths=new HashMap<Integer,Integer>(),heights=new HashMap<Integer,Integer>();
        Set<Integer> hiddenRows=new HashSet<Integer>(),hiddenColumns=new HashSet<Integer>();
        Grid(int index,String name){this.index=index;this.name=name;}
        Value cell(int r,int c){return cells.get(key(r,c));}
        void put(int r,int c,String text,String style)throws IOException{
            if(r<0||r>=1048576||c<0||c>=16384)throw new IOException("Excel 行列范围无效。");
            if(cells.size()>=MAX_CELLS)throw new IOException("Excel 单元格数量较多，暂不支持在线预览。");
            cells.put(key(r,c),new Value(text,style));rowCount=Math.max(rowCount,r+1);columnCount=Math.max(columnCount,c+1);
        }
        void merge(int r1,int r2,int c1,int c2)throws IOException{
            if(r2<r1||c2<c1||r2>=1048576||c2>=16384||merges.size()>=20000)throw new IOException("Excel 合并单元格范围无效。");
            merges.add(new Span(r1,r2,c1,c2));rowCount=Math.max(rowCount,r2+1);columnCount=Math.max(columnCount,c2+1);
        }
        int autoWidth(int column){int size=10;for(int r=0;r<Math.min(rowCount,100);r++){Value v=cell(r,column);if(v!=null)size=Math.max(size,v.text.codePoints().map(n->n>255?2:1).sum());}return Math.min(280,Math.max(100,size*8+24));}
    }

    private static void checkZip(Path file)throws Exception{
        long total=0;int count=0;byte[] buffer=new byte[8192];
        try(ZipFile zip=new ZipFile(file.toFile())){
            Enumeration<? extends ZipEntry> entries=zip.entries();
            while(entries.hasMoreElements()){
                ZipEntry entry=entries.nextElement();if(++count>10000)throw new IOException("Excel 文件结构过大。");
                if(entry.isDirectory())continue;
                try(InputStream input=zip.getInputStream(entry)){int length;while((length=input.read(buffer))!=-1){total+=length;if(total>100L*1024*1024)throw new IOException("Excel 解压后内容过大，暂不支持在线预览。");}}
                // Old POI is shared by the legacy app; validate XML before it reaches that parser.
                if(entry.getName().endsWith(".xml")||entry.getName().endsWith(".rels"))try(InputStream input=zip.getInputStream(entry)){secureXml().newDocumentBuilder().parse(input);}
            }
        }
    }
    private static List<Grid> poi(Path file)throws Exception{
        List<Grid> result=new ArrayList<Grid>();Workbook book=null;
        try(InputStream in=new BufferedInputStream(Files.newInputStream(file))){
            book=WorkbookFactory.create(in);DataFormatter format=new DataFormatter(Locale.CHINA);
            for(int i=0;i<book.getNumberOfSheets();i++){
                if(book.isSheetHidden(i)||book.isSheetVeryHidden(i))continue;
                Sheet sheet=book.getSheetAt(i);Grid grid=new Grid(i,sheet.getSheetName());result.add(grid);
                Map<Short,String> styles=new HashMap<Short,String>();
                for(Row row:sheet){
                    if(row.getZeroHeight())grid.hiddenRows.add(row.getRowNum());
                    grid.heights.put(row.getRowNum(),Math.max(22,Math.min(600,Math.round(row.getHeightInPoints()*4/3))));
                    for(Cell cell:row){
                        CellStyle style=cell.getCellStyle();String css=styles.get(style.getIndex());
                        if(css==null){css=poiStyle(book,style);styles.put(style.getIndex(),css);}
                        int type=cell.getCellType()==Cell.CELL_TYPE_FORMULA?cell.getCachedFormulaResultType():cell.getCellType();
                        String text;
                        if(type==Cell.CELL_TYPE_NUMERIC){
                            boolean date1904=book instanceof XSSFWorkbook && ((XSSFWorkbook)book).getCTWorkbook().getWorkbookPr()!=null && ((XSSFWorkbook)book).getCTWorkbook().getWorkbookPr().getDate1904();
                            text=format.formatRawCellContents(cell.getNumericCellValue(),style.getDataFormat(),style.getDataFormatString(),date1904);
                        }else if(type==Cell.CELL_TYPE_STRING)text=cell.getStringCellValue();
                        else if(type==Cell.CELL_TYPE_BOOLEAN)text=cell.getBooleanCellValue()?"TRUE":"FALSE";
                        else if(type==Cell.CELL_TYPE_ERROR)text=FormulaError.forInt(cell.getErrorCellValue()).getString();
                        else text="";
                        if(!text.isEmpty()||style.getIndex()!=0)grid.put(row.getRowNum(),cell.getColumnIndex(),text,css+(style.getAlignment()==CellStyle.ALIGN_GENERAL&&type==Cell.CELL_TYPE_NUMERIC?"text-align:right;":""));
                    }
                }
                for(int m=0;m<sheet.getNumMergedRegions();m++){CellRangeAddress span=sheet.getMergedRegion(m);grid.merge(span.getFirstRow(),span.getLastRow(),span.getFirstColumn(),span.getLastColumn());}
                for(int c=0;c<grid.columnCount;c++){
                    if(sheet.isColumnHidden(c))grid.hiddenColumns.add(c);
                    grid.widths.put(c,Math.max(24,Math.min(1200,Math.round(sheet.getColumnWidth(c)/256f*7+8))));
                }
            }
        }finally{if(book instanceof XSSFWorkbook)((XSSFWorkbook)book).getPackage().revert();}
        return result;
    }
    private static String poiStyle(Workbook book,CellStyle style){
        StringBuilder css=new StringBuilder();Font font=book.getFontAt(style.getFontIndex());
        css.append("font-size:").append(Math.max(8,Math.min(48,font.getFontHeightInPoints()))).append("pt;");
        if(font.getBoldweight()>=Font.BOLDWEIGHT_BOLD)css.append("font-weight:bold;");
        if(font.getItalic())css.append("font-style:italic;");
        if(font.getUnderline()!=Font.U_NONE)css.append("text-decoration:underline;");
        if(font.getStrikeout())css.append("text-decoration:line-through;");
        if(font.getFontName()!=null&&font.getFontName().matches("[\\p{L}\\p{N} _-]{1,60}"))css.append("font-family:'").append(font.getFontName()).append("',sans-serif;");
        color(css,"color",font instanceof XSSFFont?rgb(((XSSFFont)font).getXSSFColor()):indexed(book,font.getColor()));
        if(style.getFillPattern()!=CellStyle.NO_FILL)color(css,"background-color",rgb(style.getFillForegroundColorColor()));
        css.append("vertical-align:").append(style.getVerticalAlignment()==CellStyle.VERTICAL_TOP?"top":style.getVerticalAlignment()==CellStyle.VERTICAL_CENTER?"middle":"bottom").append(';');
        String align=style.getAlignment()==CellStyle.ALIGN_RIGHT?"right":style.getAlignment()==CellStyle.ALIGN_CENTER||style.getAlignment()==CellStyle.ALIGN_CENTER_SELECTION?"center":"left";
        css.append("text-align:").append(align).append(';');
        if(style.getWrapText())css.append("white-space:pre-wrap;overflow-wrap:anywhere;");
        short[] borders={style.getBorderTop(),style.getBorderRight(),style.getBorderBottom(),style.getBorderLeft()};
        String[] sides={"top","right","bottom","left"};
        for(int i=0;i<4;i++)if(borders[i]!=CellStyle.BORDER_NONE)css.append("border-").append(sides[i]).append(':').append(borders[i]==CellStyle.BORDER_THICK?3:borders[i]==CellStyle.BORDER_MEDIUM?2:1).append("px ").append(borders[i]==CellStyle.BORDER_DASHED?"dashed":borders[i]==CellStyle.BORDER_DOTTED?"dotted":borders[i]==CellStyle.BORDER_DOUBLE?"double":"solid").append(" #8a8a80;");
        return css.toString();
    }
    private static String indexed(Workbook book,short index){return book instanceof HSSFWorkbook?rgb(((HSSFWorkbook)book).getCustomPalette().getColor(index)):rgb(HSSFColor.getIndexHash().get((int)index));}
    private static String rgb(org.apache.poi.ss.usermodel.Color value){
        if(value instanceof XSSFColor){byte[] rgb=((XSSFColor)value).getRgbWithTint();if(rgb!=null&&rgb.length>=3)return String.format("#%02x%02x%02x",rgb[0]&255,rgb[1]&255,rgb[2]&255);return null;}
        if(value instanceof HSSFColor){short[] rgb=((HSSFColor)value).getTriplet();return String.format("#%02x%02x%02x",rgb[0],rgb[1],rgb[2]);}return null;
    }
    private static void color(StringBuilder css,String property,String value){if(value!=null&&value.matches("#[0-9a-fA-F]{6}"))css.append(property).append(':').append(value).append(';');}

    private static List<Grid> xml(Path file)throws Exception{
        DocumentBuilderFactory factory=secureXml();
        Document doc;try(InputStream in=Files.newInputStream(file)){doc=factory.newDocumentBuilder().parse(in);}
        Element root=doc.getDocumentElement();
        if(!NS.equals(root.getNamespaceURI())||!"Workbook".equals(root.getLocalName()))throw new IOException("该文件不是支持的 Excel 工作簿。");
        Map<String,Element> definitions=new HashMap<String,Element>();Element styleRoot=child(root,"Styles");
        if(styleRoot!=null)for(Element style:children(styleRoot,"Style"))definitions.put(attr(style,"ID"),style);
        Map<String,String> styles=new HashMap<String,String>();
        for(String id:definitions.keySet())xmlStyle(id,definitions,styles,new HashSet<String>());
        return xmlSheets(root,styles);
    }
    private static DocumentBuilderFactory secureXml()throws Exception{
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,true);factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities",false);factory.setFeature("http://xml.org/sax/features/external-parameter-entities",false);
        factory.setXIncludeAware(false);factory.setExpandEntityReferences(false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD,"");factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA,"");
        return factory;
    }
    private static List<Grid> xmlSheets(Element root,Map<String,String> styles)throws Exception{
        List<Grid> result=new ArrayList<Grid>();int index=0,totalCells=0;
        for(Element worksheet:children(root,"Worksheet")){
            Grid grid=new Grid(index++,attr(worksheet,"Name"));
            if(grid.name.isEmpty())grid.name="工作表 "+index;
            NodeList visible=worksheet.getElementsByTagNameNS("urn:schemas-microsoft-com:office:excel","Visible");
            if(visible.getLength()>0&&!"SheetVisible".equals(visible.item(0).getTextContent()))continue;
            result.add(grid);Element table=child(worksheet,"Table");if(table==null)continue;
            int column=0;
            Map<Integer,String> columnStyles=new HashMap<Integer,String>();
            for(Element col:children(table,"Column")){
                column=number(col,"Index",column+1)-1;int span=number(col,"Span",0);
                if(column<0||column+span>=16384)throw new IOException("Excel 列范围无效。");
                for(int c=column;c<=column+span;c++){
                    if(!attr(col,"Width").isEmpty())grid.widths.put(c,dimension(attr(col,"Width"),120));
                    if("1".equals(attr(col,"Hidden")))grid.hiddenColumns.add(c);
                    columnStyles.put(c,attr(col,"StyleID"));
                }column+=span+1;
            }
            int row=0;
            for(Element rowElement:children(table,"Row")){
                row=number(rowElement,"Index",row+1)-1;column=0;
                if(row<0||row>=1048576)throw new IOException("Excel 行范围无效。");
                if("1".equals(attr(rowElement,"Hidden")))grid.hiddenRows.add(row);
                if(!attr(rowElement,"Height").isEmpty())grid.heights.put(row,dimension(attr(rowElement,"Height"),28));
                for(Element cell:children(rowElement,"Cell")){
                    if(++totalCells>MAX_CELLS)throw new IOException("Excel 单元格数量较多，暂不支持在线预览。");
                    column=number(cell,"Index",column+1)-1;
                    Element data=child(cell,"Data");String text=data==null?"":data.getTextContent();
                    String styleId=attr(cell,"StyleID");if(styleId.isEmpty())styleId=attr(rowElement,"StyleID");if(styleId.isEmpty())styleId=columnStyles.get(column);
                    String css=styles.containsKey(styleId)?styles.get(styleId):styles.containsKey("Default")?styles.get("Default"):"";
                    if(data!=null&&"Number".equals(attr(data,"Type"))&&!css.contains("text-align:"))css+="text-align:right;";
                    grid.put(row,column,text,css);
                    int across=number(cell,"MergeAcross",0),down=number(cell,"MergeDown",0);
                    if(across>0||down>0)grid.merge(row,row+down,column,column+across);
                    column+=across+1;
                }row++;
            }
        }return result;
    }
    private static String xmlStyle(String id,Map<String,Element> definitions,Map<String,String> styles,Set<String> visiting)throws IOException{
        if(styles.containsKey(id))return styles.get(id);
        Element style=definitions.get(id);if(style==null)return "";
        if(!visiting.add(id)||visiting.size()>32)throw new IOException("Excel 样式继承无效。");
        String parent=attr(style,"Parent");if(parent.isEmpty()&&!id.equals("Default"))parent="Default";
        StringBuilder css=new StringBuilder(parent.isEmpty()?"":xmlStyle(parent,definitions,styles,visiting));
        Element font=child(style,"Font"),fill=child(style,"Interior"),align=child(style,"Alignment"),borders=child(style,"Borders");
        if(font!=null){
            if("1".equals(attr(font,"Bold")))css.append("font-weight:bold;");if("1".equals(attr(font,"Italic")))css.append("font-style:italic;");
            color(css,"color",attr(font,"Color"));String size=attr(font,"Size");if(size.matches("[0-9]{1,2}(\\.[0-9]{1,2})?"))css.append("font-size:").append(Math.max(8,Math.min(48,Double.parseDouble(size)))).append("pt;");
        }
        if(fill!=null)color(css,"background-color",attr(fill,"Color"));
        if(align!=null){String horizontal=attr(align,"Horizontal"),vertical=attr(align,"Vertical");
            if(Arrays.asList("Left","Center","Right","Justify").contains(horizontal))css.append("text-align:").append(horizontal.toLowerCase(Locale.ROOT)).append(';');
            if(Arrays.asList("Top","Center","Bottom").contains(vertical))css.append("vertical-align:").append(vertical.equals("Center")?"middle":vertical.toLowerCase(Locale.ROOT)).append(';');
            if("1".equals(attr(align,"WrapText")))css.append("white-space:pre-wrap;overflow-wrap:anywhere;");
        }
        if(borders!=null)for(Element border:children(borders,"Border")){
            String side=attr(border,"Position");if(!Arrays.asList("Top","Right","Bottom","Left").contains(side))continue;
            String color=attr(border,"Color");if(!color.matches("#[0-9a-fA-F]{6}"))color="#8a8a80";
            css.append("border-").append(side.toLowerCase(Locale.ROOT)).append(":1px solid ").append(color).append(';');
        }
        visiting.remove(id);styles.put(id,css.toString());return css.toString();
    }
    private static int number(Element element,String name,int fallback)throws IOException{String value=attr(element,name);if(value.isEmpty())return fallback;if(!value.matches("[0-9]{1,7}"))throw new IOException("Excel 行列参数无效。");return Integer.parseInt(value);}
    private static int dimension(String value,int fallback){try{return Math.max(22,Math.min(1200,(int)Math.round(Double.parseDouble(value)*4/3)));}catch(NumberFormatException ignored){return fallback;}}
    private static String attr(Element element,String name){return element.getAttributeNS(NS,name);}
    private static Element child(Element parent,String name){for(Node n=parent.getFirstChild();n!=null;n=n.getNextSibling())if(n instanceof Element&&NS.equals(n.getNamespaceURI())&&name.equals(n.getLocalName()))return (Element)n;return null;}
    private static List<Element> children(Element parent,String name){List<Element> result=new ArrayList<Element>();for(Node n=parent.getFirstChild();n!=null;n=n.getNextSibling())if(n instanceof Element&&NS.equals(n.getNamespaceURI())&&name.equals(n.getLocalName()))result.add((Element)n);return result;}
}
