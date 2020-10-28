package com.kiennguyen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author kien.nguyen
 */
public class PrettyJsonPrint {
    public static void main(String[] args) throws Exception
    {
        Book book = new Book();
        book.author = "Steve Jin";
        book.title = "VMware vSphere and VI SDK";

        // using Gson API
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String bookJson = gson.toJson(book);
        System.out.println("pretty print json using Gson:\n" + bookJson);

        // using Jackson API
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//        bookJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(book);
//        System.out.println("pretty print json using Jackson:\n" + bookJson);
    }
}

class Book
{
    public String author;
    public String title;
}
