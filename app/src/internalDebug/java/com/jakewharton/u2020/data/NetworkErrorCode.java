package com.jakewharton.u2020.data;

public enum NetworkErrorCode {

    HTTP_403(403),
    HTTP_404(404),
    HTTP_500(500),
    HTTP_501(501),
    HTTP_503(503),
    HTTP_504(504);

    public final int code;

    NetworkErrorCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "HTTP " + code;
    }
}
