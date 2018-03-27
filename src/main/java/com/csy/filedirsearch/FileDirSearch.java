package com.csy.filedirsearch;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */
@Component
public class FileDirSearch {

    public List<String> search(Path root, SearchOption searchOption, String searchStr, long timeout) throws IOException {

        long endTimeInMills = Calendar.getInstance().getTimeInMillis()+timeout;
        List<String> filePaths = new ArrayList<>();

        Files.walkFileTree(root, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if(SearchOption.FOLDER == searchOption || SearchOption.BOTH == searchOption) {
                    if ( attrs.isDirectory()) {
                        Pattern p = Pattern.compile(searchStr);
                        Matcher m = p.matcher(dir.getFileName().toString());
                        if (m.find()){
                            filePaths.add(dir.toAbsolutePath().toString());
                        }
                    } else if ( attrs.isSymbolicLink()) {
                        System.out.println(dir + " is a symbolic link.");
                    } else {
                        System.out.println(dir + " is not a regular dir or symbolic link.");
                    }
                }
                return terminateOrContinueSearch(endTimeInMills);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
               if(SearchOption.FILE == searchOption || SearchOption.BOTH == searchOption) {
                   if ( attrs.isRegularFile()) {
                       Pattern p = Pattern.compile(searchStr);
                       Matcher m = p.matcher(file.getFileName().toString());
                       if (m.find()){
                           filePaths.add(file.toAbsolutePath().toString());
                       }
                   } else if ( attrs.isSymbolicLink() ) {
                       System.out.println(file + " is a symbolic link.");
                   } else {
                       System.out.println(file + " is not a regular file or symbolic link.");
                   }
               }
                return terminateOrContinueSearch(endTimeInMills);
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return terminateOrContinueSearch(endTimeInMills);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return terminateOrContinueSearch(endTimeInMills);
            }
        });

        return filePaths;
    }

    private boolean isTimeout(long endTimeInMills){
        return Calendar.getInstance().getTimeInMillis()>=endTimeInMills;
    }

    private FileVisitResult terminateOrContinueSearch(long endTimeInMills){
        return isTimeout(endTimeInMills) ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
    }

}
