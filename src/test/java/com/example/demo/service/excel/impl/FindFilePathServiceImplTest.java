package com.example.demo.service.excel.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FindFilePathServiceImplTest {

    @Autowired
    private FindFilePathServiceImpl findFilePathServiceImpl;

    @Test
    void fileWalker() {
        findFilePathServiceImpl.fileWalker("D:\\data\\inventory","D:\\data");
    }
}