package com.go2group.storyboard.views;

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

import com.go2group.storyboard.model.ConnectAllConfig;
import com.go2group.storyboard.service.ConnectAllConfigService;

public class ConnectorConfigDialog extends TitleAreaDialog {
	
	private Text txtUrl;
	private Text txtApiKey;
	private Text txtAppLinkName;
	private Text txtAppOrigin;
	
	private ConnectAllConfig config;
	
	public ConnectAllConfig getConnectAllConfig() {
		return config;
	}

	public ConnectorConfigDialog(Shell parentShell, ConnectAllConfig config) {
		super(parentShell);
		this.config = config;
	}
	
	@Override
	protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        setTitle("ConnectALL Configuration");
        setMessage("Please enter ConnectALL configuration details",
                IMessageProvider.INFORMATION);
        return contents;	
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);
		
		Label lblUrl = new Label(parent, SWT.NONE);
		lblUrl.setText("URL");
		txtUrl = new Text(parent, SWT.BORDER);
		txtUrl.setSize(100, 50);
		txtUrl.setText(config!=null?config.getUrl():"");

		Label lblApiKey = new Label(parent, SWT.NONE);
		lblApiKey.setText("API Key");
		txtApiKey = new Text(parent, SWT.BORDER);
		txtApiKey.setText(config!=null?config.getApiKey():"");

		Label lblAppLinkName = new Label(parent, SWT.NONE);
		lblAppLinkName.setText("Applink Name");
		txtAppLinkName = new Text(parent, SWT.BORDER);
		txtAppLinkName.setText(config!=null?config.getAppLinkName():"");

		Label lblAppOrigin = new Label(parent, SWT.NONE);
		lblAppOrigin.setText("App Origin");
		txtAppOrigin = new Text(parent, SWT.BORDER);
		txtAppOrigin.setText(config!=null?config.getAppOrigin():"");

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
                if (txtUrl.getText().length() != 0
                        && txtApiKey.getText().length() != 0
                        && txtAppLinkName.getText().length() != 0
                        && txtAppOrigin.getText().length() != 0) {
                	
                	if(config == null) {
	                    config = new ConnectAllConfig(ConnectAllConfigService.getDefaultId(),
	                    		txtUrl.getText(), txtApiKey.getText(),
	                    		txtAppLinkName.getText(), txtAppOrigin.getText());
                	} else {
                		config.setUrl(txtUrl.getText());
                		config.setApiKey(txtApiKey.getText());
                		config.setAppLinkName(txtAppLinkName.getText());
                		config.setAppOrigin(txtAppOrigin.getText());
                	}
                   close();

                } else {
                    setErrorMessage("Please enter all data");
                }
            }
        });
    }
	
}
