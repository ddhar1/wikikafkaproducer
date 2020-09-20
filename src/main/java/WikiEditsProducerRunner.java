/*
    Meant to use WikiEditstoKafkaHandler
    and Eventbuilder.

    May further abstract this at later point
 */
import java.net.URI;

import com.launchdarkly.eventsource.EventSource;


import java.util.concurrent.TimeUnit;

public class WikiEditsProducerRunner {


    public static void main(String[] args) {

        String topicName = "wiki_edits_topic";

        String stream_url = "https://stream.wikimedia.org/v2/stream/recentchange";

        WikiEditstoKafkaHandler eventHandler = new WikiEditstoKafkaHandler( topicName );

        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(stream_url));


        try (EventSource eventSource = builder.build()) {
            eventSource.start();

            // just runs for a minute. would need a different implementation to be permanent
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}

