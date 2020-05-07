package com.go2group.storyboard.model;

import java.util.Arrays;

public enum StoryStatus {
	
	New,
	Started,
	Completed,
	Closed;

	public static String[] getValuesAsArray() {
	    return Arrays.stream(StoryStatus.values()).map(Enum::name).toArray(String[]::new);
	}
	
}
