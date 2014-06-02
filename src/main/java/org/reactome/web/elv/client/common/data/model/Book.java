package org.reactome.web.elv.client.common.data.model;

import com.google.gwt.json.client.JSONObject;
import org.reactome.web.elv.client.common.data.factory.FactoryUtils;
import org.reactome.web.elv.client.common.data.factory.SchemaClass;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public class Book extends Publication {

    private String isbn;
    private String chapterTitle;
    private String pages;
    private Integer publisher;
    private String publisherClass;
    private Integer year;

    public Book(JSONObject jsonObject) {
        super(SchemaClass.BOOK, jsonObject);

        if(jsonObject.containsKey("isbn")){
            this.isbn = FactoryUtils.getStringValue(jsonObject, "isbn");
        }

        if(jsonObject.containsKey("chapterTitle")){
            this.chapterTitle = FactoryUtils.getStringValue(jsonObject, "chapterTitle");
        }

        if(jsonObject.containsKey("pages")){
            this.pages = FactoryUtils.getStringValue(jsonObject, "pages");
        }

        if(jsonObject.containsKey("publisher")){
            this.publisher = FactoryUtils.getIntValue(jsonObject, "publisher");
        }

        if(jsonObject.containsKey("publisherClass")){
            this.publisherClass = FactoryUtils.getStringValue(jsonObject, "publisherClass");
        }

        if(jsonObject.containsKey("year")){
            this.year = FactoryUtils.getIntValue(jsonObject, "year");
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public String getPages() {
        return pages;
    }

    public Integer getPublisher() {
        return publisher;
    }

    public String getPublisherClass() {
        return publisherClass;
    }

    public Integer getYear() {
        return year;
    }
}
