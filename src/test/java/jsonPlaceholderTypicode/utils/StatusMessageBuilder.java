package jsonPlaceholderTypicode.utils;

import java.lang.annotation.Annotation;

public class StatusMessageBuilder {

    private Annotation step;
    private int actualResponseCode;
    private String url;

    public StatusMessageBuilder(
            Annotation step,
            int actualResponseCode,
            String url
            ){
        this.step = step;
        this.actualResponseCode = actualResponseCode;
        this.url = url;
    }

    public Annotation getStep() {
        return step;
    }


    public int getActualResponseCode() {
        return actualResponseCode;
    }

    public String getUrl() {
        return url;
    }

    public void setStep(Annotation step) {
        this.step = step;
    }

    public void setActualResponseCode(int actualResponseCode) {
        this.actualResponseCode = actualResponseCode;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
