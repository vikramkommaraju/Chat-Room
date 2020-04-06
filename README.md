# Chat Room Application that makes use of Publish Status feature
![Alt text](screenshot.png?raw=true "Chat Room")

## Running
```mvn clean package; mvn spring-boot:run```

## To Simulate Publish Failures
Add this to PlatformEventPublisher.java.  
```
 LazyProvider<AsyncPublishFailureObserverNotifier> failureNotifier = new LazyProvider<>(AsyncPublishFailureObserverNotifier.class);
```

In ASYNC.publishInternal(), update to following.  

```
boolean success = true;
String eventRecordId = idStrategy.constructEventRecordId(eventEntityInfo);
try {
    if(shouldFailPublish(topic, rowData, event)) {
        failureNotifier.get().notifyAllObserversNoThrow(getPublishFailure(topic, event));
    } else {
        success = conduit.pushAsync(topic, event);                                                            
    }
    if (success) {
        resultProcessor.onPublishSucceeded(rowData, event, eventRecordId);
    } else {
        logPublishFailure(topic, null/* exception */, eventEntityInfo,
                "Failed to push event async");
        resultProcessor.onPublishFailed(rowData, event, "Failed to push event async");
    }
} catch (RateLimitingException exception) { ... }
```
New Helper methods in PlatformEventPublisher
```
private boolean shouldFailPublish(Topic topic, EventConduitDataSourceRowData rowData, ProducerEvent event) {
    return rowData.getColumnValues().values().stream().filter(v -> v.equals("fail this.")).findAny().isPresent();
}

private PublishFailure getPublishFailure(Topic topic, ProducerEvent event) {
    return new PublishFailure() {

        @Override
        public Topic getTopic() {
            return topic;
        }

        @Override
        public ServiceLevel getServiceLevel() {
            return ServiceLevel.HIGH_VOLUME;
        }

        @Override
        public Optional<PushContext> getPushContextSnapshot() {
            return Optional.empty();
        }

        @Override
        public PublishMethodType getPublishMethodType() {
            return PublishMethodType.ASYNC;
        }

        @Override
        public Optional<String> getOrgId() {
            return Optional.of(topic.partition());
        }

        @Override
        public Optional<String> getMessage() {
            return Optional.empty();
        }

        @Override
        public PublishLayer getLayerWhereFailureOccurred() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getId() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public PublishFailureReason getFailureReason() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public FailureMode getFailureMode() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Optional<Exception> getException() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ProducerEvent getEvent() {
            return event;
    }};
}
