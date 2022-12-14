package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.requests.CredentialFormRequest;
import com.udacity.jwdnd.course1.cloudstorage.requests.FileFormRequest;
import com.udacity.jwdnd.course1.cloudstorage.requests.NoteFormRequest;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("/upload")
    public String newFile(Authentication authentication, @ModelAttribute("newFile") FileFormRequest newFile, Model model) throws IOException {

        String userName = authentication.getName();
        User user = userService.getUser(userName);
        Integer userId = user.getUserId();
        String[] fileListings = fileService.getFileByUserId(userId);
        MultipartFile multipartFile = newFile.getFile();
        String fileName = multipartFile.getOriginalFilename();
        boolean fileIsDuplicate = false;
        for (String fileListing: fileListings) {
            if (fileListing.equals(fileName)) {
                fileIsDuplicate = true;
                break;
            }
        }
        if (!fileIsDuplicate) {
            fileService.addFile(multipartFile, userName);
            model.addAttribute("result", "success");
        } else {
            model.addAttribute("result", "error");
            model.addAttribute("message", "You have tried to add a duplicate file.");
        }
        model.addAttribute("files", fileService.getFileByUserId(userId));
        return "result";
    }

    @GetMapping(value = "/find/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getFile(@PathVariable String fileName) {
        return fileService.getFile(fileName).getFileData();
    }

    @GetMapping(value = "/delete/{fileName}")
    public String deleteFile(Authentication authentication, @PathVariable String fileName, Model model) {

        fileService.deleteFile(fileName);
        User user = userService.getUser(authentication.getName());
        model.addAttribute("files", fileService.getFileByUserId(user.getUserId()));
        model.addAttribute("result", "success");
        return "result";
    }
}
