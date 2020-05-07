package com.go2group.storyboard.views;

import org.apache.http.conn.HttpHostConnectException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;

import com.go2group.storyboard.model.Story;
import com.go2group.storyboard.model.StoryStatus;
import com.go2group.storyboard.service.SyncService;

public class StatusEditingSupport extends EditingSupport {
	
	private final TableViewer viewer;

	public StatusEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
        return new ComboBoxCellEditor(viewer.getTable(), StoryStatus.getValuesAsArray());	
    }

	@Override
	protected Object getValue(Object element) {
		String[] statuses = StoryStatus.getValuesAsArray();
		Story story = (Story) element;
		int index = 0;
		for (int i = 0; i < statuses.length; i++) {
			if(statuses[i].equals(story.getStatus())) {
				index = i;
				break;
			}
		}
		return index;
	}

	@Override
	protected void setValue(Object element, Object value) {
		Integer intVal = (Integer) value;
		if(intVal < 0) intVal = 0;
		Story story = (Story) element;
		String[] statuses = StoryStatus.getValuesAsArray();
		story.setStatus(StoryStatus.valueOf(statuses[intVal]));
		viewer.update(story, null);
		try {
			new SyncService().updateStory(story);
		} catch (HttpHostConnectException e) {
            Display.getCurrent().timerExec(10, new Runnable() {
                public void run() {
					MessageDialog.openError(
							viewer.getControl().getShell(),
							"Story Board",
							"Connection Error! Unable to sync the changes to the server.\n\nCause:"+e.getMessage());
                }
            });
		}
	}

}
