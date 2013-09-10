package com.home.wrm.client.presenter;

import java.util.List;

import com.google.gwt.user.client.Timer;
import com.home.wrm.client.util.ITransmitter;
import com.home.wrm.client.util.adapter.Adaptable;
import com.home.wrm.client.util.callback.IFullCallbackExecutor;
import com.home.wrm.client.util.callback.ISuccessCallbackExecutor;
import com.home.wrm.client.util.datasource.DirectoryTreeNode;
import com.home.wrm.client.util.datasource.ResourceDatasource.ResourceRecord;
import com.home.wrm.client.view.FolderSelectionView.MoveFolderSelectionView;
import com.home.wrm.client.view.FolderSelectionView.MoveResourceSelectionView;
import com.home.wrm.shared.transport.Directory;
import com.home.wrm.shared.transport.Resource;
import com.home.wrm.shared.transport.SearchFilter;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemKeyPressEvent;
import com.smartgwt.client.widgets.form.events.ItemKeyPressHandler;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.BodyKeyPressEvent;
import com.smartgwt.client.widgets.grid.events.BodyKeyPressHandler;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.grid.events.RowContextClickEvent;
import com.smartgwt.client.widgets.grid.events.RowContextClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

public class WrProcessingPresenter extends BasePresenter<WrProcessingPresenter.IAdapter> implements ITransmitter {
    private DirectoryTreeNode selectedTreeNode;
    private Menu folderContextMenu;
    private ResourceRecord record;
    private Menu rootMenu;
	
	public WrProcessingPresenter(IAdapter adapter) {
        super(adapter);
    }
	
	public interface IAdapter extends Adaptable {
		ButtonItem getSaveButton();
		Tree getTree(TreeNode[] nodes);
		TreeGrid getDirectoriesTree();
		ListGrid getResourceGrid();
		TextItem getSearchingField();
		Resource composeUserData();
		Directory getSelectedDirectory();
		DynamicForm getAddResourceForm();
		boolean validateResourceInsertion();
		void clearUI();
	}
	
	@Override
	public void addPageActions() {
	    getAdapter().getAddResourceForm().addItemKeyPressHandler(new ItemKeyPressHandler() {
            public void onItemKeyPress(ItemKeyPressEvent event) {
                if ("Enter".equalsIgnoreCase(event.getKeyName())) {
                    getAdapter().getSaveButton().fireEvent(new ClickEvent(getAdapter().getSaveButton().getJsObj()));
                }
            }
        });
	    
		getAdapter().getSaveButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			    if (getAdapter().validateResourceInsertion()) {
			        SC.ask("Save resource to selected folder?", new BooleanCallback() {
	                    public void execute(Boolean yes) {
	                        if (yes != null && yes.booleanValue()) {
	                            Resource resource = getAdapter().composeUserData();
	                            showModalPromptToUser("Saving resource...");
	                            rpc.saveWebResource(resource, newSuccessCallback(new ISuccessCallbackExecutor<Resource>() {
                                    public void onSuccessCallbackAction(Resource createdResource) {
                                        ResourceRecord resourceRecord = new ResourceRecord(createdResource);
                                        getAdapter().getResourceGrid().getDataSource().addData(resourceRecord);
                                        getAdapter().getResourceGrid().fetchData();
                                        
                                        getAdapter().clearUI();
                                    }
                                }));
	                        }
	                    }
	                });
			    }
			}
		});
		
		getAdapter().getDirectoriesTree().addShowContextMenuHandler(new ShowContextMenuHandler() {
            public void onShowContextMenu(ShowContextMenuEvent event) {
                event.cancel();
                ListGridRecord selectedRecord = getAdapter().getDirectoriesTree().getSelectedRecord();
                if (selectedRecord != null) {
                    int selectedRecordIndex = getAdapter().getDirectoriesTree().getRecordIndex(selectedRecord);
                    if (selectedRecordIndex != -1) {
                        getAdapter().getDirectoriesTree().deselectRecord(selectedRecord);
                        getAdapter().getDirectoriesTree().refreshRow(selectedRecordIndex);
                    }
                }
                selectedTreeNode = null;
                showRootContextMenu();
            }
        });
		
		getAdapter().getDirectoriesTree().addRowContextClickHandler(new RowContextClickHandler() {
            public void onRowContextClick(RowContextClickEvent event) {
                selectedTreeNode = (DirectoryTreeNode) event.getRecord();
                showFolderContextMenu();
                getAdapter().getDirectoriesTree().setContextMenu(null);
            }
        });
		
		getAdapter().getDirectoriesTree().addBodyKeyPressHandler(new BodyKeyPressHandler() {
            public void onBodyKeyPress(BodyKeyPressEvent event) {
                event.cancel();
            }
        });
		
		getAdapter().getDirectoriesTree().addSelectionUpdatedHandler(new SelectionUpdatedHandler() {
		    private Directory prevSelectedDirectory;
            public void onSelectionUpdated(SelectionUpdatedEvent event) {
                Directory selectedDirectory = getAdapter().getSelectedDirectory();
                System.out.println("+PrevSelDir: " + prevSelectedDirectory);
                if (selectedDirectory == null) {
                    prevSelectedDirectory = null;
                    clearResourceTable();
                } else if (!selectedDirectory.equals(prevSelectedDirectory)) {
                    prevSelectedDirectory = selectedDirectory;
                    executeRpcCallToLoadResources(selectedDirectory.getId());
                }
            }
        });
		
		getAdapter().getResourceGrid().addEditorExitHandler(new EditorExitHandler() {
            public void onEditorExit(EditorExitEvent event) {
                FormItem editFormItem = getAdapter().getResourceGrid().getEditFormItem(3);
                
                Object newName = editFormItem.getValue();
                if (newName == null) {
                    event.cancel();
                    showWarning("Resource name should not be empty.");
                    editFormItem.addBlurHandler(new BlurHandler() {
                        public void onBlur(BlurEvent event) {
                            if (event.getItem().getValue() == null) {
                                event.getItem().focusInItem();
                            }
                        }
                    });
                } else {
                    ResourceRecord resourceRecord = (ResourceRecord) event.getRecord();
                    Resource resource = resourceRecord.asResource();
                    String newResName = newName.toString();
                    if (!newResName.equals(resource.getName())) {
                        executeRpcCallToUpdateResourceName(resource.getId(), event);
                    }
                }
            }
        });
		
		getAdapter().getSearchingField().addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent event) {
                if ("Enter".equalsIgnoreCase(event.getKeyName())) {
                    TextItem searchingField = getAdapter().getSearchingField();
                    Object wildSearchingValue = searchingField.getValue();
                    if (wildSearchingValue == null) {
                        showWarning("Searching value is empty.");
                        return;
                    }
                    String stVal = searchingField.getForm().getValueAsString("searchComboFilter");
                    executeRpcCallToSearchResource(wildSearchingValue.toString(), SearchFilter.get(stVal));
                }
            }
        });
	}
	
	private void clearResourceTable() {
	    ListGrid listGrid = getAdapter().getResourceGrid();
        listGrid.getDataSource().invalidateCache();
        listGrid.invalidateRecordComponents();
        listGrid.invalidateCache();
	}
	
	private void showResources(List<Resource> resources) {
        ResourceRecord[] resArray = new ResourceRecord[resources.size()];
        for (int index = 0; index < resources.size(); index++) {
            resArray[index] = new ResourceRecord(resources.get(index));
            //add
            getAdapter().getResourceGrid().getDataSource().addData(resArray[index]);
        }
        //add
        getAdapter().getResourceGrid().fetchData();
//        getAdapter().getListGrid().setData(resArray);
    }
	
	public void showDirs(List<Directory> dirs) {
        TreeNode[] nodes = new TreeNode[dirs.size()];
        for (int index = 0; index < dirs.size(); index++) {
            nodes[index] = new DirectoryTreeNode(dirs.get(index));
        }
        Tree tree = getAdapter().getTree(nodes);
        getAdapter().getDirectoriesTree().setData(tree);
        getAdapter().getDirectoriesTree().getData().openAll();
    }
    
    private void askForFolderName() {
        SC.askforValue("Enter value: ", new ValueCallback() {
            public void execute(String name) {
                if (name == null) {
                } else if ("".equals(name)) {
                    SC.warn("Folder name should not be empty.", new BooleanCallback() {
                        public void execute(Boolean value) {
                            askForFolderName();
                        }
                    });
                } else {
                    clearResourceTable();
                    executeRpcCallToCreateFolder(name);
                }
            }
        });
    }
    
    private void askForRenamingFolder(final int dirId, final String defaultFolderName) {
        Dialog dialog = new Dialog();
        dialog.setCanSelectText(true);
        dialog.setWidth("300px");
        SC.askforValue("Rename folder", "New name: ", defaultFolderName, new ValueCallback() {
            public void execute(String name) {
                if (name == null) {
                } else if ("".equals(name)) {
                    SC.warn("Folder name should not be empty.", new BooleanCallback() {
                        public void execute(Boolean value) {
                            askForRenamingFolder(dirId, defaultFolderName);
                        }
                    });
                } else if (!defaultFolderName.equals(name)) {
                    executeRpcCallToRenameFolder(dirId, name);
                }
            }
        }, dialog);
    }
	
	private void showRootContextMenu() {
	    MenuItem menuItem = new MenuItem("Add root folder...");
	    menuItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
            public void onClick(MenuItemClickEvent event) {
                askForFolderName();
            }
        });
        rootMenu = new Menu();
        rootMenu.setItems(menuItem);
        getAdapter().getDirectoriesTree().setContextMenu(rootMenu);
        rootMenu.showContextMenu();
	}
	
	private void showFolderContextMenu() {
        if (folderContextMenu == null) {
            MenuItem menuItem1 = new MenuItem("New sub folder...");
            MenuItem menuItem2 = new MenuItem("Move to...");
            MenuItem menuItem3 = new MenuItem("Delete");
            MenuItem menuItem4 = new MenuItem("Rename");
            
            menuItem1.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
                public void onClick(MenuItemClickEvent event) {
                    askForFolderName();
                }
            });
            menuItem2.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
                public void onClick(MenuItemClickEvent event) {
                    final int id = selectedTreeNode.asDirectory().getId();
                    rpc.listAccessableDirs(id, newSuccessCallback(new ISuccessCallbackExecutor<List<Directory>>() {
                        public void onSuccessCallbackAction(List<Directory> dirs) {
                            new MoveFolderSelectionView(id, dirs, WrProcessingPresenter.this).load();
                        }
                    }));
                }
            });
            menuItem3.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
                public void onClick(MenuItemClickEvent event) {
                    final Directory directory = selectedTreeNode.asDirectory();
                    SC.ask("Remove folder <b>"+directory.getName()+"</b> with all inner resources and dirs?", new BooleanCallback() {
                        public void execute(Boolean ok) {
                            if (ok != null && ok.booleanValue()) {
                                showModalPromptToUser("Deleting folder...");
                                rpc.removeDirectory(directory, newSuccessCallback(new ISuccessCallbackExecutor<Void>() {
                                    public void onSuccessCallbackAction(Void result) {
                                        getAdapter().getDirectoriesTree().removeData(selectedTreeNode);
                                        clearResourceTable();
                                    }
                                }));
                            }
                        }
                    });
                }
            });
            
            menuItem4.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
                public void onClick(MenuItemClickEvent event) {
                    Directory selectedDirectory = getAdapter().getSelectedDirectory();
                    askForRenamingFolder(selectedDirectory.getId(), selectedDirectory.getName());
                }
            });
            
            folderContextMenu = new Menu();
            folderContextMenu.setItems(menuItem1, menuItem2, menuItem3, menuItem4);
        }
        folderContextMenu.showContextMenu();
    }
	
	/************************************ RPC CALLS ************************************/
	private void executeRpcCallToSearchResource(final String wildSearchingValue, final SearchFilter searchFilter) {
	    showModalPromptToUser("Looking for resources [<b>" + wildSearchingValue + "</b>]");
	    
	    rpc.searchResource(wildSearchingValue.toUpperCase(), searchFilter, newSuccessCallback(new ISuccessCallbackExecutor<List<Resource>>() {
	        public void onSuccessCallbackAction(List<Resource> resources) {
	            if (resources.isEmpty()) {
	                showWarning("No resources found for [<b>" + wildSearchingValue + "</b>]");
	                return;
	            }
	            getAdapter().getDirectoriesTree().deselectAllRecords();
	            selectedTreeNode = null;
	            clearResourceTable();
	            showResources(resources);
	            //show icon
	        }
        }));
	}
	
	private void executeRpcCallToRenameFolder(int dirId, String newDirName) {
	    showModalPromptToUser("Renaming directory to [<b>" + newDirName + "</b>]...");
	    rpc.renameDirectoryTo(dirId, newDirName, newSuccessCallback(new ISuccessCallbackExecutor<List<Directory>>() {
	        public void onSuccessCallbackAction(List<Directory> dirs) {
	            showDirs(dirs);
	        }
        }));
	}
	
	private void executeRpcCallToUpdateResourceName(int resId, final EditorExitEvent event) {
	    String newName = event.getNewValue().toString();
	    showModalPromptToUser("Updating resource name to <b>" + newName + "</b>...");
	    rpc.updateResourceName(resId, newName, newFullCallback(new IFullCallbackExecutor<Void>() {
	        public void onSuccessCallbackAction(Void result) {
	            //it is ok
	        }
	        public void onFailureCallbackAction(Throwable caught) {
	            showWarning(caught.getMessage());
	            event.cancel();
	            getAdapter().getResourceGrid().discardEdits(event.getRowNum(), event.getColNum());
	            getAdapter().getResourceGrid().updateData(event.getRecord());
	        }
        }));
	}
	
	private void executeRpcCallToLoadResources(int folderId) {
	    clearResourceTable();
        showModalPromptToUser("Loading resources...");
        rpc.getResourcesFor(folderId, newSuccessCallback(new ISuccessCallbackExecutor<List<Resource>>() {
            public void onSuccessCallbackAction(List<Resource> resources) {
                showResources(resources);
            }
        }));
	}
	
	private void executeRpcCallToCreateFolder(String name) {
        int parentDirectoryId = 0;
        if (selectedTreeNode != null) {
            Directory selectedDirectory = selectedTreeNode.asDirectory();
            if (selectedDirectory != null) {
                parentDirectoryId = selectedDirectory.getId();
            }
        }
        Directory directory = new Directory();
        directory.setName(name);
        directory.setParentDirectoryId(parentDirectoryId);
        
        showModalPromptToUser("Creating new folder...");
        rpc.createNewFolder(directory, newSuccessCallback(new ISuccessCallbackExecutor<List<Directory>>() {
            public void onSuccessCallbackAction(List<Directory> dirs) {
                showDirs(dirs);
            }
        }));
    }
	
	public void removeResourceAction(Resource resource, final Record record) {
        showModalPromptToUser("Removing resource...");
        
        rpc.removeResource(resource, newSuccessCallback(new ISuccessCallbackExecutor<List<Resource>>() {
            public void onSuccessCallbackAction(List<Resource> resources) {
                getAdapter().getResourceGrid().getDataSource().removeData(record);
            }
        }));
    }
	
	public void loadPreferDirs(final int resourceId, int dirIdOfResource, ResourceRecord record) {
	    this.record = record;
        rpc.listTargetDirectories(dirIdOfResource, newSuccessCallback(new ISuccessCallbackExecutor<List<Directory>>() {
            public void onSuccessCallbackAction(List<Directory> dirs) {
                new MoveResourceSelectionView(resourceId, dirs, WrProcessingPresenter.this).load();
            }
        }));
    }
	
	public void loadAllDirectories() {
	    showModalPromptToUser("Loading directories...");
	    
	    Timer timer = new Timer() {
            @Override
            public void run() {
                rpc.getResourceDirectories(newSuccessCallback(new ISuccessCallbackExecutor<List<Directory>>() {
                    public void onSuccessCallbackAction(List<Directory> dirs) {
                        showDirs(dirs);
                    }
                }));
                cancel();
            }
        };
        
        timer.schedule(500);
	}
	
	public void refreshResourceTable() {
	    if (record != null) {
	        getAdapter().getResourceGrid().getDataSource().removeData(record);
	        record = null;
	    }
	}
}
