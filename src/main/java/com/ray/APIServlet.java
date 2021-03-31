package com.ray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;


import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.*;
import java.net.*;
import org.apache.http.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class APIServlet implements Servlet {
	
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 建立CloseableHttpClient
		Map map = new HashMap(5);
		String result  = "";
		HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        
//        String httpurl = "https://datacenter.taichung.gov.tw/swagger/OpenData/b0043087-5f1c-47fd-be1a-5d10047aefd2";
        String Stock_url = "https://www.esunbank.com.tw/bank/personal/deposit/rate/forex/exchange-rate-chart?Currency=USD/TWD";
        //yahoo url
//        String yahoo_url = "http://tw.stock.yahoo.com/q/q?s=2002";
//        URL url_yahoo_exchange_rates = new URL("http://tw.stock.yahoo.com/q/q?s=2002");
        URL url_esunbank_foreign_exchange_rates = new URL("https://www.esunbank.com.tw/bank/personal/deposit/rate/forex/foreign-exchange-rates");
//        https://www.esunbank.com.tw/bank/personal/deposit/rate/forex/foreign-exchange-rates
        
        /*
         * 向網頁伺服發出請求，並將回應分析成document。
         * 第一個參數是：請求位置與QueryString。
         * 第二個參數是：連線時間(毫秒)，在指定時間內若無回應則會丟出IOException
         */       
        Document doc = Jsoup.parse(url_esunbank_foreign_exchange_rates, 3000);
//        Elements tables = doc.getElementsByTag("table");
        //取回所center下所有的table
        Elements tables = doc.select("table");
        
        ArrayList<String> downServers = new ArrayList<>();
        Element table = doc.select("table").get(0); //select the first table.
        Elements rows = table.select("tr");
        JSONArray jsa=new JSONArray();
        for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
        	JSONObject jsonb=new JSONObject();
            Element row = rows.get(i);
            Elements cols = table.select("tr").select("td");
            for (int j = 0; j < cols.size(); j++) {
            	
            	System.out.println(cols.get(j).text());
                try {
					jsonb.put(String.valueOf(i), cols.get(j).text());
					downServers.add(cols.get(j).text());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               
            }
            jsa.put(jsonb);
            System.out.println(downServers);

            
//            if (cols.get(3).text().equals("Titan")) {
//                if (cols.get(7).text().equals("down"))
//                    downServers.add(cols.get(5).text());
//
//                do {
//                    if(i < rows.size() - 1)
//                       i++;
//                    row = rows.get(i);
//                    cols = row.select("td");
//                    if (cols.get(7).text().equals("down") && cols.get(3).text().equals("")) {
//                        downServers.add(cols.get(5).text());
//                    }
//                    if(i == rows.size() - 1)
//                        break;
//                }
//                while (cols.get(3).text().equals(""));
//                i--; //if there is two Titan names consecutively.
//            }
            
            
            
        }
        String json = new Gson().toJson(downServers );
        System.out.println(json);
//        ArrayList<String> downServers = new ArrayList<>();
//        Element table2 = doc.select("table").get(0); 
//        Elements rows = table2.select("tr");
//
//        for (int i = 0; i < rows.size(); i++) { 
//            Element row = rows.get(i);
//            Elements cols = rows.select("td");
//            for(int j = 0; j<=7;j++) {
//            	 downServers.add(cols.get(j).text());
//            }
//        }
        
        
        int tableIndex = -1;
        
        for (int i = 0; i < tables.size(); i++) {
            Element element = tables.get(i);
            String text = element.text();
            if (text.indexOf("table") > -1) {//inteTable1
                tableIndex = i;
                break;
            }
        }
        List<CrawlerDataStruts> list = new ArrayList<CrawlerDataStruts>();
        	
        	Elements trs = doc.select("table").select("tr");
        	
        	
        	String[] arr1 = new String[7];
       
        	for(int i = 0;i<trs.size();i++){	
        		Elements tds = trs.get(i).select("td");
        		for(int j = 0;j<tds.size();j++){
                	
                    String text = tds.get(j).text();
//                    System.out.println(text);           

                }                
        	}        	
        
//        System.out.println(list);
//        System.out.println("打印list  res ");
        
        Date date = new Date();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("current date time");
//        System.out.println(formatter.format(date));
        
        
        Calendar calendar = Calendar.getInstance(); 

        Gson gson = new Gson();
        String jsonArray = gson.toJson(list);
	    res.setContentType("text/json; charset=UTF-8");  
		PrintWriter out=res.getWriter();
		
		out.println(json.toString());
//		out.println(obj);
//		out.println(jsa.toString());

        //https://www.esunbank.com.tw/bank/personal/deposit/rate/forex/foreign-exchange-rates
//        https://www.esunbank.com.tw/bank/personal/deposit/rate/forex/exchange-rate-chart?Currency=USD/TWD
        try {
            
            URL url = new URL(Stock_url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");           
            connection.setConnectTimeout(15000); 
            connection.setReadTimeout(60000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
               // System.out.println(sbf.toString());
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();
		}

	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}



}
