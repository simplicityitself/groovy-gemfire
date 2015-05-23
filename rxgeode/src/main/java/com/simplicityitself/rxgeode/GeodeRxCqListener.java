package com.simplicityitself.rxgeode;

import com.gemstone.gemfire.cache.query.CqEvent;
import com.gemstone.gemfire.cache.query.CqListener;
import org.reactivestreams.Subscriber;

class GeodeRxCqListener implements CqListener {

    private Subscriber<? super CqEvent> subscriber;

    GeodeRxCqListener(Subscriber<? super CqEvent> sub) {
        this.subscriber = sub;
    }

    @Override
    public void onEvent(CqEvent cqEvent) {
        //TODO, respect backpressure signals.
        subscriber.onNext(cqEvent);
    }

    @Override
    public void onError(CqEvent cqEvent) {
        subscriber.onError(cqEvent.getThrowable());
    }

    @Override
    public void close() {
        subscriber.onComplete();
    }
}
