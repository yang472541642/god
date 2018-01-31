package com.gad;

import com.gad.domin.dto.PlanConfig;
import com.gad.domin.dto.PlanDetailDTO;
import com.gad.domin.dto.RemotePlanDTO;
import com.gad.handler.PlanNoticeHandler;
import com.gad.service.CpxzsBizService;
import javafx.scene.control.Alert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GadApplicationTests {
	@Resource
	CpxzsBizService cpxzsBizService;
	@Resource
	JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String fromUserName;
	@Value("${spring.mail.tousername}")
	private String toUserName;
	@Test
	public void contextLoads() {
		PlanConfig planConfig = new PlanConfig();
		//String res = cpxzsBizService.getHistoryNumber("");
		//Assert.assertTrue(res != null);
		
		/*Thread thread = new Thread(new PlanNoticeHandler(javaMailSender,fromUserName, toUserName));
		thread.start();*/
		List<String> list = new ArrayList<>();
		String date = "2018-01-18";
		/*for(int i = 1; i<=30; i++) {
			if(i<10) {
				cpxzsBizService.getHistoryNumber(date+"0"+String.valueOf(i), list);
			}else{
				cpxzsBizService.getHistoryNumber(date+String.valueOf(i), list);
			}
		}*/
		cpxzsBizService.getHistoryNumber(date, list);
		List<String> list2 = list.subList(list.size() - 1, list.size());
		int s = Integer.valueOf(String.valueOf(list2.get(0).charAt(4))) % 2;
		checkhou12(list, 0);
		while (true){
		
		}
	}
	
	public static void handle(List<String> list) {
		Map<String, Integer> map0 = new HashMap<>();
		Map<String, Integer> map1 = new HashMap<>();
		Map<String, Integer> map2 = new HashMap<>();
		Map<String, Integer> map3 = new HashMap<>();
		Map<String, Integer> map4 = new HashMap<>();
		Map<String, Integer> map5 = new HashMap<>();
		Map<String, Integer> map6 = new HashMap<>();
		Map<String, Integer> map7 = new HashMap<>();
		Map<String, Integer> map8 = new HashMap<>();
		Map<String, Integer> map9 = new HashMap<>();
		
		for(int i = 0; i < list.size() - 1; i++) {
			String current = list.get(i).substring(4,5);
			String next = list.get(i+1).substring(4,5);
			if(current.equals("0")) {
				if(map0.get(next) == null){
					map0.put(next, 1);
				}else {
					map0.put(next, map0.get(next) + 1);
				}
			}
			if(current.equals("1")) {
				if(map1.get(next) == null){
					map1.put(next, 1);
				}else {
					map1.put(next, map1.get(next) + 1);
				}
			}
			if(current.equals("2")) {
				if(map2.get(next) == null){
					map2.put(next, 1);
				}else {
					map2.put(next, map2.get(next) + 1);
				}
			}
			if(current.equals("3")) {
				if(map3.get(next) == null){
					map3.put(next, 1);
				}else {
					map3.put(next, map3.get(next) + 1);
				}
			}
			if(current.equals("4")) {
				if(map4.get(next) == null){
					map4.put(next, 1);
				}else {
					map4.put(next, map4.get(next) + 1);
				}
			}
			if(current.equals("5")) {
				if(map5.get(next) == null){
					map5.put(next, 1);
				}else {
					map5.put(next, map5.get(next) + 1);
				}
			}
			if(current.equals("6")) {
				if(map6.get(next) == null){
					map6.put(next, 1);
				}else {
					map6.put(next, map6.get(next) + 1);
				}
			}
			if(current.equals("7")) {
				if(map7.get(next) == null){
					map7.put(next, 1);
				}else {
					map7.put(next, map7.get(next) + 1);
				}
			}
			if(current.equals("8")) {
				if(map8.get(next) == null){
					map8.put(next, 1);
				}else {
					map8.put(next, map8.get(next) + 1);
				}
			}
			if(current.equals("9")) {
				if(map9.get(next) == null){
					map9.put(next, 1);
				}else {
					map9.put(next, map9.get(next) + 1);
				}
			}
			
		}
		System.out.println("list = [" + list + "]");
		
	}
	
	
	public static boolean checkhou12(List<String> list, int max){
		Map<Integer,String> map = new HashMap<Integer, String>();
		
		int failedTotal = 0;
		int success = 0;
		double maxfailedtotal = 0;
		
		double win = 0;
		
		Map<String, String> map1 = new HashMap<>();
		map1.put("0", "036");
		map1.put("1", "147");
		map1.put("2", "258");
		map1.put("3", "369");
		map1.put("4", "047");
		map1.put("5", "158");
		map1.put("6", "269");
		map1.put("7", "037");
		map1.put("8", "148");
		map1.put("9", "259");
		//判断和值
		boolean iscon = true;
		int count = 0;
		for(int i = 0; i< list.size() - 1; i++) {
			if(iscon && win >= 50) {
				count++;
				iscon = false;
			}
			if(i % 120 == 0) {
				if(iscon) {
					maxfailedtotal += win;
				}
				System.out.println("ddddddddddddddddddddddddddddddddd");
				win=0;
				iscon = true;
			}
			if(i == list.size() - 4) {
				break;
			}
			String current = list.get(i).substring(4,5);
			String keyString = map1.get(current);
			for(int k=i+1;k<i+2;k++) {
				String s = list.get(k).substring(4,5);
				if(keyString.indexOf(s) != -1) {
					
					if(k==i+1) {
						win+=13.5;
					}/*
					if(k==i+2) {
						win+=7.5;
					}
					if(k==i+3) {
						win+=1.5;
					}*/
					System.out.println("if="+list.get(i)+"期号："+list.get(i)+"------字和中" + "--"+win);
					success++;
					break;
				}
				if(k == i+1) {
					failedTotal++;
					win-=6;
					System.out.println("if="+list.get(i)+"期号："+list.get(i)+"------字和挂" + "--"+win);
					
				}
			}
			
		}
		System.out.println("挂："+failedTotal+"中："+success);
		System.out.println("连挂："+maxfailedtotal);
		System.out.println("连："+count);
		return true;
	}
	
	

}
