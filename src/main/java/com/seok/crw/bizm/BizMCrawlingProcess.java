package com.seok.crw.bizm;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BizMCrawlingProcess {


    private final static String BIZM_LOGIN_ID = "-";
    private final static String BIZM_LOGIN_PASSWORD  = "-";
    private Log logger = LogFactory.getLog(BizMCrawlingProcess.class);
    private HttpClient client;

    public BizMCrawlingProcess () {
        client =  HttpClients.createDefault();
    }

    public int login() throws IOException {

        HttpGet get = new HttpGet("https://www.bizmsg.kr/partner/login_page/");
        HttpResponse response = client.execute(get);

        Document document = Jsoup.parse(IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8));
        String token = document.select("input[name=csrfmiddlewaretoken]").val();

        HttpPost post = new HttpPost("https://www.bizmsg.kr/login/partner/");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username",BIZM_LOGIN_ID));
        params.add(new BasicNameValuePair("password",BIZM_LOGIN_PASSWORD));
        params.add(new BasicNameValuePair("csrfmiddlewaretoken",token));
        params.add(new BasicNameValuePair("next",""));
        post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

        response = client.execute(post);

        return response.getStatusLine().getStatusCode();

    }

    public List<OutGoingCallNumber> crawling() throws IOException {

        List<OutGoingCallNumber> items = new ArrayList<>();
        if(login() == 200){
            logger.error("로그인 실패!!");
            return Collections.emptyList();
        }

        int i=1;
        while(i < 6) {
            HttpPost post = new HttpPost("https://www.bizmsg.kr/callback/callback_list");

            List<NameValuePair> params = Lists.newArrayList();
            params.add(new BasicNameValuePair("page",i+""));
            params.add(new BasicNameValuePair("select_stat","all"));
            params.add(new BasicNameValuePair("search",""));
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = client.execute(post);

            Document document = Jsoup.parse(IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8));
            Elements elements = document.select("tbody tr");

            for (Element element : elements) {
                Elements row = element.select("td.text-center");
                OutGoingCallNumber item = new OutGoingCallNumber();
                item.setCallNumber(row.get(1).html());
                item.setStatus(OutGoingCallNumberStatus.findByDescription(row.get(5).html()));
                items.add(item);
            }
            i++;
        }

        return items;
    }
}
