package com.paladin.bean;

public class HFile {
	public String id;
	public String fileName;
	public String fileType;
	public String filePath;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id == ((HFile) obj).id;
	}

	@Override
	public int hashCode() {
		return (this.id + "").hashCode();
	}
}
