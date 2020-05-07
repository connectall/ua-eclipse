package com.go2group.storyboard.views;


import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import org.apache.http.conn.HttpHostConnectException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.go2group.storyboard.model.ConnectAllConfig;
import com.go2group.storyboard.model.Story;
import com.go2group.storyboard.service.ConnectAllConfigService;
import com.go2group.storyboard.service.StoryDataHandler;
import com.go2group.storyboard.service.SyncService;


/**
 * Story Board View
 * 
 * @author Vijayakumar Selvaraju
 *
 */
public class StoryBoardView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.go2group.storyboard.views.StoryBoardView";

	@Inject IWorkbench workbench;
	
	private TableViewer viewer;
	private Action syncAction;
	private Action configureAction;
	private Action addStoryAction;
	private Action doubleClickAction;
	
	private String[] columnTitles = { "Story ID", "Title", "Priority", "Status", "Linked Story ID", "Created By", "Assigned To", "Created Time", "Modified Time", "Description" };

	private int[] bounds = { 100, 100, 100, 100, 100, 100, 100, 100, 100, 150 };
	
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		createViewer(parent);
	}
	
	private void createViewer(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.BORDER);
        viewer.setContentProvider(ArrayContentProvider.getInstance());
        viewer.setLabelProvider(new LabelProvider());
        
        createColumns(parent, viewer);
        
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());
        // get the content for the viewer, setInput will call getElements in the
        // contentProvider
        viewer.setInput(StoryDataHandler.getInstance().getStories());
        
		// Create the help context id for the viewer's control
        PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "StoryBoard.viewer");

		// make the selection available to other views
        // getSite().setSelectionProvider(viewer);
		getSite().setSelectionProvider(viewer);

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();


	}
	
    private void createColumns(final Composite parent, final TableViewer viewer) {

        // Add column for Story Id
        TableViewerColumn col = createTableViewerColumn(columnTitles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Story story = (Story) element;
                return story.getId();
            }
        });

        // Add column for Story Title
        col = createTableViewerColumn(columnTitles[1], bounds[1], 0);
        col.setLabelProvider(new CellLabelProvider() {			
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Story) cell.getElement()).getTitle());				
			}
        });
        col.setEditingSupport(new AbstractTextEditingSupport(viewer) {
			@Override
			protected void setValue(Object element, Object userInputValue) {
				((Story) element).setTitle(String.valueOf(userInputValue));
				viewer.update(element, null);
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((Story) element).getTitle();
			}
		});
        
 
        // Add column for Story Priority
        col = createTableViewerColumn(columnTitles[2], bounds[2], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Story story = (Story) element;
                return story.getPriority();
            }
        });

        // Add column for Story Status
        col = createTableViewerColumn(columnTitles[3], bounds[3], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Story story = (Story) element;
                return story.getStatus()!=null?story.getStatus().name():"";
            }
        });
        col.setEditingSupport(new StatusEditingSupport(viewer));
        
        // Add column for Story Linked Story ID
        col = createTableViewerColumn(columnTitles[4], bounds[4], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Story story = (Story) element;
                return story.getLinkedStoryId();
            }
        });

        // Add column for Story Created By
        col = createTableViewerColumn(columnTitles[5], bounds[5], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Story story = (Story) element;
                return story.getCreatedBy();
            }
        });

        // Add column for Story Assigned To
        col = createTableViewerColumn(columnTitles[6], bounds[6], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Story story = (Story) element;
                return story.getAssignedTo();
            }
        });

        // Add column for Story Created Time
        col = createTableViewerColumn(columnTitles[7], bounds[7], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Story story = (Story) element;
                return story.getCreatedTimeAsString();
            }
        });

        // Add column for Story Modified Time
        col = createTableViewerColumn(columnTitles[8], bounds[8], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Story story = (Story) element;
                return story.getModifiedTimeAsString();
            }
        });


        // Add column for Story Description
        col = createTableViewerColumn(columnTitles[9], bounds[9], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Story story = (Story) element;
                return story.getDescription();
            }
        });

    }
    
    private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				StoryBoardView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(syncAction);
		manager.add(addStoryAction);
		manager.add(new Separator());
		manager.add(configureAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(syncAction);
		manager.add(addStoryAction);
		manager.add(configureAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(syncAction);
		manager.add(addStoryAction);
		manager.add(configureAction);
	}

	private void makeActions() {
		syncAction = new Action() {
			public void run() {
				if(!ConnectAllConfigService.getInstance().isConfigured()) {
					showMessage("ConnectALL configuration not found! Please complete the configuration before proceed."); 
				}
				
				List<Story> stories = null;
				try {
					stories = new SyncService().queryModifiedStoriesFromRemote();
				} catch (HttpHostConnectException e) {
					showErrorMessage("Connection Error! Pleae check the connector configuration.", e.getMessage());
				}
				
				if(stories == null) {
					return;
				}
				
				for (Story story : stories) {
					if(story.isNew()) {
						viewer.add(story);
					} else {
						viewer.update(story, null);
					}
				}
				showMessage("Stories modified : "+stories.size());
			}
		};
		syncAction.setText("Sync Stories");
		syncAction.setToolTipText("Sync stories from linked remote application");
		syncAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
		
		configureAction = new Action() {
			public void run() {
				ConnectAllConfig config = ConnectAllConfigService.getInstance().getConfig(true);
				System.out.println("Config found : "+config);
				boolean isConfigNull = (config==null);
				ConnectorConfigDialog dialog = new ConnectorConfigDialog(getViewSite().getShell(), config);
				dialog.open();
				
				if(dialog.getConnectAllConfig() != null) {
					ConnectAllConfigService.getInstance().saveConfig(dialog.getConnectAllConfig(), isConfigNull);
					showMessage("Configurations saved!");
				}
			}
		};
		configureAction.setText("Configure");
		configureAction.setToolTipText("ConnectALL configuration");
		configureAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_ETOOL_HOME_NAV));
		
		addStoryAction = new Action() {
			public void run() {
				if(!ConnectAllConfigService.getInstance().isConfigured()) {
					showMessage("ConnectALL configuration not found! Please complete the configuration before proceed."); 
				}

				StoryCreationDialog dialog = new StoryCreationDialog(getViewSite().getShell());
				dialog.open();
				
				if(dialog.getStory() != null) {
					StoryDataHandler.getInstance().addStory(dialog.getStory());
					viewer.add(dialog.getStory());
					try {
						new SyncService().createStory(dialog.getStory());
					} catch (HttpHostConnectException e1) {
						showErrorMessage("Connection Error!", e1.getMessage());
					}
					showMessage("New Story saved!");
					
					Display.getDefault().asyncExec(new Runnable() {
					    public void run() {
					    	int count = 10;
					    	Story updatedStory = dialog.getStory();
					    	do {
					    		try {
									updatedStory = new SyncService().getLinkedStoryId(updatedStory);
								} catch (HttpHostConnectException e1) {
									showErrorMessage("Connection Error!", e1.getMessage());
								}
					    		try {
									Thread.sleep(5000); // 5 sec
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					    		++count;
					    	} while(count>0 && updatedStory.getLinkedStoryId().trim().length() == 0);
					    	
					    	if(updatedStory.getLinkedStoryId().trim().length() > 0) {
					    		viewer.update(updatedStory, null);
					    	} else {
					    		System.out.println("Couldn't fetch linked record id in the given time frame: "+updatedStory);
					    	}
					    }
					});
				}
			}
		};
		addStoryAction.setText("New Story");
		addStoryAction.setToolTipText("Add new Story to the board");
		addStoryAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_ADD));

		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				Object obj = selection.getFirstElement();
				//showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Story Board",
			message);
	}
	
	private void showErrorMessage(String message, String cause) {
		MessageDialog.openError(
			viewer.getControl().getShell(),
			"Story Board",
			message+"\n\nCause:"+cause);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
