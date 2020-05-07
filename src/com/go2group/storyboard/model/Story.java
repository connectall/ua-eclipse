package com.go2group.storyboard.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Story {
	
	private static final String EMPTY_STRING = "";

	private String id;
	
	private String title;
	
	private String description;
	
	private String priority;
	
	private StoryStatus status;
	
	private String linkedStoryId;
	
	private long createdTime;
	
	private long modifiedTime;
	
	private String createdBy;
	
	private String assignedTo;
	
	private boolean isNew = false;
	
	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	
	public Story() {};
	
	public Story(String id, String title, String description, String priority, StoryStatus status) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public StoryStatus getStatus() {
		return status;
	}

	public String getStatusAsString() {
		return status!=null?status.name():StoryStatus.New.name();
	}

	public void setStatus(StoryStatus status) {
		this.status = status;
	}
	
	public String getLinkedStoryId() {
		return linkedStoryId;
	}

	public void setLinkedStoryId(String linkedStoryId) {
		this.linkedStoryId = linkedStoryId;
	}

	public long getCreatedTime() {
		return createdTime;
	}
	
	public String getCreatedTimeAsString() {
		if(createdTime == 0) return EMPTY_STRING;
		return DATE_TIME_FORMAT.format(new Date(createdTime));
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public long getModifiedTime() {
		return modifiedTime;
	}
	
	public String getModifiedTimeAsString() {
		if(modifiedTime == 0) return EMPTY_STRING;
		return DATE_TIME_FORMAT.format(new Date(modifiedTime));
	}

	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	@Override
    public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null? 0 : id.hashCode());

    	return result;
    }

    public boolean equals(Object obj) {
    	if (obj == null)
    		return false;

    	if (this.getClass() != obj.getClass())
    		return false;    	
    	
    	Story story = (Story) obj;
    	return id.equals(story.getId());
    }


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Story [Id=").append(id).append(", Title=").append(title)
			.append(",LinkedStoryId=").append(linkedStoryId)
			.append(",Status=").append(status).append("]");
		return sb.toString();
	}

}
