package com.easyrest.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.easyrest.EasyRest;
import com.easyrest.annotations.method.*;
import com.easyrest.model.request.RestObject;
import com.easyrest.netty.NettyLaunch;
import com.easyrest.netty.exception.ConfigurationException;
import com.easyrest.router.RouterProvider;
import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;

public class BindModelActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(EasyRest.class, (this::analysisRestObject)).build();
    }

    private void analysisRestObject(EasyRest easyRest){
        for (Class requestModel: easyRest.getRequestModels()) {
            if (!requestModel.isInterface()){
                throw new ConfigurationException("Only interface can be registered.");
            }
            StringBuffer[] restUri = new StringBuffer[1];
            if (requestModel.isAnnotationPresent(BindURL.class)){
                String[] uris = ((BindURL)requestModel.getAnnotation(BindURL.class)).value();
                restUri = new StringBuffer[uris.length];
                for (int i = 0; i < restUri.length; i++) {
                    restUri[i] = new StringBuffer(uris[i]);
                }
            }
            for (Method method : requestModel.getMethods()) {
                if (method.isAnnotationPresent(Post.class)) {
                    String url = method.getAnnotation(Post.class).url();
                    Class controller = method.getAnnotation(Post.class).controller();
                    RestObject restObject = new RestObject(method, HttpMethod.POST, controller);
                    for (StringBuffer aRestUri : restUri) {
                        putRouter(aRestUri + url, HttpMethod.POST, restObject);
                    }
                }
                if (method.isAnnotationPresent(Get.class)) {
                    String url = method.getAnnotation(Get.class).url();
                    Class controller = method.getAnnotation(Get.class).controller();
                    RestObject restObject = new RestObject(method, HttpMethod.GET, controller);
                    for (StringBuffer aRestUri : restUri) {
                        putRouter(aRestUri + url, HttpMethod.GET, restObject);
                    }
                }
                if (method.isAnnotationPresent(Put.class)) {
                    String url = method.getAnnotation(Put.class).url();
                    Class controller = method.getAnnotation(Put.class).controller();
                    RestObject restObject = new RestObject(method, HttpMethod.PUT, controller);
                    for (StringBuffer aRestUri : restUri) {
                        putRouter(aRestUri + url, HttpMethod.PUT, restObject);
                    }
                }
                if (method.isAnnotationPresent(Delete.class)) {
                    String url = method.getAnnotation(Delete.class).url();
                    Class controller = method.getAnnotation(Delete.class).controller();
                    RestObject restObject = new RestObject(method, HttpMethod.DELETE, controller);
                    for (StringBuffer aRestUri : restUri) {
                        putRouter(aRestUri + url, HttpMethod.DELETE, restObject);
                    }
                }
            }
        }
        ActorFactory.createActor(NettyLaunch.class).tell(easyRest, ActorRef.noSender());
    }

    private void putRouter(String url, HttpMethod httpMethod, RestObject restObject){
        if (RouterProvider.getRestObjectMap().containsKey(url)){
            RouterProvider.getRestObjectMap().get(url).addHttpMethod(httpMethod);
        } else {
            RouterProvider.getRestObjectMap().put(url, restObject);
        }
    }

}
