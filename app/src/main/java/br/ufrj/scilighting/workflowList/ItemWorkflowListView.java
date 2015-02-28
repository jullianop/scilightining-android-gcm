package br.ufrj.scilighting.workflowList;

public class ItemWorkflowListView {

    private String notification;
    private String time;
    private int iconeRid;
    private int New;
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getIconeRid() {
		return iconeRid;
	}
	public void setIconeRid(int iconeRid) {
		this.iconeRid = iconeRid;
	}
	public int getNew() {
		return New;
	}
	public void setNew(int new1) {
		New = new1;
	}
	public ItemWorkflowListView(String notification, String time, int iconeRid,
			int new1) {
		super();
		this.notification = notification;
		this.time = time;
		this.iconeRid = iconeRid;
		New = new1;
	}
	public ItemWorkflowListView() {
		super();
	}
	
    
    
        
    
    
    
    
    
}
