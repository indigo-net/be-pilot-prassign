package com.shuttle.users.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.shuttle.users.model.dto.Status;
import com.shuttle.users.model.dto.User;
import com.shuttle.users.service.UserService;
import com.shuttle.users.service.UserServiceImpl;

@WebServlet("/users")
public class UserController extends HttpServlet {
	
	@Override
	public void init() throws ServletException{
		try {
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.getApplicationDefault())
					.build();
			FirebaseApp.initializeApp(options);
			System.out.println("FirebaseApp 초기화 성공");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("FirebaseApp 초기화 실패");
		}
	}
	
	private static final long serialVersionUID = 1L;
	private static UserService userService = UserServiceImpl.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		System.out.println("get: "+ action);
		action = action==null ? "" : action;
		JsonObject jsonObject = new JsonObject();
		String jsonResponse = null;
		switch(action) {
		case "login":
			response.setStatus(HttpServletResponse.SC_OK);
			jsonObject.addProperty("msg", "운영진 접속에 성공하였습니다.");
			jsonResponse = jsonObject.toString();
			break;
		case "list":
			try {
				Map<String, List<User>> userList = userService.selectAll();
				Gson gson = new Gson();
				jsonResponse = gson.toJson(userList);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				jsonObject.addProperty("msg", "조회에 실패하였습니다.");
				jsonResponse = jsonObject.toString();
			}
			break;
		case "pin":
			response.setStatus(HttpServletResponse.SC_OK);
			jsonObject.addProperty("msg", "PIN 번호 조회에 성공하였습니다.");
			jsonObject.addProperty("pin", "1111");
			jsonResponse = jsonObject.toString();
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		
		out.print(jsonResponse);
		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonObject jsonRequestObject = parsingJson(request);
		JsonObject jsonObject = new JsonObject();
		String jsonResponse = null;
		String action = jsonRequestObject.get("action").getAsString();
		System.out.println("post: "+ jsonRequestObject.toString());
		switch(action) {
		case "notify":
			JsonArray tokensArray = jsonRequestObject.get("tokens").getAsJsonArray();
			List<String> tokens = new ArrayList<>();
			for(JsonElement tokenElement : tokensArray) {
				String token = tokenElement.getAsString();
				tokens.add(token);
			}
			try {
				for(String token:tokens) {
					Message message = Message.builder()
							.putData("msg", "게임이 곧 시작됩니다. 대기해주세요.")
							.setToken(token)
							.build();
					String responseMessage = FirebaseMessaging.getInstance().send(message);
					System.out.println("푸시 알림 전송 성공: "+responseMessage);
				}
				response.setStatus(HttpServletResponse.SC_OK);
				jsonObject.addProperty("msg", "푸시 알림이 전송되었습니다.");
			} catch (FirebaseMessagingException e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				jsonObject.addProperty("msg", "푸시 알림 전송에 실패하였습니다.");
			}
			break;
		case "regist":
			try {
				String uuid = jsonRequestObject.get("uuid").getAsString();
				String userName = jsonRequestObject.get("userName").getAsString();
				String arriveTimeStamp = jsonRequestObject.get("arriveTimeStamp").getAsString();
				String status = Status.REST.getName();
				String fcmToken = jsonRequestObject.get("fcmToken").getAsString();
				
				userService.regist(new User(uuid, userName, arriveTimeStamp, status, fcmToken));
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
				jsonObject.addProperty("msg", "접속에 성공하였습니다.");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				jsonObject.addProperty("msg", "접속에 실패하였습니다.");
				
			}
			break;
		}
		jsonResponse = jsonObject.toString();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		
		out.print(jsonResponse);
		out.flush();
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uuid = request.getParameter("uuid");
		System.out.println("delete: "+ uuid);
		JsonObject jsonObject = new JsonObject();
		try {
			switch(uuid) {
			case "all":
				userService.deleteAll();
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
				jsonObject.addProperty("msg", "초기화에 성공하였습니다.");
				break;
			default:
				userService.delete(uuid);
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
				jsonObject.addProperty("msg", "삭제에 성공하였습니다.");
				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			jsonObject.addProperty("msg", "삭제에 실패하였습니다.");
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		String jsonResponse = jsonObject.toString();
		out.print(jsonResponse);
		out.flush();
	}	
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonObject jsonObject = parsingJson(request);
		String action = jsonObject.get("action").getAsString();
		System.out.println("update: "+ jsonObject.toString());
		try {
			switch(action) {
			case "update":
				String uuid = jsonObject.get("uuid").getAsString();
				String statusCode = jsonObject.get("status").getAsString();
				String status = null;
				switch(statusCode) {
				case "REST":
					status = Status.REST.getName();
					break;
				case "READY":
					status = Status.READY.getName();
					break;
				case "GAME":
					status = Status.GAME.getName();
					break;
				default:
					System.out.println("잘못된 statusCode입니다.");
					status = Status.REST.getName();
					break;
				}
				userService.update(uuid, status);
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
				jsonObject.addProperty("msg", "선수의 상태 변경에 성공하였습니다.");
				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			jsonObject.addProperty("msg", "상태 변경 중 오류가 발생했습니다.");
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		String jsonResponse = jsonObject.toString();
		out.print(jsonResponse);
		out.flush();
	}
	
	protected JsonObject parsingJson(HttpServletRequest request) {
		BufferedReader reader;
		JsonObject jsonObject = null;
		try {
			reader = request.getReader();
			StringBuilder jsonBuilder = new StringBuilder();
			String line;
			while((line=reader.readLine())!=null) jsonBuilder.append(line);
			String jsonData = jsonBuilder.toString();
			
			jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
