package ua.kostenko.mydictionary.core.webpart.services.implementation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import ua.kostenko.mydictionary.core.local.database.domain.SerializableMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslateServiceResponse {
    @JsonProperty("code")
    private int code;
    @JsonProperty("lang")
    private String lang;
    @JsonProperty("translation")
    private String translation;
    @JsonProperty("translationAdditional")
    private SerializableMap additionalTranslations;
    @JsonProperty("technologies")
    private List<String> technologies;
    @JsonProperty("errorCode")
    private String errorCode;

    public TranslateServiceResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public SerializableMap getAdditionalTranslations() {
        return additionalTranslations;
    }

    public void setAdditionalTranslations(SerializableMap additionalTranslations) {
        this.additionalTranslations = additionalTranslations;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
