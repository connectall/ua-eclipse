package com.go2group.storyboard.views;

import java.util.Date;
import java.util.UUID;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.go2group.storyboard.model.Story;
import com.go2group.storyboard.model.StoryStatus;

public class StoryCreationDialog extends TitleAreaDialog {
	
	private Text txtTitle;
	private Text txtDescription;
	private Text txtPriority;
	private Text txtCreatedBy;
	private Text txtAssignedTo;
	
	private Story story;
	
	public Story getStory() {
		return story;
	}

	public StoryCreationDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        setTitle("New Story");
        setMessage("Please enter the new story details",
                IMessageProvider.INFORMATION);
        return contents;	
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);
		
		Label lblTitle = new Label(parent, SWT.NONE);
		lblTitle.setText("Title");
		txtTitle = new Text(parent, SWT.BORDER);
		txtTitle.setSize(64, 32);

		Label lblDesc = new Label(parent, SWT.NONE);
		lblDesc.setText("Description");
		txtDescription = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);

		Label lblPriority = new Label(parent, SWT.NONE);
		lblPriority.setText("Priority");
		txtPriority = new Text(parent, SWT.BORDER);

		Label lblCreatedBy = new Label(parent, SWT.NONE);
		lblCreatedBy.setText("Created By");
		txtCreatedBy = new Text(parent, SWT.BORDER);
		txtCreatedBy.setText("vijay");
		
		Label lblAssignedTo = new Label(parent, SWT.NONE);
		lblAssignedTo.setText("Assigned To");
		txtAssignedTo = new Text(parent, SWT.BORDER);

		return parent;
	}
	
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        ((GridLayout) parent.getLayout()).numColumns++;

        Button button = new Button(parent, SWT.PUSH);
        button.setText("OK");
        button.setFont(JFaceResources.getDialogFont());
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (txtTitle.getText().length() != 0) {
                    story = new Story();
                    story.setId(UUID.randomUUID().toString());
                    story.setTitle(txtTitle.getText());
                    story.setDescription(txtDescription.getText());
                    story.setPriority(txtPriority.getText());
                    story.setCreatedBy(txtCreatedBy.getText());
                    story.setAssignedTo(txtAssignedTo.getText());
                    story.setStatus(StoryStatus.New);
                    long curTime = new Date().getTime();
                    story.setCreatedTime(curTime);
                    story.setModifiedTime(curTime);
                    close();
                } else {
                    setErrorMessage("Please enter all data");
                }
            }
        });
    }


}
