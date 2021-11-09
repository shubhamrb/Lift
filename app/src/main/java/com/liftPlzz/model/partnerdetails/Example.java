package com.liftPlzz.model.partnerdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

@SerializedName("response")
@Expose
private PartnerRessponse response;

public PartnerRessponse getResponse() {
return response;
}

public void setResponse(PartnerRessponse response) {
this.response = response;
}

}