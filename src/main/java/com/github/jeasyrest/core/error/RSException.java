package com.github.jeasyrest.core.error;

@SuppressWarnings("serial")
public class RSException extends RuntimeException {

    public RSException(Number status, String message) {
        super("[" + status.intValue() + "] - " + getMessage(status, message));
        this.status = status.intValue();
    }

    public int getStatus() {
        return status;
    }

    private int status;

    public interface Codes {
        Code STATUS_100 = new Code(100, "Continue");
        Code STATUS_101 = new Code(101, "Switching Protocols");
        Code STATUS_102 = new Code(102, "Processing");
        Code STATUS_200 = new Code(200, "OK");
        Code STATUS_201 = new Code(201, "Created");
        Code STATUS_202 = new Code(202, "Accepted");
        Code STATUS_203 = new Code(203, "Non-Authoritative Information");
        Code STATUS_204 = new Code(204, "No Content");
        Code STATUS_205 = new Code(205, "Reset Content");
        Code STATUS_206 = new Code(206, "Partial Content");
        Code STATUS_207 = new Code(207, "Multi-Status");
        Code STATUS_208 = new Code(208, "Already Reported");
        Code STATUS_226 = new Code(226, "IM Used");
        Code STATUS_300 = new Code(300, "Multiple Choices");
        Code STATUS_301 = new Code(301, "Moved Permanently");
        Code STATUS_302 = new Code(302, "Found");
        Code STATUS_303 = new Code(303, "See Other");
        Code STATUS_304 = new Code(304, "Not Modified");
        Code STATUS_305 = new Code(305, "Use Proxy");
        Code STATUS_307 = new Code(307, "Temporary Redirect");
        Code STATUS_308 = new Code(308, "Permanent Redirect");
        Code STATUS_400 = new Code(400, "Bad Request");
        Code STATUS_401 = new Code(401, "Unauthorized");
        Code STATUS_402 = new Code(402, "Payment Required");
        Code STATUS_403 = new Code(403, "Forbidden");
        Code STATUS_404 = new Code(404, "Not Found");
        Code STATUS_405 = new Code(405, "Method Not Allowed");
        Code STATUS_406 = new Code(406, "Not Acceptable");
        Code STATUS_407 = new Code(407, "Proxy Authentication Required");
        Code STATUS_408 = new Code(408, "Request Timeout");
        Code STATUS_409 = new Code(409, "Conflict");
        Code STATUS_410 = new Code(410, "Gone");
        Code STATUS_411 = new Code(411, "Length Required");
        Code STATUS_412 = new Code(412, "Precondition Failed");
        Code STATUS_413 = new Code(413, "Request Entity Too Large");
        Code STATUS_414 = new Code(414, "Request-URI Too Long");
        Code STATUS_415 = new Code(415, "Unsupported Media Type");
        Code STATUS_416 = new Code(416, "Requested Range Not Satisfiable");
        Code STATUS_417 = new Code(417, "Expectation Failed");
        Code STATUS_418 = new Code(418, "I'm a teapot (RFC 2324)");
        Code STATUS_420 = new Code(420, "Enhance Your Calm");
        Code STATUS_422 = new Code(422, "Unprocessable Entity");
        Code STATUS_423 = new Code(423, "Locked");
        Code STATUS_424 = new Code(424, "Failed Dependency");
        Code STATUS_425 = new Code(425, "Reserved for WebDAV");
        Code STATUS_426 = new Code(426, "Upgrade Required");
        Code STATUS_428 = new Code(428, "Precondition Required");
        Code STATUS_429 = new Code(429, "Too Many Requests");
        Code STATUS_431 = new Code(431, "Request Header Fields Too Large");
        Code STATUS_444 = new Code(444, "No Response");
        Code STATUS_449 = new Code(449, "Retry With");
        Code STATUS_450 = new Code(450, "Blocked by Windows Parental Controls");
        Code STATUS_451 = new Code(451, "Unavailable For Legal Reasons");
        Code STATUS_499 = new Code(499, "Client Closed Request");
        Code STATUS_500 = new Code(500, "Internal Server Error");
        Code STATUS_501 = new Code(501, "Not Implemented");
        Code STATUS_502 = new Code(502, "Bad Gateway");
        Code STATUS_503 = new Code(503, "Service Unavailable");
        Code STATUS_504 = new Code(504, "Gateway Timeout");
        Code STATUS_505 = new Code(505, "HTTP Version Not Supportedr");
        Code STATUS_506 = new Code(506, "Variant Also Negotiates");
        Code STATUS_507 = new Code(507, "Insufficient Storage");
        Code STATUS_508 = new Code(508, "Loop Detected");
        Code STATUS_509 = new Code(509, "Bandwidth Limit Exceeded");
        Code STATUS_510 = new Code(510, "Not Extended");
        Code STATUS_511 = new Code(511, "Network Authentication Required");
        Code STATUS_598 = new Code(598, "Network read timeout error");
        Code STATUS_599 = new Code(599, "Network connect timeout error");

    }

    public static final class Code extends Number {
        @Override
        public String toString() {
            return "HTTP Status Code [code = " + code + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + code;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Code other = (Code) obj;
            if (code != other.code)
                return false;
            return true;
        }

        @Override
        public int intValue() {
            return code;
        }

        @Override
        public long longValue() {
            return code;
        }

        @Override
        public float floatValue() {
            return code;
        }

        @Override
        public double doubleValue() {
            return code;
        }

        private Code(int code, String description) {
            this.code = code;
            this.description = description;
        }

        private int code;
        private String description;
    }

    private static String getMessage(Number status, String message) {
        StringBuilder msg = new StringBuilder();
        if (status instanceof Code) {
            msg.append(((Code) status).description);
        }
        if (message != null && message.trim().length() != 0) {
            msg.append(" - ").append(message);
        }
        return msg.toString();
    }

}
