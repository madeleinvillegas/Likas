package com.example.likas.models;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private String uid;
    private String name;
    private String content;
    private String date;
    private List<String> tags = new ArrayList<>();
    private String key;

    public Post(){}
    public Post(String uid, String name, String content,String date,List<String> tags){
        this.uid = uid;
        this.name = name;
        this.content = content;
        this.date = date;
        this.tags = tags;
    }


    public String getTags()
    {
        String s = tags.toString();
        s = s.replace("[", "").replace("]", "").trim();
        return s;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getListTags(){
        return tags;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append( this.getClass().getName() );
        result.append( " Object {" );
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for ( Field field : fields  ) {
            result.append("  ");
            try {
                result.append( field.getName() );
                result.append(": ");
                //requires access to private field:
                result.append( field.get(this) );
            } catch ( IllegalAccessException ex ) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }

}
