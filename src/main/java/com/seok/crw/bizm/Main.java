package com.seok.crw.bizm;

import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {

        BizMCrawlingProcess process = new BizMCrawlingProcess();

        List<OutGoingCallNumber> items = process.crawling();

        for (OutGoingCallNumber item: items) {
            System.out.println(item.toString());
        }


    }
}
