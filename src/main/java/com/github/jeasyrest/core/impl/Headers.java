package com.github.jeasyrest.core.impl;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jeasyrest.core.IRunningContext;

public class Headers extends AbstractHeaders {

    @Override
    protected ListIterator<Entry<String, Iterable<String>>> entryIterator() {
        return new ListIterator<Entry<String, Iterable<String>>>() {
            private HttpServletRequest request;
            private HttpServletResponse response;
            private Iterator<String> headerNames;
            private Entry<String, Iterable<String>> current;

            {
                if (requestHeaders) {
                    request = IRunningContext.INSTANCE.getInstance(HttpServletRequest.class);
                    headerNames = new Iterator<String>() {
                        @Override
                        public boolean hasNext() {
                            return en.hasMoreElements();
                        }

                        @Override
                        public String next() {
                            return en.nextElement();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }

                        private Enumeration<String> en = request.getHeaderNames();
                    };
                } else {
                    response = IRunningContext.INSTANCE.getInstance(HttpServletResponse.class);
                    headerNames = response.getHeaderNames().iterator();
                }
            }

            @Override
            public boolean hasNext() {
                return headerNames.hasNext();
            }

            @Override
            public Entry<String, Iterable<String>> next() {
                final String key = headerNames.next();
                Iterable<String> value = null;
                if (requestHeaders) {
                    value = new Iterable<String>() {
                        @Override
                        public Iterator<String> iterator() {
                            return new Iterator<String>() {
                                @Override
                                public boolean hasNext() {
                                    return en.hasMoreElements();
                                }

                                @Override
                                public String next() {
                                    return en.nextElement();
                                }

                                @Override
                                public void remove() {
                                    throw new UnsupportedOperationException();
                                }

                                private Enumeration<String> en = request.getHeaders(key);
                            };
                        }
                    };
                } else {
                    value = new Iterable<String>() {
                        @Override
                        public Iterator<String> iterator() {
                            return response.getHeaders(key).iterator();
                        }
                    };
                }
                final Iterable<String> val = value;
                current = new Entry<String, Iterable<String>>() {
                    @Override
                    public String getKey() {
                        return key;
                    }

                    @Override
                    public Iterable<String> getValue() {
                        return val;
                    }

                    @Override
                    public Iterable<String> setValue(Iterable<String> value) {
                        for (String e : value) {
                            response.setHeader(key, e);
                        }
                        return value;
                    }
                };
                return current;
            }

            @Override
            public void remove() {
                if (requestHeaders) {
                    throw new UnsupportedOperationException();
                } else {
                    response.setHeader(current.getKey(), "");
                }
            }

            @Override
            public void add(Entry<String, Iterable<String>> e) {
                if (requestHeaders) {
                    throw new UnsupportedOperationException();
                } else {
                    for (String value : e.getValue()) {
                        response.addHeader(e.getKey(), value);
                    }
                }
            }
            
            @Override
            public boolean hasPrevious() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Entry<String, Iterable<String>> previous() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int nextIndex() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int previousIndex() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(Entry<String, Iterable<String>> e) {
                throw new UnsupportedOperationException();
            }
        };
    }

    protected Headers(boolean requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    private boolean requestHeaders;

}
