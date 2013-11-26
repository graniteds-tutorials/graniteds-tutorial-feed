package org.graniteds.tutorial.feed.server;

import org.granite.config.servlet3.ServerFilter;
import org.granite.gravity.config.servlet3.MessagingDestination;

// tag::server-filter[]
@ServerFilter(useBigDecimal=true) // <1>
public class FeedApplication {

    @MessagingDestination
    public String feedTopic; // <2>
}
// end::server-filter[]
