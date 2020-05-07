package com.go2group.storyboard.views;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

public abstract class AbstractTextEditingSupport extends EditingSupport {
	
    private final TableViewer viewer;
    private final CellEditor editor;


	public AbstractTextEditingSupport(TableViewer viewer) {
		super(viewer);
        this.viewer = viewer;
        this.editor = new TextCellEditor(viewer.getTable());		
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected abstract Object getValue(Object element);

	@Override
	protected abstract void setValue(Object element, Object userInputValue);
	
	protected TableViewer getTableViewer() {
		return this.viewer;
	}
	
}
