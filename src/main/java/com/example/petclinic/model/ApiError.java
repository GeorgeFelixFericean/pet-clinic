package com.example.petclinic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@Validated
@JacksonXmlRootElement(localName = "ApiError")
@XmlRootElement(name = "ApiError")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiError {

    @JsonProperty("errorCode")
    @JacksonXmlProperty(localName = "errorCode")
    private Integer errorCode = null;

    @JsonProperty("errorMessage")
    @JacksonXmlProperty(localName = "errorMessage")
    private String errorMessage = null;

    public ApiError errorCode(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @ApiModelProperty(example = "7005", value = "")
    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public ApiError errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    @ApiModelProperty(example = "Sample error message", value = "")
    @Size(max = 80)
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiError apiError = (ApiError) o;
        return Objects.equals(this.errorCode, apiError.errorCode) &&
                Objects.equals(this.errorMessage, apiError.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, errorMessage);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ApiError {\n");

        sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
