package com.evcar.subsidy.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.evcar.subsidy.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 基于poi的excel工具类
 * @author shu
 */
public class ExcelUtil {

	public static Object setTitleRow;

	/**
	 * 读取excel 原理：一行一行读取内容，保存到List<String[]>中
	 *
	 * @param filePath 	需要读取的文件路径
	 * @return
	 * @throws IOException
	 */
	public static List<String[]> read(String filePath) throws IOException {
		File file = new File(filePath);

		return read(file);
	}

	/**
	 * 读取excel 原理：一行一行读取内容，保存到List<String[]>中
	 *
	 * @param file 	需要读取的流
	 * @return
	 * @throws IOException
	 */
	public static List<String[]> read(File file) throws IOException {
		String filePath = file.getName();
		// 创建输入流
		InputStream inStream = new FileInputStream(file);

		return read(filePath, inStream);
	}

	/**
	 * 读取excel 原理：一行一行读取内容，保存到List<String[]>中
	 *
	 * @param suffix 	文件名后缀
	 * @param inStream 	需要读取的流
	 * @return
	 * @throws IOException
	 */
	public static List<String[]> read(String filePath, InputStream inStream) throws IOException {
		// 创建一个list 用来存储读取的内容
		List<String[]> list = new ArrayList<String[]>();
		// 前缀prefix，后缀suffix
//		String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
		String suffix = StringUtil.getSuffix(filePath);

		Workbook workbook = createWorkbook(suffix, inStream);
		if(workbook == null){
			inStream.close();
			return list;
		}
		// 默认读取第一个sheet
		// Sheet sheet = workbook.getSheetAt(0);
		int sheetTotal = workbook.getNumberOfSheets();
		for (int numSheet = 0; numSheet < sheetTotal; numSheet++) {
			Sheet sheet = workbook.getSheetAt(numSheet);
			if (sheet == null) {
				continue;
			}
			int rownum = sheet.getLastRowNum();// 总行数
			// Read the Row
			for (int i = 1; i <= rownum; i++) {
				Row row = sheet.getRow(i);
				int colnum = row.getLastCellNum();// 总列数
				String[] str = new String[colnum];
				if (row != null) {
					for (int j = 0; j < colnum; j++) {
						Cell cell = row.getCell(j);
						str[j] = getValue(cell);
					}
					list.add(str);
				}
			}
		}
		workbook.close();//关闭
		inStream.close();

		return list;
	}

	/**
	 * 根据xls或xlsx 创建相应 Workbook
	 * @param suffix
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	private static Workbook createWorkbook(String suffix, InputStream inStream) throws IOException{
		Workbook workbook = null;
		if ("xls".equals(suffix)) {
			workbook = new HSSFWorkbook(inStream);
		} else if ("xlsx".equals(suffix)) {
			workbook = new XSSFWorkbook(inStream);
		}

		return workbook;
	}

	/**
	 * 根据xls或xlsx 创建相应 Workbook
	 * @param suffix
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	private static Workbook createWorkbook(String suffix) throws IOException{
		Workbook workbook = null;
		if ("xls".equals(suffix)) {
			workbook = new HSSFWorkbook();
		} else if ("xlsx".equals(suffix)) {
			workbook = new XSSFWorkbook();
		}

		return workbook;
	}

	/**
	 * 获取单元格内容
	 *
	 * @param cell
	 * @return
	 */
	private static String getValue(Cell cell) {
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		} else {
			return cell.getStringCellValue();
		}
	}

	/**
	 * 导出excel
	 * @param excelInfo
	 * @param outStream
	 * @throws IOException
	 */
	public static void write(ExcelInfo excelInfo, OutputStream outStream) throws IOException {
		String fileName = excelInfo.getFileName();
		// 前缀prefix，后缀suffix
//		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String suffix = StringUtil.getSuffix(fileName);
		// 创建Workbook对象(excel的文档对象)
		Workbook workbook = createWorkbook(suffix);
		// 建立新的sheet对象（excel的表单）
		Sheet sheet = workbook.createSheet(excelInfo.getSheetName());
//		//设置缺省列宽8.5,行高为设置的20
//		sheet.setDefaultRowHeightInPoints(20);

		String[] titles = excelInfo.getTitles();
		String[] fields = excelInfo.getFields();
		setTitleRow(sheet, titles);

		//具体内容
		List<Map<String, Object>> list = excelInfo.getList();
		if(list != null){
			int size = list.size();
			for (int i = 0; i < size; i++) {
				setContentRows(sheet, i+1, list.get(i), fields, titles);
			}
		}
		workbook.write(outStream);
		outStream.close();
	}

	/**
	 * 导出excel,并分页
	 * @param excelInfo
	 * @param outStream
	 * @throws IOException
	 */
	public static void write2Page(ExcelInfo excelInfo, OutputStream outStream) throws IOException {
		String fileName = excelInfo.getFileName();
		// 前缀prefix，后缀suffix
//		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String suffix = StringUtil.getSuffix(fileName);
		// 创建Workbook对象(excel的文档对象)
		Workbook workbook = createWorkbook(suffix);

		//具体内容
		List<Map<String, Object>> list = excelInfo.getList();
		int rows = list.size();
		int pageSize = excelInfo.getPageSize();
		int sheetNum = 0; //指定sheet的页数

		if (rows % pageSize == 0) {
			sheetNum = rows / pageSize;
		} else {
			sheetNum = rows / pageSize + 1;
		}

		for (int i = 1; i <= sheetNum; i++) {
			// 建立新的sheet对象（excel的表单）
			Sheet sheet = workbook.createSheet(excelInfo.getSheetName() + i);
//			// 设置缺省列宽8.5,行高为设置的20
//			sheet.setDefaultRowHeightInPoints(20);

			String[] titles = excelInfo.getTitles();
			String[] fields = excelInfo.getFields();
			setTitleRow(sheet, titles);

			//分页处理内容
			int end = Math.min(pageSize, rows);
			for (int s = 0; s < end; s++) {
				int pageCount = (i -1) * pageSize;
				int start = pageCount + s;
				if (start >= rows){
					//如果数据超出总的记录数的时候，就退出循环
					break;
				}
				setContentRows(sheet, s+1, list.get(start), fields, titles);
			}
		}
		workbook.write(outStream);
		outStream.close();
	}

	/**
	 * 浏览器上导出excel,并解决文件名 中文乱码
	 *
	 * @param excelInfo
	 * @param response
	 * @param isNeedPage 是否需要分sheet页
	 * @throws IOException
	 */
	public static void export2Http(ExcelInfo excelInfo, HttpServletResponse response, boolean isNeedPage) throws IOException {
		String fileName = excelInfo.getFileName();

		response.reset();
//		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
		response.setContentType("application/msexcel");
		OutputStream output = response.getOutputStream();

		if(isNeedPage){
			write2Page(excelInfo, output);
		}else{
			write(excelInfo, output);
		}
	}

	/**
	 * 填充标题行
	 * @param sheet
	 * @param titles
	 */
	private static void setTitleRow(Sheet sheet, String[] titles) {
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		Row header = sheet.createRow(0);
		header.setHeightInPoints(25);
		// 创建单元格并设置单元格内容
		for (int i = 0, max = titles.length; i < max; i++) {
			header.createCell(i).setCellValue(titles[i]);
		}
	}

	/**
	 * 填充内容行
	 * @param sheet
	 * @param rowNum 行号
	 * @param map	对象
	 * @param fields
	 * @param titles
	 */
	private static void setContentRows(Sheet sheet, int rowNum, Map<String, Object> map, String[] fields, String[] titles){
		//sheet.autoSizeColumn(i,true);//中文还是不能实现宽度自适应
		Row row = sheet.createRow(rowNum);
		row.setHeightInPoints(20);
		for (int j = 0, max = fields.length; j < max; j++) {
			String value = String.valueOf(map.get(fields[j]));
			String titleValue = titles[j];
			int valueLength = value.getBytes().length;
			int titleValueLength = titleValue.getBytes().length;
//			int cellLength = (valueLength >= titleValueLength) ? valueLength : titleValueLength;
			int cellLength = Math.max(valueLength, titleValueLength);

			//由于utf-8一个中文字符有3个长度
			sheet.setColumnWidth(j,(cellLength + 3) * 256);//手动设置列宽
			row.createCell(j).setCellValue(value);
//			Cell cell = row.createCell(j);
//			cell.setCellType(Cell.CELL_TYPE_STRING);
//			cell.setCellValue(value);
		}
	}

}
