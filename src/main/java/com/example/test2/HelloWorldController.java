package com.example.test2;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloWorldController {
    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C:\\folder";

    @RequestMapping("/")
    public String sayHello() {
        return "Hello Spring Boot!!";
    }


    @RequestMapping("/list")
    public List<String> filesList() {
        File folder = new File(UPLOADED_FOLDER);
        File[] listOfFiles = folder.listFiles();
        List<String> list = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                list.add(listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }

        return list;
    }

//    @RequestMapping(path = "/download", method = RequestMethod.GET)
//    public void download(HttpServletResponse response) throws IOException {
//
//        // ...
//
//        File file = new File("C:\\folder");
//
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
////        return ResponseEntity.ok()
////                .headers(((ServletServerHttpResponse.ServletResponseHttpHeaders) new HttpHeaders()))
////                .contentLength(file.length())
////                .contentType(MediaType.parseMediaType("application/octet-stream"))
////                .body(resource);
//        response.flushBuffer();
//    }

    @RequestMapping(value = "/files/{file_name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("file_name") String fileName) {
        File file = new File("C:\\folder\\TODO.txt");
//        file = new File("C:\\folder\\Шефер К., Хо К., Харроп Р. - Spring 4 для профессионалов - 2015.pdf");
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        return fileSystemResource;
    }


    @RequestMapping(value = "/getfiles/{file_name}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getFile(@PathVariable("file_name") String fileName, HttpServletResponse response) {
        ResponseEntity<byte[]> result = null;
        try {
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            byte[] resultFileArray = Files.readAllBytes(path);

            response.setStatus(HttpStatus.OK.value());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(resultFileArray.length);

            result = new ResponseEntity<byte[]>(resultFileArray, headers, HttpStatus.OK);
        } catch (java.nio.file.NoSuchFileException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return result;
    }


}

