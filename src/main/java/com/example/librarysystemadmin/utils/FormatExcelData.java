package com.example.librarysystemadmin.utils;

import com.example.librarysystemadmin.domain.FetchBook;
import com.example.librarysystemadmin.mapper.BooksMapper;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatExcelData {
    public static FetchBook[] getData(int rows, int cells, XSSFSheet sheet, BooksMapper booksMapper) {
        // 读取数据
        FetchBook[] books = new FetchBook[rows - 1];
        for (int i = 1; i < rows; i++) {
            FetchBook book = new FetchBook();
            for (int j = 0; j < cells; j++) {

                try {
                    if (sheet.getRow(i).getCell(j).toString().equals("")) {
                        return books;
                    }

                    if (sheet.getRow(i).getCell(j).getCellType() != 3) {
                        switch (j) {
                            case 0:

                                book.setBookName(sheet.getRow(i).getCell(j).toString());
                                break;
                            case 1:
                                book.setAuthor(sheet.getRow(i).getCell(j).toString());
                                break;
                            case 2:
                                book.setIntroduction(sheet.getRow(i).getCell(j).toString());
                                break;
                            case 3:
                                book.setPublisher(sheet.getRow(i).getCell(j).toString());
                                break;
                            case 4:
                                int borrowable = sheet.getRow(i).getCell(j).toString().equals("外借") ? 0 : 1;
                                book.setIsBorrowable(borrowable);
                                break;
                            case 5:
                                if (sheet.getRow(i).getCell(j).getCellType() == 0) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    // 将 String 类型转换成 Date 类型
                                    Date date = sheet.getRow(i).getCell(j).getDateCellValue();
                                    book.setPublishDate(new java.sql.Date(date.getTime()));
                                }
                                break;
                            case 6:
                                if (sheet.getRow(i).getCell(j).getCellType() == 0) {
                                    double isbn = sheet.getRow(i).getCell(j).getNumericCellValue();
                                    //将double类型的isbn转换成int类型
                                    book.setIsbn(String.valueOf((long) isbn));
                                }
                                break;
                            case 7:
                                String category = sheet.getRow(i).getCell(j).toString();
                                String category_id = booksMapper.getBookCategoryListByName(category);
//                            System.out.println(category_id);
                                // 检查分类是否存在
                                if (category_id != null) {
                                    book.setCategoryId(new Long(category_id));
                                } else {
                                    int id = booksMapper.addBookCategory(category);
                                    book.setCategoryId(new Long(id));
                                }
                                break;
                            case 8:
                                if (sheet.getRow(i).getCell(j).getCellType() == 0) {
                                    double copies_number = sheet.getRow(i).getCell(j).getNumericCellValue();
//                                System.out.println((int) copies_number);
                                    //将double类型的copies_number转换成int类型
                                    book.setCopies_number(2);
                                }
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("第" + i + "行第" + j + "列数据格式错误");
                }

            }

            books[i - 1] = book;
        }
        return books;
    }
}