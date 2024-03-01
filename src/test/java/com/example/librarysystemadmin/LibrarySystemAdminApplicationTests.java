package com.example.librarysystemadmin;

import com.example.librarysystemadmin.domain.InterceptorArr;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class LibrarySystemAdminApplicationTests {

    public String all = "";

    @Test
    void contextLoads() {
        Map<String, String[]> jobs = getStringMap();

        jobs.forEach((k, v) -> {
            for (String s : v) {
                all += "/Api/" + k + "/" + s + ",";
            }
        });
        System.out.println(all);

//        String[] allExcludedPaths = Stream.of(interceptorArr.getUser(),
//                        interceptorArr.getBook(),
//                        interceptorArr.getUtil())
//                .flatMap(item -> Arrays.stream(item))
//                .toArray(String[]::new);
//        System.out.println(Arrays.toString(jobs));
    }

    private static Map<String, String[]> getStringMap() {
        String[] user = {"login", "register"};
        String[] book = {"getBookList", "getBookInfo", "getNewBookList"};
        String[] util = {"captcha"};

        // 将数组转换为一个由逗号分隔的路径字符串，并传递给excludePathPatterns
        Map<String, String[]> jobs = new HashMap<String, String[]>() {{
            put("user", user);
            put("book", book);
            put("util", util);
        }};
        return jobs;
    }

}
