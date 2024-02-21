package org.example;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class JavaAutomationAssignment {
    /*
      @Main : Start  of execution
     */
    public static void main(String[] args) {
        String urlPath = ConfigValues.FLIPCART_URL;
        try {

            //#TODO : retrive links using iterator for- each
            retriveLinksUsingIterator(urlPath);

            //#TODO: retrive links using stream upi
            retriveLinksUsingStreamApi(urlPath);

            //#TODO: retrive links using parallel stream api
            retriveLinksUsingParallelStreamApi(urlPath);

            //#TODO: retrive links using lambda expression
            retriveLinksUsingLambdaExpression(urlPath);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     @param :
       @url : Used for connecting to website
     */
    private static void retriveLinksUsingIterator(String urlPath) throws IOException {
        try {
            System.out.println(ConfigValues.PRINT_MSG_ITERATOR);
            Document document = Jsoup.parse(new URL(urlPath), 10000);
            Elements links = document.select(ConfigValues.A_HREF);

            for (Element linkName : links) {
                System.out.println(linkName.attr(ConfigValues.ABS_HREF));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    @param :
      @url : Used for connecting to website
    */
    private static void retriveLinksUsingStreamApi(String urlPath) throws IOException {

        try {
            System.out.println(ConfigValues.PRINT_MSG_USING_STREAM_API);
            Document doc = Jsoup.parse(new URL(urlPath), 100000);
            List<String> links = doc.select(ConfigValues.A_HREF).stream().map(link -> link.attr(ConfigValues.ABS_HREF))
                    .collect(Collectors.toList());

            links.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    @param :
      @url : Used for connecting to website
    */
    private static void retriveLinksUsingParallelStreamApi(String urlPath) throws IOException {

        try {
            System.out.println(ConfigValues.PRINT_MSG_USING_PARALLEL_STREAM);
            Document doc = Jsoup.parse(new URL(urlPath), 100000);
            List<String> links = doc.select(ConfigValues.A_HREF).parallelStream().map(link -> link.attr(ConfigValues.ABS_HREF))
                    .collect(Collectors.toList());

            links.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        @param :
          @url : Used for connecting to website
        */
    private static void retriveLinksUsingLambdaExpression(String urlPath) throws IOException {

        try {
            System.out.println(ConfigValues.PRINT_MSG_USING_LAMBDA_EXPRESSION);
            Document doc = Jsoup.parse(new URL(urlPath), 100000);
            Elements links = doc.select(ConfigValues.A_HREF);

            Consumer<Element> printLink = link -> System.out.println(link.attr(ConfigValues.ABS_HREF));
            links.forEach(printLink);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
