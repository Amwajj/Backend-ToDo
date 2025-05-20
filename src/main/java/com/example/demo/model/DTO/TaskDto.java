package com.example.demo.model.DTO;
public class TaskDto {

    private Integer id;
    private String title;
    private String content;
    private String tag;
    private String fileName;
    private String fileType;

    private byte[] data;
    public  TaskDto(){}
    
    public TaskDto(Integer id, String title, String content ,String tag){
        this.id=id;
        this.title=title;
        this.content =content;
        this.tag=tag;
    
    }
   

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
 
}
