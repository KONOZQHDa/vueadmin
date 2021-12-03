package com.example.vueadminjava2.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vueadminjava2.commen.result.Result;
import com.example.vueadminjava2.dao.BookDao;
import com.example.vueadminjava2.dao.BookSortDao;
import com.example.vueadminjava2.dao.SortDao;
import com.example.vueadminjava2.entity.Book;
import com.example.vueadminjava2.entity.BookSort;
import com.example.vueadminjava2.entity.Sort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * (Book)表控制层
 *
 * @author KONO 曾淇杭哒！
 * @since 2021-11-06 10:03:36
 */
@RestController
@RequestMapping("book")
public class BookController{

    @Value("${server.port}")
    private String port;

    @Resource
    private BookDao bookDao;

    @Resource
    private BookSortDao bookSortDao;

    @Resource
    private SortDao sortDao;

    @SaCheckPermission("sys:book:save")
    @PostMapping("addBook")
    public Result addBook(@RequestBody Book book) {
        bookDao.insert(book);
        BookSort bookSort = new BookSort();
        bookSort.setBookId(book.getId());
        bookSort.setSortId(book.getSortId());
        bookSortDao.insert(bookSort);
        return Result.success(200, "添加书籍成功！", null);
    }

    @SaCheckPermission("sys:book:delete")
    @DeleteMapping("deleteBooks")
    public Result deleteBooks(@RequestParam("ids") List<String> ids) {
        List<Book> books = bookDao.selectBatchIds(ids);
        //先将要删除的图片的图片删除
        for (Book book : books) {
            String imgUrl = book.getImageUrl();
            int index = imgUrl.lastIndexOf("/");
            String imgName = imgUrl.substring(index + 1);
            String classPath = null;
            try {
                classPath = ResourceUtils.getURL("classpath:").getPath();
            } catch (FileNotFoundException e) {
                System.out.println("删除的图片为找到");
            }
            String imgPath = classPath + "static/" + imgName;
            File file = new File(imgPath);
            if (file.exists()) {
                file.delete();
            }
        }
        bookDao.deleteBatchIds(ids);
        return Result.success(200, "删除成功！", null);
    }

    @SaCheckPermission("sys:book:update")
    @PutMapping("updateBook")
    public Result updateBook(@RequestBody Book book) {
        //更新书籍除书籍类型外的基本信息
        bookDao.updateById(book);

        //如果书籍类型发生改变则修改book_sort表中数据
        QueryWrapper<BookSort> bookSortQueryWrapper = new QueryWrapper<>();
        bookSortQueryWrapper.eq("book_id", book.getId());
        BookSort targetBookSort = bookSortDao.selectOne(bookSortQueryWrapper);
        if (!book.getSortId().equals(targetBookSort.getSortId())) {
            BookSort bookSort = new BookSort();
            bookSort.setSortId(book.getSortId());
            bookSort.setBookId(book.getId());
            bookSortDao.updateById(bookSort);
        }
        return Result.success(200, "更新书籍信息成功！", null);
    }


    @GetMapping("bookList")
    public Result bookList(@RequestParam(value = "sortIds", required = false) List<String> sortIds,
                           @RequestParam(value = "price", required = false) List<Integer> price,
                           @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                           @RequestParam(value = "keyWords", required = false) String searchWord) {
        pageNumber = pageNumber == null ? 1 : pageNumber;
        pageSize = pageSize == null ? 10 : pageSize;

        QueryWrapper<BookSort> bookSortQueryWrapper = new QueryWrapper<>();
        bookSortQueryWrapper.isNotNull("id");
        //如果有指定书籍类型则查询指定类型，没有则以默认查询所有可用类型（未被逻辑删除）
        if (sortIds != null) {
            bookSortQueryWrapper.in("sort_id", sortIds);
        }
        else {
            //查出可用的书籍类型即没有被逻辑删除的
            QueryWrapper<Sort> sortQueryWrapper = new QueryWrapper<>();
            sortQueryWrapper.eq("usful", 1);
            List<Sort> sorts = sortDao.selectList(sortQueryWrapper);
            List<String> effectiveTypeIds = sorts.stream().map(sort -> sort.getId()).collect(Collectors.toList());
            bookSortQueryWrapper.in("sort_id", effectiveTypeIds);
        }
        //查出选定类型的所有书的id
        List<BookSort> bookSorts = bookSortDao.selectList(bookSortQueryWrapper);

        List<String> bookIds = bookSorts.stream().map(book -> book.getBookId()).collect(Collectors.toList());
        Page<Book> page = new Page<>(pageNumber, pageSize);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("id");
        //根据查到的所有id查询book并分页
        queryWrapper.in("id", bookIds);
        //如果有指定价格区间，则将其加入查询条件
        if (price != null) {
            for (int i = 0; i < price.size(); i += 2) {
                queryWrapper.between("price", price.get(i), price.get(i + 1));
            }
        }
        //如果带有关键字模糊查询则加入查询条件中
        queryWrapper.like((searchWord != null), "name", searchWord);
        Page<Book> bookPage = bookDao.selectPage(page, queryWrapper);
        //给查询到的书本注入书籍类型的ID
        for (Book record : bookPage.getRecords()) {
            QueryWrapper<BookSort> bookSortQueryWrapper1 = new QueryWrapper<>();
            bookSortQueryWrapper1.eq("book_id", record.getId());
            BookSort bookSort = bookSortDao.selectOne(bookSortQueryWrapper1);
            record.setSortId(bookSort.getSortId());
        }
        return Result.success(bookPage);
    }

    /**
     * @methordName:
     * @description: 接收上传的书籍封面，因为前端设置了只上传一张封面，然后自己改写了elementui的上传组件的方法
     * 导致需要使用MultipartFile[]接收
     * @parameters: [imgs] 上传的图片
     * @return: com.example.vueadminjava2.commen.result.Result
     * @author : kono曾淇杭哒！
     * @creat: 2021/11/21 18:08
     */
    @PostMapping("upImg")
    public Result upImg(@RequestParam("imgs") MultipartFile[] imgs) {
        if (imgs.length == 0) {
            return Result.fail(400, "图片上传失败！", null);
        }
        MultipartFile img = imgs[0];
        if (!img.isEmpty()) {
            String originalFilename = img.getOriginalFilename();
            String realFileName = UUID.randomUUID() + originalFilename;
            try {
                String classPath = ResourceUtils.getURL("classpath:").getPath();
                img.transferTo(new File(classPath + "static/" + realFileName));
            } catch (IOException e) {
                return Result.fail(400, "图片上传失败！", null);
            }
            String imageUrl = "http://localhost:" + port + "/static/" + realFileName;
            return Result.success(imageUrl);
        }
        return Result.fail(400, "图片上传失败！", null);

    }
}



