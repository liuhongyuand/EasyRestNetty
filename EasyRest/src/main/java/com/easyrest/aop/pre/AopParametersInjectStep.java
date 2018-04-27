package com.easyrest.aop.pre;

import com.easyrest.annotations.parameter.validation.AllDefinedValidation;
import com.easyrest.aop.AopPreCommitStep;
import com.easyrest.aop.resolvers.FormDataResolve;
import com.easyrest.aop.resolvers.JsonDataResolve;
import com.easyrest.aop.resolvers.UrlDataResolve;
import com.easyrest.exception.ParameterNotFoundException;
import com.easyrest.model.HttpEntity;
import io.netty.handler.codec.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class AopParametersInjectStep implements AopPreCommitStep {

    @Override
    public HttpEntity executeStep(HttpEntity entity) {
        return inject(entity);
    }

    private HttpEntity inject(HttpEntity httpEntity){
        if (httpEntity.getRequest().isMultipart()){
            httpEntity.setArgs(FormDataResolve.resolveArgs(httpEntity));
        } else {
            if (httpEntity.getRequest().getRequestHttpMethod().equalsIgnoreCase(HttpMethod.GET.name())){
                httpEntity.setArgs(UrlDataResolve.resolveArgs(httpEntity));
            } else {
                httpEntity.setArgs(JsonDataResolve.resolveArgs(httpEntity));
            }
        }
        List<String> errorParams = validationParameters(httpEntity);
        if (errorParams.size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            errorParams.forEach((name) -> stringBuilder.append(name).append(", "));
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            if (errorParams.size() == 1){
                stringBuilder.append(" is not defined.");
            } else {
                stringBuilder.append(" are not defined.");
            }
            httpEntity.addError(new ParameterNotFoundException(stringBuilder.toString()));
        }
        return httpEntity;
    }

    private List<String> validationParameters(HttpEntity httpEntity){
        List<String> errorNames = new ArrayList<>();
        errorNames.addAll(AllDefinedValidation.validate(httpEntity));
        return errorNames;
    }
}
