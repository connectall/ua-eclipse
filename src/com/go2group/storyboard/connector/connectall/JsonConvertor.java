package com.go2group.storyboard.connector.connectall;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.go2group.storyboard.model.Story;
import com.go2group.storyboard.model.StoryStatus;

public class JsonConvertor {
/*	
	private static final String DELIMITER = ":";
	private static final String COMMA = ",";
	private static final String QUOTES = "\"";

	public static String toJson(Map<String, String> fields) {
		if(fields == null) {
			return "{}";
		}
		
		StringBuilder json = new StringBuilder();
		json.append("{");
		
		boolean isFirst = true;
		for (Entry<String, String> field : fields.entrySet()) {
			if(!isFirst) {
				json.append(COMMA);
			} else {
				isFirst = false;
			}
			
			json.append(QUOTES).append(field.getKey()).append(QUOTES).append(DELIMITER)
				.append(QUOTES).append(field.getValue()).append(QUOTES);
		}
		
		json.append("}");
		return json.toString();
	}
*/	
	
	public static String toJson(Map<String, Object> jsonMap) {
		JSONObject jsonObj = new JSONObject(jsonMap);
		return jsonObj.toString();
	}
		
	public static List<Story> getStories(String json) {
		List<Story> stories = new ArrayList<>();
		
		try {
			JSONObject jsonObj = new JSONObject(json);
			int totalRecords = jsonObj.getInt("totalrecords");
			if(totalRecords == 0) {
				return stories;
			}
			
			JSONArray data = jsonObj.getJSONArray("data");
			if(data == null) {
				return stories;
			}
			
			for (int i = 0; i < data.length(); i++) {
				Story story = getStory(data.getJSONObject(i));
				stories.add(story);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return stories;
	}
	
	private static Story getStory(JSONObject entity) {
		Story story = null;
		try {
			JSONObject fields = entity.getJSONObject("fields");
			if(fields != null) {
				story = new Story();
				story.setId(fields.has("id")?fields.getString("id"):"");
				story.setTitle(fields.has("title")?fields.getString("title"):"");
				story.setDescription(fields.has("description")?fields.getString("description"):"");
				story.setStatus(fields.has("status")?StoryStatus.valueOf(fields.getString("status")):StoryStatus.New);
				story.setPriority(fields.has("priority")?fields.getString("priority"):"");
				story.setLinkedStoryId(fields.has("linked_story_id")?fields.getString("linked_story_id"):"");
				story.setCreatedBy(fields.has("created_by")?fields.getString("created_by"):"");
				story.setAssignedTo(fields.has("assigned_to")?fields.getString("assigned_to"):"");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return story;
	}
	
	public static String getLinkedId(String json) {
		String linkedId = "";
		try {
			JSONObject obj = new JSONObject(json);
			linkedId = obj.has("id")?obj.getString("id"):"";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return linkedId;
	}
	
	public static Map<String, String> buildAsMap(Story story) {
		Map<String, String> fields = new LinkedHashMap<>();
		fields.put("id", story.getId());
		fields.put("title", story.getTitle());
		fields.put("description", story.getDescription());
		fields.put("status", story.getStatusAsString());
		fields.put("priority", story.getPriority());
		fields.put("created_by", story.getCreatedBy());
		fields.put("assigned_to", story.getAssignedTo());
		fields.put("linked_story_id", story.getLinkedStoryId());
		fields.put("created_time", story.getCreatedTimeAsString());
		fields.put("modified_time", story.getModifiedTimeAsString());
		
		fields.put("_LINKED_RECORD_ID", story.getLinkedStoryId());
		return fields;
	}
	
}
