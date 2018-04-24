package com.taotao.portal.pojo;

/*[{
	"srcB": "http://image.taotao.com/images/2015/03/03/2015030304360302109345.jpg",
	"height": 240,
	"alt": "",
	"width": 670,
	"src": "http://image.taotao.com/images/2015/03/03/2015030304360302109345.jpg",
	"widthB": 550,
	"href": "http://sale.jd.com/act/e0FMkuDhJz35CNt.html?cpdad=1DLSUE",
	"heightB": 240
}, */
public class BigPicture {
	// 不参与网络传输，不需要序列化
	private String srcB;
	private Integer height;
	private String alt;
	private Integer width;
	private String src;
	private Integer widthB;
	private String href;
	private Integer heightB;

	public BigPicture() {
		super();
	}

	public String getSrcB() {
		return srcB;
	}

	public void setSrcB(String srcB) {
		this.srcB = srcB;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public Integer getWidthB() {
		return widthB;
	}

	public void setWidthB(Integer widthB) {
		this.widthB = widthB;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Integer getHeightB() {
		return heightB;
	}

	public void setHeightB(Integer heightB) {
		this.heightB = heightB;
	}

}
