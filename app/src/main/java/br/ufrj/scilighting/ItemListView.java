package br.ufrj.scilighting;

public class ItemListView {

    private String text;
    private String text2;
    private String text3;
    private String text4;
    private int iconeRid;
	
       
    
    public ItemListView(String text, String text2, String text3, String text4,
			int iconeRid) {
		super();
		this.text = text;
		this.text2 = text2;
		this.text3 = text3;
		this.text4 = text4;
		this.iconeRid = iconeRid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getText2() {
		return text2;
	}
	public void setText2(String text2) {
		this.text2 = text2;
	}
	public String getText3() {
		return text3;
	}
	public void setText3(String text3) {
		this.text3 = text3;
	}
	public String getText4() {
		return text4;
	}
	public void setText4(String text4) {
		this.text4 = text4;
	}
	public int getIconeRid() {
		return iconeRid;
	}
	public void setIconeRid(int iconeRid) {
		this.iconeRid = iconeRid;
	}

    
}
