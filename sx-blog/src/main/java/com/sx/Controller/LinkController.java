package com.sx.Controller;

import com.sx.domain.ResponseResult;
import com.sx.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;

@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    LinkService linkService;
    @RequestMapping("/getAllLink")
    public ResponseResult getAllLink(){
        Scanner scanner = new Scanner(System.in);
        return linkService.getAllLink();
    }

}
