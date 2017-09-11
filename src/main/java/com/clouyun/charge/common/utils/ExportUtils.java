package com.clouyun.charge.common.utils;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 描述: 文件导出工具类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年5月2日 下午2:19:27
 */
public class ExportUtils {
	
	/**
	 * 参数: list     	结果集数据
	 *		headList    标题头(就是excel第一行,诸如会员名称,手机号码等)
	 *		valList		与标题头对应的字段名(如会员名称对应consName)
	 *		arr         导出的文件名
	 */
	public static void exportExcel(List list, HttpServletResponse response,List<String> headList,List<String> valList,String arr)
			throws Exception {
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("sheet1");

		HSSFCellStyle style = wb.createCellStyle(); // 样式对象

		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平

		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		HSSFCellStyle style2 = wb.createCellStyle(); // 样式对象
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

		// 生成一个字体
		HSSFFont font = wb.createFont();
		font.setColor(HSSFColor.BLACK.index);// HSSFColor.VIOLET.index //字体颜色
		font.setFontHeightInPoints((short) 14);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
		// 把字体应用到当前的样式
		style.setFont(font);
		if(null!=headList && headList.size()>0){
			for (int i = 0; i < headList.size(); i++) {
				sheet.setColumnWidth(i, 8000);
			} 
		}

		HSSFRow row2 = sheet.createRow(0);
		sheet.createFreezePane(0, 1, 0, 1);//冻结首行
		// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

		row2.setHeightInPoints(20);
		if(null!=headList && headList.size()>0){
			for (int i = 0; i < headList.size(); i++) {
				// 创建单元格并设置单元格内容
				HSSFCell cell1 = row2.createCell(i);
				cell1.setCellValue(headList.get(i).toString());
				cell1.setCellStyle(style);
			} 
		}
		if(null!=list && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object object = list.get(i);
				DataVo vo = null;
				if (object instanceof Map){
					vo = new DataVo((Map)object);
				}else if(object instanceof DataVo){
					vo = (DataVo) object;
				}
				// 在sheet里创建第三行...
				HSSFRow rowrow = sheet.createRow(i + 1);
				rowrow.setHeight((short) 320);
				if(valList != null && valList.size() > 0){
					for (int j = 0; j < valList.size(); j++) {
						HSSFCell cell = rowrow.createCell(j);
						cell.setCellStyle(style2);
						if(null != vo && null != vo.getString(valList.get(j))){
							//类型转换
							convertType(cell, vo.get(valList.get(j)));
							//cell.setCellValue(vo.getString(valList.get(j)));
						}else{
							rowrow.createCell(j).setCellValue("");
						}
					}
				}
			}
		}

		// 输出Excel文件
		String as = arr + ".xls";
		String fileName = as;// = Java.NET.URLEncoder.encode(as, "UTF-8");
		/* 根据request的locale 得出可能的编码，中文操作系统通常是gb2312 */
		// fileName = new String(as.getBytes("GB2312"), "ISO_8859_1");
		fileName = new String(as.getBytes("GB2312"), "ISO_8859_1");
		as = fileName;
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			response.reset();

			response.setHeader(
					"Content-disposition",
					"attachment; filename="
							+ URLDecoder.decode(URLEncoder.encode(as, "UTF-8"),
									"UTF-8"));
			response.setContentType("application/msexcel;charset=UTF-8");

			wb.write(output);
			output.flush();
		} catch (Exception e){
			e.getStackTrace();
			throw new BizException(1200005);
		}finally {
			if (output != null){
				output.close();
			}

			headList = null;
			valList = null;
			list = null;
			sheet = null;
			font = null;
			row2 = null;
			patriarch = null;
		}
	}

	/**
	 * 数据类型转换
	 */
	private static void convertType(HSSFCell cell,Object obj){
		if (obj instanceof Integer) {
			int value = 0;
			if(obj != null){
				value = ((Integer) obj).intValue();
			}
			cell.setCellValue(value);
		} else if (obj instanceof Double) {
			double d = 0;
			if(obj != null){
				d = ((Double) obj).doubleValue();
			}
			cell.setCellValue(d);
		} else if (obj instanceof Float) {
			float f = 0;
			if(obj != null){
				f = ((Float) obj).floatValue();
			}
			cell.setCellValue(f);
		} else if (obj instanceof Long) {
			long l = 0;
			if(obj != null){
				l = ((Long) obj).longValue();
			}
			cell.setCellValue(l);
		} else if (obj instanceof BigDecimal){
			BigDecimal bd = BigDecimal.ZERO;
			if(obj != null){
				bd = (BigDecimal) obj;
			}
			cell.setCellValue(bd.doubleValue());
		}else{
			String s = "";
			if(obj != null){
				s = obj.toString();
			}
			cell.setCellValue(s);
		}
	}
}
