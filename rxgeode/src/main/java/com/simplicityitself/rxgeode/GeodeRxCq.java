package com.simplicityitself.rxgeode;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.query.*;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeodeRxCq implements Publisher<CqEvent> {

    private ClientCache cache;
    private String basename;
    private String query;

    GeodeRxCq(
            ClientCache cache,
            String basename,
            String query) {

        this.cache = cache;
        this.basename = basename;
        this.query = query;
    }

    @Override
    public void subscribe(Subscriber<? super CqEvent> s) {
        s.onSubscribe(new GeodeCqSubscription(s));
    }

    class GeodeCqSubscription implements Subscription {

        private Subscriber<? super CqEvent> sub;
        private CqQuery cqQuery;
        private GeodeRxCqListener listener;

        boolean started = false;
        private Exception bootException;

        GeodeCqSubscription(Subscriber<? super CqEvent> subscriber) {
            this.sub = subscriber;

            try {
                CqAttributesFactory cqf = new CqAttributesFactory();
                listener = new GeodeRxCqListener(sub);
                cqf.addCqListener(listener);
                CqAttributes cqa = cqf.create();

                //todo, this will duplicate the name on second subscription
                cqQuery = cache.getQueryService().newCq(basename, query, cqa);

            } catch (Exception e) {
                try {
                    if (cqQuery != null) cqQuery.stop();
                } catch (CqException e1) {
                    e1.printStackTrace();
                }
                bootException = e;
            }
        }

        @Override
        public void request(long n) {
            //TODO, respect the back pressure signals.
            if(!started) {
                if (bootException != null) {
                    try {
                        if (cqQuery != null) cqQuery.stop();
                    } catch (CqException e) {
                        e.printStackTrace();
                    }
                    sub.onError(bootException);
                    return;
                }
                try {
                    cqQuery.execute();
                    System.out.println("Initialised RxCQ");
                    started = true;
                } catch (Exception e) {
                    sub.onError(e);
                }
            }
        }

        @Override
        public void cancel() {
            try {
                cqQuery.stop();
            } catch (CqException e) {
                sub.onError(e);
            }
        }
    }
}
