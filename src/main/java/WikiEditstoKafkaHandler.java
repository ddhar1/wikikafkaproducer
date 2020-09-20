/*  Written to handle events from "https://stream.wikimedia.org/v2/stream/recentchange" and send data to Kafka
    Parses the Json message and sends to designated KafkaProducer
 */

import java.io.StringReader;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class WikiEditstoKafkaHandler implements EventHandler {

    private WikiEditsProducer wikiEditsProducer;
    private String STREAM_URL = "https://stream.wikimedia.org/v2/stream/recentchange";


    public WikiEditstoKafkaHandler( String topicName )
    {
        super();
        this.wikiEditsProducer = new WikiEditsProducer(topicName);
    }



    public void onOpen() throws Exception {
        System.out.println("Opened connection with " + this.STREAM_URL );
    }


    public void onClosed() throws Exception {
        System.out.println("Connection closed with " + this.STREAM_URL);

    }


    public void onMessage(String s, MessageEvent messageEvent) throws Exception {
        // Parse the JSON message
        try (JsonReader jsonReader = Json
                .createReader(new StringReader(messageEvent.getData()))) {
            JsonObject jsonObject = jsonReader.readObject();
            String title = jsonObject.getValue("/title").toString();
            String changeType = jsonObject.getValue("/type").toString();

            String message = changeType + " : " + title;

            // send data - asyncronous
            this.wikiEditsProducer.sendMessage( message );
        }

    }

    @Override
    public void onComment(String comment) throws Exception {
        System.out.println(comment);
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("Something is wrong with EventHandler or WikiEdits EventStream: \n Error: " + t);
    }

}

