package com.example.test2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class FileDownloadController {
    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C:\\folder";



    @RequestMapping("/get-list-of-files")
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

    @RequestMapping(value = "/get-file/{file_name}", method = RequestMethod.GET)
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

