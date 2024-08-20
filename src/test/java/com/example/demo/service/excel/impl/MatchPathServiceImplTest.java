package com.example.demo.service.excel.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

class MatchPathServiceImplTest {

    @Autowired
    private MatchPathServiceImpl matchPathServiceImpl;


    @Test
    void matchPath() {
        matchPathServiceImpl.matchPath("D:\\data\\inventory","D:\\data");
    }
}