package com.csy.filedirsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FileDirSearchApplication implements CommandLineRunner {
    @Autowired
    private FileDirSearch fileDirSearch;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FileDirSearchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if(args[0] == null || args[0].isEmpty()){
            throw new IllegalArgumentException("args[0] should be an root search directory");
        }
        String searchOptionStr = "BOTH";
        if(args[1] != null || !args[1].isEmpty()){
            searchOptionStr = args[1].toUpperCase();
            if(!Arrays.toString(SearchOption.values()).contains(searchOptionStr)){
                throw new IllegalArgumentException("args[1] should be SeachOption(file/folder/both).");
            }
        }
        if(args[2] == null || args[2].isEmpty()){
            throw new IllegalArgumentException("args[2] should search strings");
        }
        if(args[3] == null || args[3].isEmpty()){
            throw new IllegalArgumentException("args[3] should be timeout of search in milliseconds.");
        }

        Path root = Paths.get(args[0]);
        SearchOption searchOption = SearchOption.valueOf(searchOptionStr);
        String searchPattern = args[2];
        long timeout = Long.parseLong(args[3]);
        
        fileDirSearch.search(root, searchOption, searchPattern, timeout).stream().forEach(System.out::println);
    }
}
